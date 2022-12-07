package svfc_rdms.rdms.controller;

import java.time.LocalDate;
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
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.AdminRepository;
import svfc_rdms.rdms.repository.Admin_DocumentRepository;
import svfc_rdms.rdms.serviceImpl.FileUploadServiceImpl;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;
import svfc_rdms.rdms.serviceImpl.StudentServiceImpl;

@RestController
public class Admin_RestController {

     @Autowired
     MainServiceImpl mainService;

     @Autowired
     Admin_DocumentRepository docRepo;

     @Autowired
     AdminRepository adminRepo;

     @Autowired
     StudentServiceImpl studService;

     @Autowired
     FileUploadServiceImpl studFileService;

     @PostMapping(value = "/saveUserAcc")
     public ResponseEntity<Object> saveUser(@RequestBody Users user, Model model) {
          ServiceResponse<Users> serviceResponse = new ServiceResponse<>("success", user);
          return saveUserWithRestrict(user, 0, serviceResponse);

     }

     @PostMapping(value = "/updateUserAcc")
     public ResponseEntity<Object> updateUser(@RequestBody Users user, Model model) {

          ServiceResponse<Users> serviceResponse = new ServiceResponse<>("success", user);
          return saveUserWithRestrict(user, 1, serviceResponse);

     }

     @GetMapping(value = "/getAllUser")
     public ResponseEntity<Object> getAllUser(@RequestParam("account-type") String accountType,
               @RequestParam("status") String status) {

          List<Users> users = mainService.diplayAllAccounts(status, accountType);
          ServiceResponse<List<Users>> serviceResponse = new ServiceResponse<>("success", users);

          return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);

     }

     @PostMapping("/save_file")

     public ResponseEntity<String> studFileUploadRequirement(@RequestParam("studentId") String id,
               @RequestParam("file[]") MultipartFile[] files) {
          try {

               LocalDate dateNow = LocalDate.now();

               if (files != null || id.length() < -1) {
                    Users user = adminRepo.findUserIdByUsername(id);
                    for (MultipartFile filex : files) {
                         UserFiles userFiles = new UserFiles();
                         userFiles.setData(filex.getBytes());
                         userFiles.setName(filex.getOriginalFilename());
                         userFiles.setSize(Long.toString(filex.getSize()));
                         userFiles.setDateUploaded(dateNow.toString());
                         userFiles.setStatus("Pending");
                         userFiles.setUploadedBy(user);
                         studFileService.saveFiles(userFiles);
                    }

               } else {
                    return new ResponseEntity<>("No File Has been save", HttpStatus.BAD_REQUEST);
               }

          } catch (Exception ex) {
               ex.printStackTrace();
               return new ResponseEntity<>("Invalid file format!!", HttpStatus.BAD_REQUEST);
          }

          return new ResponseEntity<>("File uploaded!!", HttpStatus.OK);
     }

     public ResponseEntity<Object> saveUserWithRestrict(Users user, int action,
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
