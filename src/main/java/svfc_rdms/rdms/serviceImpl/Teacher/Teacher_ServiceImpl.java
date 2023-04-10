package svfc_rdms.rdms.serviceImpl.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.ExceptionHandler.UserNotFoundException;
import svfc_rdms.rdms.dto.RegistrarRequest_DTO;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegsRequestRepository;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Teacher.Teacher_Service;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;

@Service
public class Teacher_ServiceImpl implements Teacher_Service, FileService {

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private RegsRequestRepository regsRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsServiceImpl;

     @Autowired
     private NotificationServiceImpl notificationService;


     @Override
     public Optional<RegistrarRequest> getRegistrarRequest(long requestsId) {

          
          Optional<RegistrarRequest> registrarRequests = regsRepository.findOneByRequestId(requestsId);
          if (requestsId > -1 && !registrarRequests.isEmpty()) {
               return registrarRequests;
          }
          return Optional.empty();
     }

     @Override
     public ResponseEntity<Object> viewRegistrarRequests(long requestsId) {
          Optional<RegistrarRequest> req = getRegistrarRequest(requestsId);
          List<RegistrarRequest_DTO> registrarDtoCompressor = new ArrayList<>();

          if (req.isPresent()) {
               List<UserFiles> regRequestFiles = fileRepository.findAllByRegRequestsWith(req.get());
               RegistrarRequest regReq = req.get();
               RegistrarRequest_DTO regDto = new RegistrarRequest_DTO(
                         regReq.getRequestId(),
                         regReq.getRequestTitle(), regReq.getRequestMessage(), regReq.getTeacherMessage(),
                         regReq.getRequestBy().getName(),
                         regReq.getRequestTo().getName(), regReq.getRequestDate(),
                         regReq.getDateOfUpdate(), regReq.getRequestStatus());
               registrarDtoCompressor.add(regDto);

               if (regRequestFiles != null) {
                    regRequestFiles.forEach(userFiles -> {

                         registrarDtoCompressor.add(new RegistrarRequest_DTO(
                                   userFiles.getFileId(), userFiles.getName(),
                                   userFiles.getSize(),
                                   userFiles.getStatus(), userFiles.getDateUploaded(),
                                   userFiles.getFilePurpose(),
                                   userFiles.getUploadedBy().getName()));
                    });

               }
               return new ResponseEntity<Object>(registrarDtoCompressor, HttpStatus.OK);

          } else {
               throw new ApiRequestException("No data found for the given requests ID");
          }
     }

