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
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.NotificationRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.repository.Teacher.TeacherRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.Admin_Registrar_ManageServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.AllAccountServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;
import svfc_rdms.rdms.serviceImpl.Registrar.Reg_RequestServiceImpl;

@RestController
public class AllAccount_RestController {

     @Autowired
     private AdminServicesImpl mainService;

     @Autowired
     private AllAccountServiceImpl accountServiceImpl;

     @Autowired
     private Admin_Registrar_ManageServiceImpl admin_RegistrarServiceImpl;

     @Autowired
     private NotificationServiceImpl notificationServiceImpl;

     @Autowired
     private NotificationRepository notificationRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private Reg_RequestServiceImpl regs_RequestService;

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

               if ((username = session.getAttribute("username").toString()) == null) {
                    throw new ApiRequestException("You are performing invalid action, Please try again");
               }
               if (userType.equalsIgnoreCase("svfc-admin")) {
                    userType = "admin";
               }
               ValidAccounts[] validAccountType = ValidAccounts.values();

               for (ValidAccounts validAcc : validAccountType) {

                    if (String.valueOf(validAcc).toLowerCase()
                              .contains(userType.toLowerCase())) {
                         userType = validAcc.toString().toLowerCase();
                         break;
                    }
               }
               if (!globalService.validatePages(userType, response, session)) {
                    throw new ApiRequestException("You are performing invalid action");

               }
               Optional<Users> user = usersRepository.findUserIdByUsername(username);
            
               List<Long> totals = new ArrayList<>();

               if (!user.isPresent()) {

                    return new ResponseEntity<>(
                              "You are performing invalid action, Please try again later.",
                              HttpStatus.BAD_REQUEST);
               }

               Users userInfo = user.get();

               if (userInfo.getType().equalsIgnoreCase("student")) {
                    long totalRequests = studentRepository.countByRequestBy(userInfo);
                    long totalApprovedRequests = studentRepository
                              .countByRequestByAndRequestStatus(userInfo, "Approved");
                    long totalRejectedRequests = studentRepository
                              .countByRequestByAndRequestStatus(userInfo, "Rejected");
                    long totalUploadedFiles = fileRepository.countByUploadedBy(userInfo);

                    totals.add(totalRequests);
                    totals.add(totalApprovedRequests);
                    totals.add(totalRejectedRequests);
                    totals.add(totalUploadedFiles);
                    return new ResponseEntity<>(totals, HttpStatus.OK);
               }
               if (userInfo.getType().equalsIgnoreCase("registrar")) {
                    long totalStudentRequests = studentRepository.count();
                    long totalTeacherRequests = teacherRepository.count();
                    long totalPendingRequests = studentRepository
                              .countByRequestStatus("Pending");
                    long totalApprovedRequests = studentRepository
                              .countByRequestStatus("Approved");
                    long totalRejectedRequests = studentRepository
                              .countByRequestStatus("Rejected");
                    long totalUploadedFiles = fileRepository.countByUploadedBy(userInfo);

                    totals.add(totalStudentRequests);
                    totals.add(totalTeacherRequests);
                    totals.add(totalPendingRequests);
                    totals.add(totalApprovedRequests);
                    totals.add(totalRejectedRequests);
                    totals.add(totalUploadedFiles);
                    return new ResponseEntity<>(totals, HttpStatus.OK);
               }
               if (userInfo.getType().equalsIgnoreCase("teacher")) {

                    long totalRequests = teacherRepository.totalRequests(userInfo);
                    long totalPendingRequests = teacherRepository.totalRequestByUserAndStatus(userInfo,
                              "pending");

                    long totalSentRequests_Completed = teacherRepository
                              .totalRequestByUserAndStatus(userInfo, "completed");

                    long totalSentRequests_NoRecord = teacherRepository
                              .totalRequestByUserAndStatus(userInfo, "norecord");

                    long totalSentRequests_MessageOnly = teacherRepository
                              .totalRequestByUserAndStatus(userInfo, "messageonly");

                    long totalSentRequests_RecordOnly = teacherRepository
                              .totalRequestByUserAndStatus(userInfo, "recordonly");

                    long totalUploadedFiles = fileRepository.countByUploadedBy(userInfo);
                    long totalSentRequests = totalSentRequests_Completed
                              + totalSentRequests_MessageOnly + totalSentRequests_NoRecord
                              + totalSentRequests_RecordOnly;
                    totals.add(totalRequests);
                    totals.add(totalPendingRequests);
                    totals.add(totalSentRequests);
                    totals.add(totalUploadedFiles);
                    return new ResponseEntity<>(totals, HttpStatus.OK);
               }
               if (userInfo.getType().equalsIgnoreCase("school_admin")) {
                    long totalStudentRequests = studentRepository.count();
                    long totalRegistrarRequests = teacherRepository.count();
                    long totalPendingRequests = studentRepository
                              .countByRequestStatus("Pending");
                    long totalApprovedRequests = studentRepository
                              .countByRequestStatus("Approved");
                    long totalRejectedRequests = studentRepository
                              .countByRequestStatus("Rejected");

                    long totalRegistrarAcc = usersRepository.totalUsers("Active",
                              "Registrar");
                    long totalTeacherAcc = usersRepository.totalUsers("Active",
                              "Teacher");
                    long totalStudentsAcc = usersRepository.totalUsers("Active",
                              "Student");

                    long totalDelRegistarAcc = usersRepository.totalUsers("Temporary",
                              "Registrar");
                    long totalDelTeacherAcc = usersRepository.totalUsers("Temporary",
                              "Teacher");
                    long totalDelStudentsAcc = usersRepository.totalUsers("Temporary",
                              "Student");

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
          if ((username = session.getAttribute("username").toString()) == null) {
               throw new ApiRequestException("Invalid Action");
             
          }
          Optional<Users> optionalUser = usersRepository.findByUsername(username);
          if (!optionalUser.isPresent()) {
               throw new ApiRequestException("Invalid Action, Please Try Again.");
          }
          userId = optionalUser.get().getUserId();
          return accountServiceImpl.changePassword(oldPassword, newPassword, userId, session, request);
         

     }

