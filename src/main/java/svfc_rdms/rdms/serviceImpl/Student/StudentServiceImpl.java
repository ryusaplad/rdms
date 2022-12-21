package svfc_rdms.rdms.serviceImpl.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                                                  req.getMessage(), req.getRequestBy().getName(), req.getRequestDate(),
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

               LocalDate dateNow = LocalDate.now();

               if (uploadedFiles.isPresent() || !requestId.isEmpty() || !document.isEmpty() || params.size() > 0) {
                    StudentRequest req = new StudentRequest();
                    Users user = new Users();

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                         System.out.println("Key:" + entry.getKey() + ":" + entry.getValue());
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
                    System.out.println("Excluded Files");
                    excludedFiles.stream().forEach(e -> {
                         System.out.println(e);

                    });

                    req.setRequestDate("2022");
                    req.setRequestStatus("Pending");
                    req.setReleaseDate("2022");
                    req.setManageBy("admin");

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
                              userFiles.setSize(formatFileUploadSize(filex.getSize()));
                              userFiles.setDateUploaded(dateNow.toString());
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
     public Optional<UserFiles> getFileById(long id) {

          Optional<UserFiles> fileOptional = fileRepository.findById(id);
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
                         e.getRequestDocument().getTitle(), e.getMessage(),
                         e.getRequestDate(), e.getRequestStatus(), e.getReleaseDate(), e.getManageBy()));

          });
          ufOptional.stream().forEach(files -> {
               studentRequestCompress.add(new StudentRequest_Dto(files.getFileId(), files.getData(), files.getName(),
                         files.getSize(), files.getStatus(), files.getDateUploaded(), files.getFilePurpose(),
                         files.getRequestWith().getRequestId()));
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
     public String formatFileUploadSize(long size) {

          final long kilo = 1024;
          final long mega = kilo * kilo;
          final long giga = mega * kilo;
          final long tera = giga * kilo;

          double kb = size / kilo;
          double mb = kb / kilo;
          double gb = mb / kilo;
          double tb = gb / kilo;
          String formatedSize = "";
          if (size < kilo) {
               formatedSize = size + " Bytes";
          } else if (size >= kilo && size < mega) {
               formatedSize = String.format("%.2f", kb) + " KB";
          } else if (size >= mega && size < giga) {
               formatedSize = String.format("%.2f", mb) + " MB";
          } else if (size >= giga && size < tera) {
               formatedSize = String.format("%.2f", gb) + " GB";
          } else if (size >= tera) {
               formatedSize = String.format("%.2f", tb) + " TB";
          }
          return formatedSize;

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
     public void student_DownloadFile(long id, Model model, HttpServletResponse response) {
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

}