     @Override
     public ResponseEntity<String> sendRequestToRegistrar(long requestsId, HttpSession session,
               Optional<MultipartFile[]> files,
               Map<String, String> params,HttpServletRequest request) {
          List<String> excludedFiles = new ArrayList<>();
          Optional<RegistrarRequest> optionalRegRequest = getRegistrarRequest(requestsId);

          String username = (String) session.getAttribute("username");
          String message = "";

          String title = "";
          String notifMessage = "";
          String messageType = "";
          String date = globalService.formattedDate();
          boolean notifStatus = false;

          boolean messageAvailability = false;
          boolean filesAvailability = false;
          if (username == null || username.isEmpty()) {
               throw new ApiRequestException("Can't send requests. Please contact the administrator!");
          }

          Optional<Users> optionalUser = usersRepository.findByUsername(username);

          if (optionalRegRequest.isPresent() && optionalUser.isPresent()) {
               try {
                    Users uploadedBy = optionalUser.get();
                    RegistrarRequest registrarRequest = optionalRegRequest.get();

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                         String key = entry.getKey().replace(" ", "");
                         if (key.contains("excluded")) {
                              excludedFiles.add(entry.getValue());
                         } else if (key.contains("message")) {
                              message = entry.getValue();
                              registrarRequest.setTeacherMessage(message);
                              messageAvailability = true;
                         }
                    }

                    if (files.isPresent()) {
                         for (MultipartFile file : files.get()) {
                              if (!excludedFiles.contains(file.getOriginalFilename())) {
                                   UserFiles userFile = new UserFiles();
                                   userFile.setData(file.getBytes());
                                   userFile.setName(file.getOriginalFilename());
                                   userFile.setSize(globalService.formatFileUploadSize(file.getSize()));
                                   userFile.setDateUploaded(globalService.formattedDate());
                                   userFile.setStatus("Approved");
                                   userFile.setFilePurpose("student-record");
                                   userFile.setUploadedBy(uploadedBy);
                                   userFile.setRegRequestsWith(registrarRequest);
                                   fileRepository.save(userFile);
                              }
                         }
                         filesAvailability = true;
                    } else {
                         title = "Student has no record.";
                         notifMessage = "This student has no record available, please go to the 'Registrar Request for more details.'";
                         messageType = "Request_No_Record";
                         registrarRequest.setRequestStatus("norecord");
                    }

                    if (messageAvailability && filesAvailability) {
                         title = "Request Completed";
                         notifMessage = "This student record has been found and sent., please go to the 'Registrar Request for more details.'";
                         messageType = "Request Completed";
                         registrarRequest.setRequestStatus("completed");
                    } else if (messageAvailability) {
                         title = "Student has no record";
                         notifMessage = "This student has no record available, please go to the 'Registrar Request for more details.'";
                         messageType = "Request_No_Record";
                         registrarRequest.setRequestStatus("messageonly");
                    } else if (filesAvailability) {
                         title = "Student has no record";
                         notifMessage = "This student has no record available, please go to the 'Registrar Request for more details.'";
                         messageType = "Request_No_Record";
                         registrarRequest.setRequestStatus("recordonly");
                    }

                    if (notificationService.sendNotification(title, notifMessage, messageType, date, notifStatus,
                              uploadedBy,
                              registrarRequest.getRequestBy(), session,request)) {
                         regsRepository.save(registrarRequest);

                      
                         String logMessage = "Teacher replied "+registrarRequest.getRequestBy().getName()+ " User: " + registrarRequest.getRequestTo().getName()
                                   + " re-sent requested data";
                         globalService.sendTopic("/topic/registrar/requests", "OK");
                         globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session,request);

                         return new ResponseEntity<>("Success", HttpStatus.OK);
                    }

               } catch (Exception e) {
                    throw new ApiRequestException("Can't send requests. Please contact the administrator!");
               }
          }
          throw new ApiRequestException("Can't send requests. Please try again later!");
     }

     @Override
     public ResponseEntity<Object> displayAllRequests(HttpSession session) {
          List<RegistrarRequest_DTO> filteredRequests = new ArrayList<>();
          try {

               Sort descendingSort = Sort.by("requestId").descending();
               if (session.getAttribute("username") != null || !session.getAttribute("username").toString().isEmpty()) {
                    String username = session.getAttribute("username").toString();
                    Optional<Users> teacher = usersRepository.findByUsername(username);

                    if (teacher.isPresent()) {
                         List<RegistrarRequest> regRequests = regsRepository.findAllByRequestTo(teacher.get(),descendingSort);
                         if (regRequests != null) {
                              regRequests.forEach(request -> {

                                   filteredRequests
                                             .add(new RegistrarRequest_DTO(request.getRequestId(),
                                                       request.getRequestTitle(),
                                                       request.getRequestMessage(), request.getTeacherMessage(),
                                                       request.getRequestBy().getName(),
                                                       request.getRequestTo().getName(), request.getRequestDate(),
                                                       request.getDateOfUpdate(),
                                                       request.getRequestStatus()));

                              });

                            return new ResponseEntity<>(filteredRequests, HttpStatus.OK);
                         }
                    }

               }
          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }
          return new ResponseEntity<>("No Data Available", HttpStatus.OK);
     }

     @Override
     public boolean saveFiles(UserFiles files) {

          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {

          return null;
     }

     @Override
     public Optional<UserFiles> getFileById(String id) {

          return Optional.empty();
     }

     @Override
     public List<UserFiles> getFilesByRequestWith(StudentRequest sr) {

          return null;
     }

     @Override
     public List<UserFiles> getAllFilesByUser(long userId) {

          try {
               if (userId != -1) {
                    Users uploader = usersRepository.findByuserId(userId).get();
                    List<UserFiles> teacherFiles = fileRepository.findAllByUploadedBy(uploader);
                    if (!teacherFiles.isEmpty()) {
                         return teacherFiles;
                    }

               }

               return null;

          } catch (IllegalArgumentException e) {
               throw new UserNotFoundException("User with id " + userId + " not found", e);
          }
     }

     @Override
     public Boolean deleteFile(UUID id) {

          return null;

     }

     @Override
     public String displayAllFilesByUserId(HttpSession session, Model model) {
          Users user = usersRepository.findUserIdByUsername(session.getAttribute("username").toString()).get();
          if (user.getUserId() != -1) {

               List<UserFiles> getAllFiles = getAllFilesByUser(user.getUserId());
               List<UserFiles_Dto> userFiles = new ArrayList<>();

               if (getAllFiles == null) {
                    model.addAttribute("pageTitle", "My Files");
                    model.addAttribute("page", "myfiles");
                    model.addAttribute("files", userFiles);
                    System.out.println("Files got fire but no item");
                    return "/teacher/teach";
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
               model.addAttribute("pageTitle", "My Files");
               model.addAttribute("page", "myfiles");
               return "/teacher/teach";
              
          }

          return null;
     }

}
