package svfc_rdms.rdms.controller.RestControllers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.Enums.ValidAccounts;
import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.NotificationRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.repository.Teacher.TeacherRepository;
import svfc_rdms.rdms.serviceImpl.Global.Admin_Registrar_ManageAccountServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.AllAccountServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;

@RestController
public class AllAccount_RestController {

     @Autowired
     private AllAccountServiceImpl accountServiceImpl;

     @Autowired
     private Admin_Registrar_ManageAccountServiceImpl admin_RegistrarAccountServiceImpl;

     @Autowired
     private NotificationServiceImpl notificationServiceImpl;

     @Autowired
     private NotificationRepository notificationRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private StudentRepository studentRepository;
     @Autowired
     private FileRepository fileRepository;
     @Autowired
     private UsersRepository usersRepository;
     @Autowired
     private TeacherRepository teacherRepository;

     @GetMapping("/{userType}/dashboard/totals")
     public ResponseEntity<Object> loadAllTotal(@PathVariable("userType") String userType, HttpServletResponse response,
               HttpServletRequest request,
               HttpSession session) {

          try {

               String username = "";
               if ((username = session.getAttribute("username").toString()) != null) {

                    ValidAccounts[] validAccountType = ValidAccounts.values();

                    for (ValidAccounts validAcc : validAccountType) {
                         if (String.valueOf(validAcc).toLowerCase()
                                   .contains(userType.toLowerCase())) {
                              userType = validAcc.toString().toLowerCase();
                              break;
                         }
                    }
                    if (globalService.validatePages(userType, response, session)) {
                         if (session.getAttribute("name") != null) {
                              String name = session.getAttribute("name").toString();
                              Optional<Users> user = usersRepository.findByName(name);
                              List<Long> totals = new ArrayList<>();

                              if (user.isPresent()) {

                                   if (user.get().getType().equalsIgnoreCase("student")) {
                                        long totalRequests = studentRepository.countByRequestBy(user.get());
                                        long totalApprovedRequests = studentRepository
                                                  .countByRequestByAndRequestStatus(user.get(), "Approved");
                                        long totalRejectedRequests = studentRepository
                                                  .countByRequestByAndRequestStatus(user.get(), "Rejected");
                                        long totalUploadedFiles = fileRepository.countByUploadedBy(user.get());

                                        totals.add(totalRequests);
                                        totals.add(totalApprovedRequests);
                                        totals.add(totalRejectedRequests);
                                        totals.add(totalUploadedFiles);
                                        return new ResponseEntity<>(totals, HttpStatus.OK);
                                   } else if (user.get().getType().equalsIgnoreCase("registrar")) {
                                        long totalStudentRequests = studentRepository.count();
                                        long totalTeacherRequests = teacherRepository.count();
                                        long totalPendingRequests = studentRepository
                                                  .countByRequestStatus("Pending");
                                        long totalApprovedRequests = studentRepository
                                                  .countByRequestStatus("Approved");
                                        long totalRejectedRequests = studentRepository
                                                  .countByRequestStatus("Rejected");
                                        long totalUploadedFiles = fileRepository.countByUploadedBy(user.get());

                                        totals.add(totalStudentRequests);
                                        totals.add(totalTeacherRequests);
                                        totals.add(totalPendingRequests);
                                        totals.add(totalApprovedRequests);
                                        totals.add(totalRejectedRequests);
                                        totals.add(totalUploadedFiles);
                                        return new ResponseEntity<>(totals, HttpStatus.OK);
                                   } else if (user.get().getType().equalsIgnoreCase("teacher")) {
                                        long totalRequests = teacherRepository.count();
                                        long totalPendingRequests = teacherRepository.totalRequestsByStatus("pending");

                                        long totalSentRequests_Completed = teacherRepository
                                                  .totalRequestsByStatus("completed");

                                        long totalSentRequests_NoRecord = teacherRepository
                                                  .totalRequestsByStatus("norecord");

                                        long totalSentRequests_MessageOnly = teacherRepository
                                                  .totalRequestsByStatus("messageonly");

                                        long totalSentRequests_RecordOnly = teacherRepository
                                                  .totalRequestsByStatus("recordonly");

                                        long totalUploadedFiles = fileRepository

                                                  .countByUploadedBy(user.get());
                                        long totalSentRequests = totalSentRequests_Completed
                                                  + totalSentRequests_MessageOnly + totalSentRequests_NoRecord
                                                  + totalSentRequests_RecordOnly;
                                        totals.add(totalRequests);
                                        totals.add(totalPendingRequests);
                                        totals.add(totalSentRequests);
                                        totals.add(totalUploadedFiles);
                                        return new ResponseEntity<>(totals, HttpStatus.OK);
                                   } else if (user.get().getType().equalsIgnoreCase("school_admin")) {
                                        long totalStudentRequests = studentRepository.count();
                                        long totalRegistrarRequests = teacherRepository.count();
                                        long totalPendingRequests = studentRepository
                                                  .countByRequestStatus("Pending");
                                        long totalApprovedRequests = studentRepository
                                                  .countByRequestStatus("Approved");
                                        long totalRejectedRequests = studentRepository
                                                  .countByRequestStatus("Rejected");

                                        long totalRegistrarAcc = usersRepository.totalUsers("Registrar",
                                                  "Active");
                                        long totalTeacherAcc = usersRepository.totalUsers("Teacher",
                                                  "Active");
                                        long totalStudentsAcc = usersRepository.totalUsers("Students",
                                                  "Active");

                                        long totalDelRegistarAcc = usersRepository.totalUsers("Students",
                                                  "Temporary");
                                        long totalDelTeacherAcc = usersRepository.totalUsers("Teacher",
                                                  "Temporary");
                                        long totalDelStudentsAcc = usersRepository.totalUsers("Students",
                                                  "Temporary");

                                        long totalGlobalFiles = fileRepository.count();

                                        totals.add(totalStudentRequests);
                                        totals.add(totalRegistrarRequests);
                                        totals.add(totalPendingRequests);
                                        totals.add(totalApprovedRequests);
                                        totals.add(totalRejectedRequests);

                                        totals.add(totalRegistrarAcc);
                                        totals.add(totalTeacherAcc);
                                        totals.add(totalStudentsAcc);

                                        totals.add(totalDelRegistarAcc);
                                        totals.add(totalDelTeacherAcc);
                                        totals.add(totalDelStudentsAcc);
                                        totals.add(totalGlobalFiles);
                                        return new ResponseEntity<>(totals, HttpStatus.OK);
                                   }

                              } else {
                                   return new ResponseEntity<>(
                                             "You are performing invalid action, Please try again later.",
                                             HttpStatus.BAD_REQUEST);
                              }

                         }

                    } else {
                         throw new ApiRequestException("You are performing invalid action, Please try again");
                    }

               } else {
                    throw new ApiRequestException("You are performing invalid action, Please try again");
               }

          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }
          throw new ApiRequestException("You are performing invalid action, Please try again");
     }

