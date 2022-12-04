package svfc_rdms.rdms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.service.ServiceResponse;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;

@RestController
public class Admin_RestController {

     @Autowired
     MainServiceImpl mainService;

     @PostMapping(value = "/saveUserAcc")
     public ResponseEntity<Object> saveUser(@RequestBody Users user, Model model) {
          ServiceResponse<Users> serviceResponse = new ServiceResponse<>("success", user);
          return saveWithRestriction(user, 0, serviceResponse);

     }

     @PostMapping(value = "/updateUserAcc")
     public ResponseEntity<Object> updateUser(@RequestBody Users user, Model model) {

          ServiceResponse<Users> serviceResponse = new ServiceResponse<>("success", user);
          return saveWithRestriction(user, 1, serviceResponse);

     }

     @GetMapping(value = "/getAllUser")
     public ResponseEntity<Object> getAllUser(@RequestParam("account-type") String accountType,
               @RequestParam("status") String status) {

          List<Users> users = mainService.diplayAllAccounts(status, accountType);
          ServiceResponse<List<Users>> serviceResponse = new ServiceResponse<>("success", users);

          return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);

     }

     public ResponseEntity<Object> saveWithRestriction(Users user, int action,
               ServiceResponse<Users> response) {
          String error = "";
          try {
               if (user.getName() == null || user.getName().length() < 1 || user.getName().isEmpty()) {
                    error = "Name cannot be empty! " + user.getName();
                    return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
               } else if (user.getUsername() == null || user.getUsername().length() < 1
                         || user.getUsername().isEmpty()) {
                    error = "Username cannot be empty " + user.getUsername();
                    return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
               } else if (user.getPassword() == null || user.getPassword().length() < 1
                         || user.getPassword().isEmpty()) {
                    error = "Password cannot be empty" + user.getPassword();
                    return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
               } else {
                    String userIdFormat = user.getUsername().toUpperCase();
                    user.setStatus("Active");
                    if (userIdFormat.contains("C")) {

                         user.setType("Student");
                    } else if (userIdFormat.contains("F-")) {

                         user.setType("Facilitator");
                    } else if (userIdFormat.contains("R-")) {

                         user.setType("Registrar");
                    } else if (userIdFormat.contains("T-")) {

                         user.setType("Teacher");
                    }
                    if (action == 0) {
                         if (mainService.findUserName(userIdFormat.toLowerCase())) {
                              error = "Username is already taken, Please try again!";

                              return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
                         } else {
                              mainService.saveUsersAccount(user);

                              return new ResponseEntity<Object>(response, HttpStatus.OK);
                         }
                    } else if (action == 1) {

                         try {
                              mainService.saveUsersAccount(user);

                              return new ResponseEntity<Object>(response, HttpStatus.OK);
                         } catch (Exception e) {
                              error = e.getMessage();
                              if (error.contains("ConstraintViolationException")) {
                                   error = "Username is already taken, Please try again!";
                                   return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
                              }
                         }
                         return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);

                    }

               }
          } catch (Exception e) {
               error = e.getMessage();
               if (error.contains("ConstraintViolationException")) {

                    error = "Updating Failed!. Username is already taken, Please try again!";
                    return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
               }
          }

          return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
     }
}
