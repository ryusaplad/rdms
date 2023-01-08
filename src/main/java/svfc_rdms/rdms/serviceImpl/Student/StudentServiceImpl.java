package svfc_rdms.rdms.serviceImpl.Student;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Document.DocumentService;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Student.StudentService;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Service
public class StudentServiceImpl implements StudentService, FileService, DocumentService {

     @Autowired
     private StudentRepository studentRepository;

     @Autowired
     private DocumentRepository documentRepository;

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Override
     public String displayStudentRequests(Model model, HttpSession session) {
          try {
               String username = "";
               if (session.getAttribute("username") != null) {
                    username = session.getAttribute("username").toString();
                    Users user = displayAllRequestByStudent(username).get();

                    List<StudentRequest> fetchStudentRequest = displayAllRequestByStudent(user);
                    if (fetchStudentRequest.size() > -1) {
                         List<StudentRequest_Dto> studentRequests = new ArrayList<>();

                         fetchStudentRequest.stream().forEach(req -> {
                              studentRequests
                                        .add(new StudentRequest_Dto(req.getRequestId(), req.getRequestBy().getUserId(),
                                                  req.getRequestBy().getType(), req.getYear(),
                                                  req.getCourse(), req.getSemester(),
                                                  req.getRequestDocument().getTitle(),
                                                  req.getMessage(), req.getReply(), req.getRequestBy().getName(),
                                                  req.getRequestDate(),
                                                  req.getRequestStatus(), req.getReleaseDate(), req.getManageBy()));
                         });
                         model.addAttribute("myrequest", studentRequests);
                         return "/student/student-request-list";
                    }
                    return "redirect:/";
               }

               return "redirect:/student/dashboard";
          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }
     }

     @Override
     public ResponseEntity<String> saveRequest(String requestId,
               Optional<MultipartFile[]> uploadedFiles, String document,
               Map<String, String> params) {

          try {

               List<String> excludedFiles = new ArrayList<>();

               LocalDateTime myDateObj = LocalDateTime.now();
               DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
               String formattedDate = myDateObj.format(myFormatObj);
               if (uploadedFiles.isPresent() || !requestId.isEmpty() || !document.isEmpty() || params.size() > 0) {
                    StudentRequest req = new StudentRequest();
                    Users user = new Users();

                    for (Map.Entry<String, String> entry : params.entrySet()) {

                         if (entry.getKey().equals("studentId")) {
                              user = usersRepository.findUserIdByUsername(entry.getValue()).get();
                              req.setRequestBy(user);
                         } else if (entry.getKey().equals("year")) {
                              req.setYear(entry.getValue());
                         } else if (entry.getKey().equals("course")) {
                              req.setCourse(entry.getValue());
                         } else if (entry.getKey().equals("semester")) {
                              req.setSemester(entry.getValue());
                         } else if (entry.getKey().contains("excluded")) {
                              excludedFiles.add(entry.getValue());
                         } else if (entry.getKey().contains("message")) {
                              if (entry.getValue().length() > 250) {
                                   throw new ApiRequestException("Invalid Message length, must be 250 letters below.");
                              }
                              req.setMessage(entry.getValue());
                         }
                    }

                    excludedFiles.stream().forEach(e -> {

                    });

                    req.setRequestDate(formattedDate);
                    req.setRequestStatus("Pending");
                    req.setReleaseDate("");
                    req.setManageBy("");

                    if (findDocumentByTitle(document).isPresent()) {
                         if (documentRepository.findByTitle(document).isPresent()) {
                              req.setRequestDocument(documentRepository.findByTitle(document).get());
                         }
                    }

                    for (MultipartFile filex : uploadedFiles.get()) {

                         if (!excludedFiles.contains(filex.getOriginalFilename())) {
                              UserFiles userFiles = new UserFiles();
                              userFiles.setData(filex.getBytes());
                              userFiles.setName(filex.getOriginalFilename());
                              userFiles.setSize(globalService.formatFileUploadSize(filex.getSize()));
                              userFiles.setDateUploaded(formattedDate);
                              userFiles.setStatus("Pending");
                              userFiles.setFilePurpose("requirement");

                              userFiles.setUploadedBy(user);
                              userFiles.setRequestWith(req);
                              saveFiles(userFiles);

                         }

                    }
                    studentRepository.save(req);
                    return new ResponseEntity<>("Request Submited.", HttpStatus.OK);
               } else {
                    return new ResponseEntity<>("Required Informations, cannot be empty!", HttpStatus.BAD_REQUEST);
               }

          } catch (Exception ex) {
               ex.printStackTrace();
               return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
          }

     }

