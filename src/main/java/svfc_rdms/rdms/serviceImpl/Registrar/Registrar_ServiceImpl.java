package svfc_rdms.rdms.serviceImpl.Registrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.RegistrarRequest_DTO;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegsRequestRepository;
import svfc_rdms.rdms.service.Registrar.Registrar_SelfRequest_Service;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;

@Service
public class Registrar_ServiceImpl implements Registrar_SelfRequest_Service {

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private RegsRequestRepository regsRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private NotificationServiceImpl notificationService;

     @Override
     public ResponseEntity<String> sendRequestToTeacher(long userId, HttpSession session, Map<String, String> params) {
          try {

               String requestedDate = globalService.formattedDate();

               String username = Optional.ofNullable(session.getAttribute("username")).orElse("").toString();
               Users teacher = null;
               Users registrar = null;
               Optional<Users> Optional_teacher = usersRepository.findByuserId(userId);

               Optional<Users> Optional_Registrar = usersRepository.findByUsername(username);
               if (Optional_Registrar.isPresent() && Optional_teacher.isPresent()) {
                    registrar = Optional_Registrar.get();
                    teacher = Optional_teacher.get();

                    if (!params.isEmpty()) {
                         RegistrarRequest registrarRequest = new RegistrarRequest();
                         registrarRequest.setRequestBy(registrar);
                         registrarRequest.setRequestTo(teacher);
                         String from = params.get("from");
                         String to = params.get("to");
                         String message = params.get("message");
                         String title = params.get("title");

                         if (from.isBlank() || to.isBlank()) {
                              return new ResponseEntity<>("Message sender/reciever cannot be empty. Please try again!.",
                                        HttpStatus.BAD_REQUEST);
                         }

                         if (title.isBlank()) {
                              return new ResponseEntity<>("Title cannot be empty. Please add a requests title.",
                                        HttpStatus.BAD_REQUEST);
                         } else if (title.length() > 50) {
                              return new ResponseEntity<>("Title Length Invalid. Please try again!.",
                                        HttpStatus.BAD_REQUEST);
                         }
                         if (message.isBlank()) {
                              return new ResponseEntity<>("Message cannot be empty. Please add a message.",
                                        HttpStatus.BAD_REQUEST);
                         } else if (message.length() > 10000) {
                              return new ResponseEntity<>("Message Length Invalid. Please try again!.",
                                        HttpStatus.BAD_REQUEST);
                         }
                         if (!from.isBlank() && !to.isBlank() && !message.isBlank() && !title.isBlank()
                                   && textSizeChecker(message, 10000) && textSizeChecker(title, 50)) {

                              String notifMessage = registrar.getName()
                                        + " is requesting document for the specific student, please go to the 'Request View' for more details.";
                              String messageType = "requesting_document";
                              String date = globalService.formattedDate();
                              boolean notifStatus = false;
                              registrarRequest.setRequestBy(registrar);
                              registrarRequest.setRequestTo(teacher);
                              registrarRequest.setRequestTitle(title);
                              registrarRequest.setRequestMessage(message);
                              registrarRequest.setRequestStatus("pending");
                              registrarRequest.setRequestDate(requestedDate);
                              if (notificationService.sendNotification(title, notifMessage, messageType, date,
                                        notifStatus, registrar, teacher)) {
                                   regsRepository.save(registrarRequest);
                                   return new ResponseEntity<>("Success", HttpStatus.OK);
                              }

                         }

                    }
               } else {
                    throw new ApiRequestException(
                              "Failed to sent request, Please Try Again!. Please contact the administrator for further assistance.");
               }

          } catch (Exception e) {

               throw new ApiRequestException(
                         "Failed to sent request, Please Try Again!. Please contact the administrator for further assistance.");
          }
          throw new ApiRequestException(
                    "Failed to sent request, Please Try Again!. Please contact the administrator for further assistance.");
     }

     @Override
     public Optional<RegistrarRequest> getRegistrarRequest(long requestsId) {

          Optional<RegistrarRequest> registrarRequests = regsRepository.findOneByRequestId(requestsId);
          if (requestsId > -1 && !registrarRequests.isEmpty()) {
               return registrarRequests;
          }
          return Optional.empty();
     }

     @Override
     public String displayAllRequests(HttpSession session, Model model) {
          List<RegistrarRequest_DTO> filteredRequests = new ArrayList<>();
          try {

               String username = session.getAttribute("username").toString();
               if (!username.isBlank() || !username.isEmpty() || username.length() > -1) {
                    Optional<Users> user = usersRepository.findByUsername(username);

                    if (user.isPresent()) {
                         List<RegistrarRequest> regRequests = regsRepository.findAllByRequestBy(user.get());
                         if (regRequests != null) {
                              regRequests.forEach(request -> {

                                   filteredRequests.add(
                                             new RegistrarRequest_DTO(request.getRequestId(), request.getRequestTitle(),
                                                       request.getRequestMessage(), request.getTeacherMessage(),
                                                       request.getRequestBy().getName(),
                                                       request.getRequestTo().getName(), request.getRequestDate(),
                                                       request.getDateOfUpdate(),
                                                       request.getRequestStatus()));

                              });

                              model.addAttribute("requests_list", filteredRequests);
                              return "/registrar/registrar-requests";
                         }
                    }

               }
          } catch (Exception e) {
               return "/registrar/reg";
          }
          model.addAttribute("requests_list", filteredRequests);
          return "/registrar/registrar-requests";
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

     private boolean textSizeChecker(String text, int max) {
          if (text.length() > max) {
               return false;
          }

          return true;
     }

}
