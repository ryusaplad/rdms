package svfc_rdms.rdms.serviceImpl.Registrar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.ExceptionHandler.UserNotFoundException;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Registrar.Registrar_RequestService;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;

@Service
public class Reg_RequestServiceImpl implements Registrar_RequestService, FileService {

     @Autowired
     private StudentRepository studentRepository;

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsServiceImpl;

     @Autowired
     private NotificationServiceImpl notificationService;


     @Override
     public String displayAllFilesByUserId(HttpSession session, Model model) {
          model.addAttribute("page", "myfiles");
          model.addAttribute("pageTitle", "My Files");
          Users user = usersRepository.findUserIdByUsername(session.getAttribute("username").toString()).get();
          if (user.getUserId() != -1) {

               List<UserFiles> getAllFiles = getAllFilesByUser(user.getUserId());
               List<UserFiles_Dto> userFiles = new ArrayList<>();

               if (getAllFiles == null) {
                    model.addAttribute("files", userFiles);
                    return "/registrar/reg";
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

               return "/registrar/reg";

          }

          return null;

     }

     @Override
     public ResponseEntity<String> changeStatusAndManageByAndMessageOfRequests(String status, String message,
               long userId, long requestId, HttpSession session, HttpServletRequest request) {
          Users user = usersRepository.findByuserId(userId).get();
          Optional<StudentRequest> studRequest = studentRepository.findOneByRequestByAndRequestId(user, requestId);

          if (studRequest.isPresent()) {
               StudentRequest studentRequest = studRequest.get();
               if (message.equals("N/A")) {
                    message = studentRequest.getReply();
               } else if (studentRequest.getReply() != null) {
                    if (studentRequest.getReply().length() > 0) {
                         message = studentRequest.getReply() + "," + message;
                    }

               } else if (message == null || message.isEmpty()) {
                    message = "";
               }
               if (!status.isEmpty() || !status.isBlank() || session.getAttribute("name") != null) {
                    String manageBy = session.getAttribute("name").toString();
                    String manageByFromDatabase = globalService.removeDuplicateInManageBy(studentRequest.getManageBy());

                    if (!manageByFromDatabase.trim().isEmpty()) {
                         manageBy = globalService
                                   .removeDuplicateInManageBy(studentRequest.getManageBy() + "," + manageBy);
                    }
                    String title = "Request Status", notifMessage = "";
                    String documentName = studentRequest.getRequestDocument().getTitle();
                  
                    if (status.equals("On-Going")) {

                         notifMessage = "Hello " + user.getName() + ", the " + documentName
                                   + " request is currently ongoing, please go to the 'My Request' to view the details.";
                      
                    } else if (status.equals("Rejected")) {

                         notifMessage = "Hello " + user.getName() + ", unfortunately your request for " + documentName
                                   + " has been rejected. Please review the request provided and resubmit the request if needed.";
                         
                    }

                    String messageType = "requesting_notification";
                    String dateAndTime = globalService.formattedDate();

                    if (user != null && studentRequest != null) {
                         String text = notifMessage + "Please check 'My Requests' for further information.";

                         

                         if (notificationService.sendStudentNotification(title, notifMessage, messageType, dateAndTime,
                                   false,
                                   user, session, request)) {

                              studentRepository.changeStatusAndManagebyAndMessageOfRequests(status,
                                        manageBy,
                                        message, studentRequest.getRequestId());

                              String date = LocalDateTime.now().toString();
                              String logMessage = "Registrar changed the request status of " + user.getName() + " to "
                                        + status + ". User: " + manageBy + ".";
                              globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
                              globalService.sendTopic("/topic/totals", "OK");
                              globalService.sendTopic("/topic/student_request/recieved", "OK");

                              
                              return new ResponseEntity<>("Success", HttpStatus.OK);

                         } else {
                              String date = LocalDateTime.now().toString();
                              String logMessage = "Failed to change the student request status. System Message: Notification failed to send along with the status change of the message request.";
                              globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);

                              throw new ApiRequestException(
                                        "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");

                         }
                         

                    }

               }
          }
          String date = LocalDateTime.now().toString();
          String logMessage = " Registrar (" + session.getAttribute("name") + ":" + session.getAttribute("username")
                    + ")failed to change the student request status \nSystem Message: Student not exist";
          globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);