     @Override
     public Optional<Documents> findDocumentByTitle(String title) {
          Optional<Documents> documentTitleOptional = documentRepository.findByTitle(title);
          if (documentTitleOptional.isPresent()) {
               return documentTitleOptional;
          }
          return null;

     }

     @Override
     public List<StudentRequest> displayAllRequestByStudent(Users user) {
          List<StudentRequest> studentRequest = studentRepository.findAllByRequestBy(user);
          if (studentRequest.size() > -1) {
               return studentRequest;
          }
          return null;
     }

     @Override
     public List<StudentRequest> displayAllRequestByStudentAndRequestId(Users user, long requestId) {
          List<StudentRequest> studentRequest = studentRepository.findAllByRequestByAndRequestId(user, requestId);
          if (studentRequest.size() > -1) {
               return studentRequest;
          }
          return null;
     }

     @Override
     public Optional<Users> displayAllRequestByStudent(String username) {
          Optional<Users> userIdOptional = usersRepository.findUserIdByUsername(username);
          if (userIdOptional.isPresent()) {
               return userIdOptional;
          }
          return null;
     }

     @Override
     public boolean saveFiles(UserFiles files) {

          if (files != null) {

               fileRepository.save(files);
               return true;
          }
          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {

          return fileRepository.findAll();
     }

     @Override
     public Optional<UserFiles> getFileById(String id) {
          String stringValue = id.toString();
          UUID uuidValue = UUID.fromString(stringValue);
          Optional<UserFiles> fileOptional = fileRepository.findById(uuidValue);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return null;
     }

     @Override
     public Boolean deleteFile(long id) {

          return null;
     }

     @Override
     public List<UserFiles> getFilesByRequestWith(StudentRequest sr) {

          List<UserFiles> userFiles = fileRepository.findAllByRequestWith(sr);

          if (!userFiles.isEmpty()) {
               return userFiles;
          }
          return null;
     }

     @Override
     public Optional<StudentRequest> findRequestById(Long requestId) {
          Optional<StudentRequest> findReq = studentRepository.findById(requestId);
          if (findReq.isPresent()) {
               return findReq;
          }
          return Optional.empty();
     }

     @Override
     public ResponseEntity<Object> fetchRequestInformationToModals(String username,
               Long requestId) {
          Users user = displayAllRequestByStudent(username).get();

          List<StudentRequest_Dto> studentRequestCompress = new ArrayList<>();

          // get request in database
          List<StudentRequest> stOptional = displayAllRequestByStudentAndRequestId(user, requestId);
          // passtothe student request
          StudentRequest studentRequest = findRequestById(requestId).get();
          // getFiles by student request id
          List<UserFiles> ufOptional = getFilesByRequestWith(studentRequest);
          stOptional.stream().forEach(e -> {
               studentRequestCompress.add(new StudentRequest_Dto(e.getRequestId(), e.getRequestBy().getUserId(),
                         e.getRequestBy().getUsername(), e.getRequestBy().getName(),
                         e.getRequestBy().getType(), e.getYear(), e.getCourse(), e.getSemester(),
                         e.getRequestDocument().getTitle(), e.getMessage(), e.getReply(),
                         e.getRequestDate(), e.getRequestStatus(), e.getReleaseDate(), e.getManageBy()));

          });
          ufOptional.stream().forEach(files -> {
               String stringValue = files.getFileId().toString();
               UUID uuidValue = UUID.fromString(stringValue);
               studentRequestCompress.add(new StudentRequest_Dto(uuidValue, files.getData(), files.getName(),
                         files.getSize(), files.getStatus(), files.getDateUploaded(), files.getFilePurpose(),
                         files.getRequestWith().getRequestId(), files.getUploadedBy().getName()));
          });
          ServiceResponse<List<StudentRequest_Dto>> serviceResponse = new ServiceResponse<>("success",
                    studentRequestCompress);

          if (!stOptional.isEmpty()) {
               return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);

          } else {

               return new ResponseEntity<Object>("Failed To Retrieve Data.", HttpStatus.BAD_REQUEST);

          }
     }

