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
     StudentServiceImpl studService;

     @Autowired
     FileUploadServiceImpl studFileService;

     @PostMapping(value = "/saveUserAcc")
     public ResponseEntity<Object> saveUser(@RequestBody Users user, Model model) {

          return mainService.saveUsersAccount(user, 0);
     }

     @PostMapping(value = "/updateUserAcc")
     public ResponseEntity<Object> updateUser(@RequestBody Users user, Model model) {

          return mainService.saveUsersAccount(user, 1);
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
     public void saveDocument(@RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params) {

          mainService.saveDocumentData(partFile, params);

     }

     @DeleteMapping("/delete-document-info")
     public ResponseEntity<Object> deleteDocument(@RequestParam("docId") long documentId) {

          try {
               if (mainService.deleteDocumentFile(documentId)) {
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
     public void updateDocument(@RequestParam("docId") long id,
               @RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params) {

          mainService.saveDocumentData(id, partFile, params);

     }

     @GetMapping(value = "/fetch-document-to-modal")
     public ResponseEntity<Object> getAllDocument(@RequestParam("docId") long docId) {

          try {
               Optional<Documents> documents = mainService.getFileDocumentById(docId);

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

}