     @PostMapping("/change/account/password")
     public ResponseEntity<String> changePassword(@RequestParam("oldPass") String oldPassword,
               @RequestParam("newPass") String newPassword, @RequestParam("confirmPass") String confirmPassword,
               HttpSession session, HttpServletResponse response, HttpServletRequest request) {
          long userId = 0;
          String username = "";
          if ((username = session.getAttribute("username").toString()) != null) {
               Optional<Users> optionalUser = usersRepository.findByUsername(username);
               if (optionalUser.isPresent()) {
                    userId = optionalUser.get().getUserId();
               }
               return accountServiceImpl.changePassword(oldPassword, newPassword, userId, session, request);
          }
          throw new ApiRequestException("Invalid Action");

     }

     @PostMapping("/change/account/profile-picture")
     public ResponseEntity<String> changeProfilePicture(@RequestParam MultipartFile image, HttpSession session,
               HttpServletRequest request) {
          long userId = 0;
          String username = "";
          if ((username = session.getAttribute("username").toString()) != null) {
               Optional<Users> optionalUser = usersRepository.findByUsername(username);
               if (optionalUser.isPresent()) {
                    userId = optionalUser.get().getUserId();
               }
               return accountServiceImpl.changeProfilePicture(image, userId, session, request);
          }
          throw new ApiRequestException("Invalid Action");
     }