     @Override
     public void student_showImageFiles(long id, HttpServletResponse response,
               Optional<Documents> dOptional) {

          dOptional = getFileDocumentById(id);
          try {
               response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf");
               response.getOutputStream().write(dOptional.get().getImage());
               response.getOutputStream().close();
          } catch (Exception e) {
               throw new ApiRequestException("Failed to load image Reason: " + e.getMessage());
          }
     }

     @Override
     public void student_DownloadFile(String id, Model model, HttpServletResponse response) {
          try {
               Optional<UserFiles> temp = getFileById(id);
               if (temp != null) {
                    UserFiles file = temp.get();
                    response.setContentType("application/octet-stream");
                    String headerKey = "Content-Disposition";
                    String headerValue = "attachment; filename = " + file.getName();
                    response.setHeader(headerKey, headerValue);
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(file.getData());
                    outputStream.close();
               }
          } catch (Exception e) {
               throw new ApiRequestException("Failed to download Reason: " + e.getMessage());
          }
     }

     @Override
     public Optional<Documents> getFileDocumentById(long id) {

          Optional<Documents> fileOptional = documentRepository.findById(id);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return Optional.empty();

     }

     @Override
     public List<UserFiles> getAllFilesByUser(long users) {

          try {
               if (users != -1) {
                    Users uploader = usersRepository.findByuserId(users).get();
                    List<UserFiles> studentFiles = fileRepository.findAllByUploadedBy(uploader);
                    if (!studentFiles.isEmpty()) {
                         return studentFiles;
                    } else {
                         return new ArrayList<>();
                    }

               }

               return null;

          } catch (Exception e) {
               return new ArrayList<>();
          }
     }

     @Override
     public ResponseEntity<Object> updateFileRequirement(Optional<MultipartFile> file, Map<String, String> params) {
          try {

               if (file.get().getSize() != 0) {

                    for (Map.Entry<String, String> entry : params.entrySet()) {

                         if (entry.getKey().equals("fileId")) {
                              String stringValue = entry.getValue().toString();
                              UUID uuidValue = UUID.fromString(stringValue);
                              UserFiles userFiles = fileRepository.findById(uuidValue).get();
                              userFiles.setFileId(uuidValue);
                              userFiles.setData(file.get().getBytes());
                              userFiles.setName(file.get().getOriginalFilename());
                              userFiles.setSize(globalService.formatFileUploadSize(file.get().getSize()));
                              fileRepository.save(userFiles);
                         }
                    }

                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
               return new ResponseEntity<>("Please Select File!", HttpStatus.BAD_REQUEST);
          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }
     }

     @Override
     public ResponseEntity<Object> updateInformationRequirement(long requestId, Map<String, String> params) {
          try {

               if (params.size() != 0) {
                    StudentRequest studentRequest = findRequestById(requestId).get();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                         if (entry.getKey().equals("year")) {
                              studentRequest.setYear(entry.getValue());
                         } else if (entry.getKey().equals("course")) {
                              studentRequest.setCourse(entry.getValue());
                         } else if (entry.getKey().equals("semester")) {
                              studentRequest.setSemester(entry.getValue());
                         } else if (entry.getKey().equals("message")) {
                              studentRequest.setMessage(entry.getValue());
                         }
                    }
                    studentRepository.save(studentRequest);

                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
               return new ResponseEntity<>("Invalid Informations", HttpStatus.BAD_REQUEST);
          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }
     }

     @Override
     public ResponseEntity<Object> resubmitRequests(String status, long userId, long requestId) {
          try {

               Users user = usersRepository.findByuserId(userId).get();

               StudentRequest studentRequest = studentRepository.findOneByRequestByAndRequestId(user, requestId).get();

               if (user != null && studentRequest != null) {

                    studentRepository.studentRequestsResubmit(status, studentRequest.getRequestId());
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
               return new ResponseEntity<>("Invalid Information, Please Try Again!", HttpStatus.BAD_REQUEST);
          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

}