          throw new ApiRequestException(
                    "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");

     }

     @Override
     public ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
               Optional<MultipartFile[]> files, Map<String, String> params,
               HttpSession session, HttpServletRequest request) {

          try {

               if (files.isEmpty()) {
                    throw new ApiRequestException("Please upload file.");
               }
               Optional<Users> user_op = usersRepository.findByuserId(userId);

               if (user_op.isPresent()) {
                    Users user = usersRepository.findByuserId(userId).get();
                    Optional<StudentRequest> studRequest = studentRepository.findOneByRequestByAndRequestId(user,
                              requestId);
                    StudentRequest studentRequest = studRequest.get();

                    studentRequest.setReleaseDate(globalService.formattedDate());
                    studentRequest.setRequestStatus("Approved");
                    List<String> excludedFiles = new ArrayList<>();

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                         if (entry.getKey().contains("excluded")) {
                              excludedFiles.add(entry.getValue());
                         }

                    }

                    String documentName = studentRequest.getRequestDocument().getTitle();
                    String title = "Requested " + documentName + " has been completed.";
                    String notifMessage = "Hello " + studentRequest.getRequestBy().getName() + " your requested "
                              + documentName + " has been approved, please go to the 'My Request' to view the details.";
                    String messageType = "requested_completed";
                    String dateAndTime = globalService.formattedDate();
                    if (notificationService.sendStudentNotification(title, notifMessage, messageType, dateAndTime,
                              false,
                              user, session, request)) {
                         Users manageBy = usersRepository.findByUsername(session.getAttribute("username").toString())
                                   .get();

                         for (MultipartFile filex : files.get()) {

                              if (!excludedFiles.contains(filex.getOriginalFilename())) {

                                   UserFiles userFiles = new UserFiles();
                                   userFiles.setData(filex.getBytes());
                                   userFiles.setName(filex.getOriginalFilename());
                                   userFiles.setSize(globalService.formatFileUploadSize(filex.getSize()));
                                   userFiles.setDateUploaded(globalService.formattedDate());
                                   userFiles.setStatus("Approved");
                                   userFiles.setFilePurpose("dfs");
                                   userFiles.setUploadedBy(manageBy);
                                   userFiles.setRequestWith(studentRequest);
                                   fileRepository.save(userFiles);

                              }
                         }
                         String reqStatus = studentRequest.getRequestStatus().toLowerCase();
                         if (!reqStatus.contains("approved") || !reqStatus.contains("pending")
                                   || !reqStatus.contains("on-going")) {
                              studentRepository.save(studentRequest);
                              String date = LocalDateTime.now().toString();
                              String logMessage = "Registrar Finalized "
                                        + user.getName() + " User: " + manageBy.getName() + ":" + manageBy.getUsername()
                                        + "Finalized/Completed the request of " + user.getName();

                              globalService.sendTopic("/topic/totals", "OK");
                              globalService.sendTopic("/topic/student_request/recieved", "OK");
                              globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);

                              return new ResponseEntity<>("Success", HttpStatus.OK);
                         } else {
                              return new ResponseEntity<>("Request is already completed.", HttpStatus.OK);
                         }

                    } else {
                         return new ResponseEntity<>("Failed to save the notification.", HttpStatus.OK);
                    }
               }

          } catch (Exception ex) {

               String date = LocalDateTime.now().toString();
               String logMessage = "Registrar (" + session.getAttribute("name") + ":" + session.getAttribute("username")
                         + ")failed to finalized request\nSystem Message: " + ex.getMessage();
               globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
               throw new ApiRequestException(ex.getMessage());
          }
          return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
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
     public List<UserFiles> getAllFilesByUser(long userId) {

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
               throw new UserNotFoundException("User with id " + userId + " not found", e);
          }
     }

     @Override
     public Boolean deleteFile(UUID id) {
          // TODO Auto-generated method stub
          return null;
     }

}