     @GetMapping("/session/{sessionKey}")
     public ResponseEntity<String> getSessionValue(@PathVariable("sessionKey") String sessionKey, HttpSession session) {
          return new ResponseEntity<>((String) session.getAttribute(sessionKey), HttpStatus.OK);
     }

     @GetMapping("/load/image")
     public ResponseEntity<Object> loadImage(@RequestParam("username") String username, HttpSession session) {
          Optional<Users> user = usersRepository.findByUsername(username);
          try {
               byte[] image = null;
               if (!username.isEmpty() || !username.isBlank()) {

                    if (user.isPresent()) {
                         image = user.get().getProfilePicture();

                         // if image is empty return the name and color code to use in the ajax call. to
                         // produce avatar using name and color code.
                         if (image[0] == 48 || image == null || image[0] == 0) {
                              String[] nameData = { user.get().getName(), user.get().getColorCode() };
                              return new ResponseEntity<>(nameData, HttpStatus.NOT_FOUND);
                         }

                         byte[] encodeBase64 = Base64.getEncoder().encode(image);
                         String base64Encoded = new String(encodeBase64, "UTF-8");
                         return new ResponseEntity<>(base64Encoded, HttpStatus.OK);
                    }
                    throw new ApiRequestException("User not found!, Please Try Again");
               }
               throw new ApiRequestException("Username not found!, Please Try Again");
          } catch (Exception e) {
               String[] nameData = { user.get().getName(), user.get().getColorCode() };
               return new ResponseEntity<>(nameData, HttpStatus.NOT_FOUND);
          }
     }

     @GetMapping("/load/user-profile-info")
     public ResponseEntity<Object> profileInfo(@RequestParam("username") String username, HttpSession session) {

          try {

               if (!username.isEmpty() || !username.isBlank()) {
                    Optional<Users> optionalUser = usersRepository.findByUsername(username);
                    if (optionalUser.isPresent()) {
                         // return only name and color code.
                         String[] nameData = { optionalUser.get().getName(), optionalUser.get().getColorCode() };
                         return new ResponseEntity<>(nameData, HttpStatus.OK);
                    }
                    throw new ApiRequestException("User not found!, Please Try Again");
               }
               throw new ApiRequestException("Username not found!, Please Try Again");
          } catch (Exception e) {
               throw new ApiRequestException("Profile Picture Data not found!, Please Try Again");
          }
     }

