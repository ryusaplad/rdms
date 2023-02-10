package svfc_rdms.rdms.serviceImpl.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.ExceptionHandler.FileNotFoundException;
import svfc_rdms.rdms.ExceptionHandler.UserNotFoundException;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Student.Student_RequirementService;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Service
public class Student_RequirementServiceImpl implements Student_RequirementService, FileService {
     @Autowired
     private StudentRepository studentRepository;

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Override
     public List<UserFiles> getAllFilesByUser(long userId) throws FileNotFoundException {

          try {
               if (userId != -1) {
                    Users uploader = usersRepository.findByuserId(userId).get();
                    List<UserFiles> studentFiles = fileRepository.findAllByUploadedBy(uploader);
                    if (!studentFiles.isEmpty()) {
                         return studentFiles;
                    }

               }

               return null;

          } catch (IllegalArgumentException e) {
               throw new UserNotFoundException("Can't display files because, Reason: User " + userId + " not found.",
                         e);
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
               StudentRequest studentRequest = null;
               if (params.size() != 0) {
                    Optional<StudentRequest> optional_StudentRequest = studentRepository.findById(requestId);
                    if (optional_StudentRequest.isPresent()) {
                         studentRequest = optional_StudentRequest.get();
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

     @Override
     public String displayAllFilesByUserId(HttpSession session, Model model) {

          Users user = usersRepository.findUserIdByUsername(session.getAttribute("username").toString()).get();
          if (user.getUserId() != -1) {

               List<UserFiles> getAllFiles = getAllFilesByUser(user.getUserId());
               List<UserFiles_Dto> userFiles = new ArrayList<>();

               if (getAllFiles == null) {
                    model.addAttribute("files", userFiles);
                    return "/student/documents-list";
               }
               getAllFiles.stream().forEach(file -> {
                    String stringValue = file.getFileId().toString();
                    UUID uuidValue = UUID.fromString(stringValue);
                    String uploadedBy = file.getUploadedBy().getUsername() + ":"
                              + file.getUploadedBy().getName();
                    userFiles.add(new UserFiles_Dto(
                              uuidValue, file.getName(), file.getSize(),
                              file.getStatus(),
                              file.getDateUploaded(), file.getFilePurpose(), uploadedBy));
               });
               model.addAttribute("files", userFiles);
               return "/student/documents-list";

          }

          return null;

     }

     @Override
     public boolean saveFiles(UserFiles files) {
          // TODO Auto-generated method stub
          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {
          // TODO Auto-generated method stub
          return null;
     }

     @Override
     public Optional<UserFiles> getFileById(String id) {
          // TODO Auto-generated method stub
          return Optional.empty();
     }

     @Override
     public List<UserFiles> getFilesByRequestWith(StudentRequest sr) {
          // TODO Auto-generated method stub
          return null;
     }

     @Override
     public Boolean deleteFile(long id) {
          // TODO Auto-generated method stub
          return null;
     }

}
