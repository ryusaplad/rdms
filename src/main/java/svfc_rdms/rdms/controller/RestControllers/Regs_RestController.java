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
import svfc_rdms.rdms.dto.ServiceResponse;
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
     public List<Users> returnUserById(@RequestParam("userId") long id, Model model) {

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
          System.out.println("Status: " + status);
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

          List<Users> users = regs_ServiceImpl.diplayAllAccountsByType(accountType);

          List<Users> storedAccountsWithoutImage = new ArrayList<>();

          for (Users user : users) {
               storedAccountsWithoutImage
                         .add(new Users(user.getUserId(), user.getName(), user.getUsername(),
                                   user.getPassword(),
                                   user.getType(), user.getStatus()));

          }
          users = storedAccountsWithoutImage;
          ServiceResponse<List<Users>> serviceResponse = new ServiceResponse<>("success", users);

          return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);

     }

     @GetMapping("/{userType}/studentrequest/fetch")
     public ResponseEntity<Object> getRequestInformation(@PathVariable String userType, @RequestParam("s") Long userId,
               @RequestParam("req") Long requestId,
               HttpServletResponse response, HttpSession session) {
          if (userType.isEmpty() || userType.isBlank()) {
               throw new ApiRequestException("Invalid User.");
          } else {
               Optional<Users> user = userRepository.findByuserId(userId);
               if (user.isPresent()) {
                    Users getUser = user.get();
                    String username = getUser.getUsername();
                    return studentServiceImpl.fetchRequestInformationToModals(username, requestId);
               } else {
                    throw new ApiRequestException("Can't get the users.");
               }
          }

     }

     @GetMapping(value = "/{userType}/studentreq/change/{status}")
     @ResponseBody
     public ResponseEntity<Object> changeRequestInformations(@PathVariable String userType,
               @PathVariable("status") String status,
               @RequestParam("userId") long userId, @RequestParam("requestId") long requestId,
               @RequestParam("reason") String message,
               HttpServletResponse response,
               HttpSession session) {
          List<String> validUserType = new ArrayList<>();
          validUserType.add("admin");
          validUserType.add("registrar");
          if (userType.isEmpty() || userType.isBlank() || !validUserType.contains(userType)) {
               throw new ApiRequestException("Invalid User.");
          } else {
               if (!status.isEmpty() || !status.isBlank() || session.getAttribute("name") != null) {
                    String manageBy = session.getAttribute("name").toString();

                    // changing status based on the input
                    if (regs_ServiceImpl.changeStatusAndManageByAndMessageOfRequests(status, manageBy, message,
                              userId, requestId)) {
                         return new ResponseEntity<>("Success", HttpStatus.OK);
                    } else {
                         throw new ApiRequestException("Failed to change requests status.");
                    }
               }
          }

          return new ResponseEntity<>("failed to change status, invalid data.", HttpStatus.BAD_REQUEST);
     }

     @PostMapping("/registrar/studentreq/finalized")
     public ResponseEntity<Object> finalizedRequest(@RequestParam long userId, @RequestParam long requestId,
               @RequestParam("rfile[]") Optional<MultipartFile[]> files,
               @RequestParam Map<String, String> params, HttpSession session) {

          return regs_ServiceImpl.finalizedRequestsWithFiles(userId, requestId, files, params, session);

     }
}