     @GetMapping("/{userType}/notification/{status}/{lowest}/{current}")
     public ResponseEntity<Object> loadNotification(@PathVariable("userType") String userType,
               @PathVariable("lowest") int lowestPage,
               @PathVariable("current") int totalPage, @PathVariable("status") boolean status, HttpSession session,
               HttpServletResponse response) {

          try {
               String username = "";
               if ((username = session.getAttribute("username").toString()) != null) {

                    ValidAccounts[] validAccountType = ValidAccounts.values();

                    for (ValidAccounts validAcc : validAccountType) {
                         if (String.valueOf(validAcc).toLowerCase()
                                   .contains(userType.toLowerCase())) {
                              userType = validAcc.toString().toLowerCase();
                              break;
                         }
                    }
                    if (globalService.validatePages(userType, response, session)) {
                         if (userType.equals("student")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchDasboardAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else if (userType.equals("registrar")) {

                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchDasboardAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else if (userType.equals("teacher")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchDasboardAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else if (userType.equals("school_admin")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchDasboardAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else {
                              throw new ApiRequestException("You are performing invalid action, Please try again");
                         }

                    } else {
                         throw new ApiRequestException("You are performing invalid action, Please try again");
                    }

               } else {
                    throw new ApiRequestException("You are performing invalid action, Please try again");
               }

          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

     @GetMapping("/{userType}/notification/{lowest}/{current}")
     public ResponseEntity<Object> loadNotification(@PathVariable("userType") String userType,
               @PathVariable("lowest") int lowestPage,
               @PathVariable("current") int totalPage, HttpSession session, HttpServletResponse response) {

          try {
               String username = "";
               if ((username = session.getAttribute("username").toString()) != null) {

                    ValidAccounts[] validAccountType = ValidAccounts.values();

                    for (ValidAccounts validAcc : validAccountType) {
                         if (String.valueOf(validAcc).toLowerCase()
                                   .contains(userType.toLowerCase())) {
                              userType = validAcc.toString().toLowerCase();
                              break;
                         }
                    }

                    if (globalService.validatePages(userType, response, session)) {
                         if (userType.equals("student")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.getAllNotificationsByUser(user.get(), userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else if (userType.equals("registrar")) {

                              Optional<Users> user = usersRepository.findByUsername(username);

                              return new ResponseEntity<>(
                                        notificationServiceImpl.getAllNotificationsByUser(user.get(), userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else if (userType.equals("teacher")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.getAllNotificationsByUser(user.get(), userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else if (userType.equals("school_admin")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.getAllNotificationsByUser(user.get(), userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else {
                              throw new ApiRequestException("You are performing invalid action, Please try again");
                         }

                    } else {
                         throw new ApiRequestException("You are performing invalid action, Please try agai 1n");
                    }

               } else {
                    throw new ApiRequestException("You are performing invalid action, Please try again");
               }

          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

     @GetMapping("/{userType}/notification-status/{status}/{notifId}")
     public ResponseEntity<String> updateNotificationStatus(@PathVariable("userType") String userType,
               @PathVariable("status") Boolean status,
               @PathVariable("notifId") Long notifId, HttpSession session, HttpServletResponse response) {
          Optional<Notifications> notification = notificationRepository.findById(notifId);

          String username = "";
          if ((username = session.getAttribute("username").toString()) != null) {

               ValidAccounts[] validAccountType = ValidAccounts.values();

               for (ValidAccounts validAcc : validAccountType) {
                    if (String.valueOf(validAcc).toLowerCase()
                              .contains(userType.toLowerCase())) {
                         userType = validAcc.toString().toLowerCase();
                         break;
                    }
               }
               if (globalService.validatePages(userType, response, session)) {
                    if (notification.isPresent()) {
                         Notifications notifData = notification.get();
                         notifData.setStatus(status);
                         notificationRepository.save(notifData);
                         return new ResponseEntity<>("success", HttpStatus.OK);
                    } else {
                         throw new ApiRequestException("You are performing invalid action, Please try again");
                    }

               }

          }
          throw new ApiRequestException("You are performing invalid action, Please try again");
     }

     @GetMapping("/{userType}/student-request-export/all")
     public ResponseEntity<String> exportStudentRequestToExcelCheck(@PathVariable("userType") String userType,
               HttpSession session,
               HttpServletResponse response, HttpServletRequest request) {
          List<StudentRequest> studRequest = studentRepository.findAll();
          return admin_RegistrarAccountServiceImpl.exportingStudentRequestToExcel(response, session, studRequest,
                    request);
     }

     @GetMapping("/{userType}/student-request-export/confirm")
     public void exportStudentRequestToExcelConfirm(@PathVariable("userType") String userType, HttpSession session,
               HttpServletResponse response, HttpServletRequest request) {
          List<StudentRequest> studRequest = studentRepository.findAll();
          admin_RegistrarAccountServiceImpl.exportConfirmation(response, session, studRequest, request);
     }

}
