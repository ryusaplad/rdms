package svfc_rdms.rdms.controller.RestControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Admin.AdminRepository;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.File.FileUploadServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@RestController
public class Admin_RestController {

     @Autowired
     AdminServicesImpl mainService;

     @Autowired
     DocumentRepository docRepo;

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

     @GetMapping("/update-document-cards")
     public ResponseEntity<Object> resetDocumentCards() {

          try {
               List<Documents> documents = mainService.getAllDocuments();
               if (documents.size() > -1) {
                    ServiceResponse<List<Documents>> serviceResponse = new ServiceResponse<>("success",
                              documents);

                    return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);
               } else {
                    return new ResponseEntity<Object>("Failed to retrieve document, Invalid Document ID",
                              HttpStatus.BAD_REQUEST);
               }
          } catch (Exception e) {
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
     }

     @PostMapping("/save-document-info")
     public ResponseEntity<Object> saveDocument(@RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params) {

          try {
               return saveDocumentWithRestrict(0, partFile, params, "save");
          } catch (Exception e) {
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
          }

     }

     @DeleteMapping("/delete-document-info")
     public ResponseEntity<Object> deleteDocument(@RequestParam("docId") long documentId) {

          try {
               if (mainService.deleteFile(documentId)) {
                    return new ResponseEntity<Object>("success", HttpStatus.OK);
               } else {
                    return new ResponseEntity<Object>("Failed to delete document!", HttpStatus.BAD_REQUEST);
               }
          } catch (Exception e) {
               if (e.getMessage().contains("exist")) {
                    return new ResponseEntity<Object>("Invalid Document ID, Must be valid Document Id.",
                              HttpStatus.BAD_REQUEST);
               }
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
     }

     @PostMapping("/update-document-info")
     public ResponseEntity<Object> updateDocument(@RequestParam("docId") long id,
               @RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params) {

          try {

               return saveDocumentWithRestrict(id, partFile, params, "update");

          } catch (Exception e) {
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
          }

     }

     @GetMapping(value = "/fetch-document-to-modal")
     public ResponseEntity<Object> getAllDocument(@RequestParam("docId") long docId) {

          try {
               Optional<Documents> documents = mainService.getFileById(docId);

               if (documents.isPresent()) {
                    ServiceResponse<Optional<Documents>> serviceResponse = new ServiceResponse<>("success",
                              documents);

                    return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);
               } else {
                    return new ResponseEntity<Object>("Failed to retrieve document, Invalid Document ID",
                              HttpStatus.BAD_REQUEST);
               }
          } catch (Exception e) {
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
          }

     }

     public ResponseEntity<Object> saveDocumentWithRestrict(long id, MultipartFile partFile,
               Map<String, String> params, String actions) {
          try {

               if (actions.equals("save")) {
                    if (partFile.getSize() < 0) {
                         return new ResponseEntity<Object>("Invalid Image File, Please Try Again.",
                                   HttpStatus.BAD_REQUEST);
                    } else if (params.isEmpty()) {
                         return new ResponseEntity<Object>("No Parameters Found", HttpStatus.BAD_REQUEST);
                    } else {
                         if (!mainService.saveDocumentData(partFile, params)) {
                              throw new IllegalArgumentException(
                                        "Failed to Save Document, It's either invalid Image or Invalid Informations, Please Try Again.");
                         }
                    }
               } else if (actions.equals("update")) {

                    if (id < 0) {
                         throw new IllegalArgumentException(
                                   "Failed to Update Document, Invalid ID, Please Try Again.");
                    } else {
                         if (!mainService.saveDocumentData(id, partFile, params)) {
                              throw new IllegalArgumentException(
                                        "Failed to Update Document, Invalid Informations, Please Try Again.");
                         }
                    }
               } else {
                    throw new IllegalArgumentException(
                              "Invalid Adding Actions, Please Try Again.");
               }

               return new ResponseEntity<Object>("success", HttpStatus.OK);
          } catch (Exception e) {
               return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }

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
