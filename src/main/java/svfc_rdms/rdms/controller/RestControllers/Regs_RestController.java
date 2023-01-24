package svfc_rdms.rdms.controller.RestControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.serviceImpl.Registrar.Registrar_ServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@RestController
public class Regs_RestController {

     @Autowired
     private StudentServiceImpl studentServiceImpl;

     @Autowired
     private UsersRepository userRepository;

     @Autowired
     private Registrar_ServiceImpl regs_ServiceImpl;

     @PostMapping(value = "/registrar/save/user-account")
     public ResponseEntity<Object> saveUser(@RequestBody Users user, Model model) {

          return regs_ServiceImpl.saveUsersAccount(user, 0);
     }

     @PostMapping(value = "/registrar/save/update-account")
     public ResponseEntity<Object> updateUser(@RequestBody Users user, Model model) {

          return regs_ServiceImpl.saveUsersAccount(user, 1);
     }

     @GetMapping("/registrar/user/update")
     @ResponseBody
     public List<Users> returnUserById(@RequestParam("userId") long id) {

          Optional<Users> users = regs_ServiceImpl.findOneUserById(id);

          List<Users> usersList = new ArrayList<>();
          if (users.isPresent()) {
               users.stream().forEach(e -> {

                    usersList.add(new Users(users.get().getUserId(), users.get().getName(), users.get().getUsername(),
                              users.get().getPassword().replace(users.get()
                                        .getPassword(), ""),
                              users.get().getType(), users.get().getStatus()));
               });
               return usersList;
          }
          return usersList;

     }

     @GetMapping(value = "/registrar/user/change/{status}")

     public ResponseEntity<Object> changeStatus(@PathVariable("status") String status,
               @RequestParam("userId") long userId) {
          if (status.equals("permanently")) {
               if (regs_ServiceImpl.deleteData(userId)) {
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
          } else if (status.equals("temporary")) {
               if (regs_ServiceImpl.changeAccountStatus("Temporary", userId)) {
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
          } else if (status.equals("active")) {
               if (regs_ServiceImpl.changeAccountStatus("Active", userId)) {
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
          }
          throw new ApiRequestException(
                    "You are performing invalid action. Please contact the administrator for further assistance.");

     }

     @GetMapping(value = "/registrar/getAllAccounts")
     public ResponseEntity<Object> getAllUser(@RequestParam("account-type") String accountType) {
          return regs_ServiceImpl.displayAllUserAccountByType(accountType);
     }

     @GetMapping("/registrar/studentrequest/fetch")
     public ResponseEntity<Object> getRequestInformation(
               @RequestParam("s") Long userId,
               @RequestParam("req") Long requestId,
               HttpServletResponse response,
               HttpSession session) {

          Optional<Users> user = userRepository.findByuserId(userId);
          if (!user.isPresent()) {
               throw new ApiRequestException(
                         "Failed to get user informations, Please Try Again!. Please contact the administrator for further assistance.");
          }
          String username = user.get().getUsername();
          return studentServiceImpl.fetchRequestInformationToModals(username, requestId);
     }

     @GetMapping(value = "/registrar/studentreq/change/{status}")
     @ResponseBody
     public ResponseEntity<Object> changeRequestInformations(
               @PathVariable("status") String status,
               @RequestParam("userId") long userId,
               @RequestParam("requestId") long requestId,
               @RequestParam("reason") String message,
               HttpServletResponse response,
               HttpSession session) {

          if (!regs_ServiceImpl.changeStatusAndManageByAndMessageOfRequests(status, message, userId, requestId,
                    session)) {
               throw new ApiRequestException(
                         "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");
          }
          return new ResponseEntity<>("Success", HttpStatus.OK);
     }

     @PostMapping("/registrar/studentreq/finalized")
     public ResponseEntity<Object> finalizedRequest(@RequestParam long userId, @RequestParam long requestId,
               @RequestParam("rfile[]") Optional<MultipartFile[]> files,
               @RequestParam Map<String, String> params, HttpSession session) {

          return regs_ServiceImpl.finalizedRequestsWithFiles(userId, requestId, files, params, session);

     }

     @GetMapping("/session/{sessionKey}")
     public ResponseEntity<String> getSessionValue(@PathVariable("sessionKey") String sessionKey, HttpSession session) {
          return new ResponseEntity<>((String) session.getAttribute(sessionKey), HttpStatus.OK);
     }

     // Manage Requests for teachers.
     @PostMapping("/registrar/send/requests")
     public ResponseEntity<String> sendRequestsToTeacher(@RequestParam long userId,HttpSession session,
               @RequestParam Map<String, String> params) {

          return regs_ServiceImpl.sendRequestToTeacher(userId, session, params);
     }

     @GetMapping("/registrar/sent-requests-view")
     public ResponseEntity<Object> viewRequestsToTeacher(@RequestParam long requestId, HttpSession session,
               @RequestParam Map<String, String> params) {

          return regs_ServiceImpl.viewRegistrarRequests(requestId);
     }
}