     @PostMapping("/change/account/profile-picture")
     public ResponseEntity<String> changeProfilePicture(@RequestParam MultipartFile image, HttpSession session,
               HttpServletRequest request) {
          long userId = 0;
          String username = "";
         
          if ((username = session.getAttribute("username").toString()) == null) {
               throw new ApiRequestException("Invalid Action");
             
          }
          Optional<Users> optionalUser = usersRepository.findByUsername(username);
          if (!optionalUser.isPresent()) {
               throw new ApiRequestException("Invalid Action Failed To Change Profile Picture, Please Try Again.");
          }
          userId = optionalUser.get().getUserId();
          return accountServiceImpl.changeProfilePicture(image, userId, session, request);
     }

     @GetMapping("/session/{sessionKey}")
     public ResponseEntity<String> getSessionValue(@PathVariable("sessionKey") String sessionKey, HttpSession session) {
          return new ResponseEntity<>((String) session.getAttribute(sessionKey), HttpStatus.OK);
     }

     @GetMapping("/load/image")
     public ResponseEntity<Object> loadUserProfileImage(@RequestParam("username") String username,
               HttpSession session) {
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
                                        notificationServiceImpl.fetchTopNavBarAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else if (userType.equals("registrar")) {

                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchTopNavBarAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else if (userType.equals("teacher")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchTopNavBarAndSidebarNotif(user.get(), userType,
                                                  lowestPage,
                                                  totalPage, status),
                                        HttpStatus.OK);
                         } else if (userType.equals("school_admin")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchTopNavBarAndSidebarNotif(user.get(), userType,
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
                                        notificationServiceImpl.fetchAllNotificationsByUserToMainNotifModal(user.get(),
                                                  userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else if (userType.equals("registrar")) {

                              Optional<Users> user = usersRepository.findByUsername(username);

                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchAllNotificationsByUserToMainNotifModal(user.get(),
                                                  userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else if (userType.equals("teacher")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchAllNotificationsByUserToMainNotifModal(user.get(),
                                                  userType,
                                                  lowestPage,
                                                  totalPage),
                                        HttpStatus.OK);
                         } else if (userType.equals("school_admin")) {
                              Optional<Users> user = usersRepository.findByUsername(username);
                              return new ResponseEntity<>(
                                        notificationServiceImpl.fetchAllNotificationsByUserToMainNotifModal(user.get(),
                                                  userType,
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
               System.out.println(userType);
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
          return admin_RegistrarServiceImpl.exportingStudentRequestToExcel(response, session, studRequest,
                    request);
     }

     @GetMapping("/{userType}/student-request-export/confirm")
     public void exportStudentRequestToExcelConfirm(@PathVariable("userType") String userType, HttpSession session,
               HttpServletResponse response, HttpServletRequest request) {
          List<StudentRequest> studRequest = studentRepository.findAll();
          admin_RegistrarServiceImpl.exportConfirmation(response, session, studRequest, request);
     }

     @GetMapping(value = "/{userType}/fetch/student-requests")
     public ResponseEntity<Object> fetchstudentRequests(@PathVariable("userType") String userType, HttpSession session,
               HttpServletResponse response) {

          if (userType.equalsIgnoreCase("svfc-admin")) {
               userType = "school_admin";
          }
          if (globalService.validatePages(userType, response, session)) {
               return admin_RegistrarServiceImpl.fetchAllStudentRequest();
          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.",
                    HttpStatus.BAD_REQUEST);

     }

     @GetMapping("/{userType}/load/document-info")
     public ResponseEntity<Object> showImage(@PathVariable("userType") String userType,
               HttpSession session) {

          try {
               List<Documents> documents = mainService.getAllDocuments();
               if (documents.size() > -1) {

                    return new ResponseEntity<Object>(documents, HttpStatus.OK);
               } else {
                    return new ResponseEntity<Object>("Failed to retrieve document, Please Try Again!",
                              HttpStatus.BAD_REQUEST);
               }
          } catch (Exception e) {
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
     }

}
