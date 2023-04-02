package svfc_rdms.rdms.controller.RestControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.GlobalLogs;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.Admin_Registrar_ManageAccountServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.StudentRequest_ChartsLogicServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.Student_RequestServiceImpl;

@RestController
public class Admin_RestController {

     @Autowired
     private AdminServicesImpl mainService;

     @Autowired
     private UsersRepository userRepo;

     @Autowired
     private Student_RequestServiceImpl requestServiceImpl;

     @Autowired
     private Admin_Registrar_ManageAccountServiceImpl adminAccountService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsService;
     
     @Autowired
     private StudentRequest_ChartsLogicServiceImpl student_RequestChartServiceImpl;

     @PostMapping(value = "/admin/saveUserAcc")
     public ResponseEntity<Object> saveUser(@RequestBody Users user, Model model, HttpSession session) {

          return adminAccountService.saveUsersAccount(user, 0, session);
     }

     @PostMapping(value = "/admin/updateUserAcc")
     public ResponseEntity<Object> updateUser(@RequestBody Users user, Model model, HttpSession session) {

          return adminAccountService.saveUsersAccount(user, 1, session);
     }

     @GetMapping(value = "/admin/getAllUser")
     public ResponseEntity<Object> getAllUser(@RequestParam("account-type") String accountType,
               @RequestParam("status") String status) {

          List<Users> users = mainService.diplayAllAccounts(status, accountType);

          List<Users> storedAccountsWithoutImage = new ArrayList<>();

          for (Users user : users) {
               storedAccountsWithoutImage
                         .add(new Users(user.getUserId(), user.getName(), user.getEmail(), user.getUsername(),
                                   user.getPassword(),
                                   user.getType(), user.getStatus(), "display"));

          }
          users = storedAccountsWithoutImage;
          ServiceResponse<List<Users>> serviceResponse = new ServiceResponse<>("success", users);

          return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);

     }

     @GetMapping("/admin/dashboard/studentrequest/fetch")
     public ResponseEntity<Object> getRequestInformation(
               @RequestParam("s") Long userId,
               @RequestParam("req") Long requestId,
               HttpServletResponse response,
               HttpSession session) {

          Optional<Users> user = userRepo.findByuserId(userId);
          if (!user.isPresent()) {
               throw new ApiRequestException(
                         "Failed to get user informations, Please Try Again!. Please contact the administrator for further assistance.");
          }
          String username = user.get().getUsername();
          return requestServiceImpl.fetchRequestInformationToModals(username, requestId);
     }

     @GetMapping("/admin/update-document-cards")
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

     @PostMapping("/admin/save-document-info")
     public void saveDocument(@RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params, HttpSession session) {

          mainService.saveDocumentData(partFile, params, session);

     }

     @DeleteMapping("/admin/delete-document-info")
     public ResponseEntity<Object> deleteDocument(@RequestParam("docId") long documentId, HttpSession session) {

          try {
               if (mainService.deleteDocumentFile(documentId, session)) {
                    return new ResponseEntity<Object>("success", HttpStatus.OK);
               } else {
                    return new ResponseEntity<Object>("Invalid Document ID, Must be valid Document Id.",
                              HttpStatus.BAD_REQUEST);
               }
          } catch (Exception e) {
               if (e.getMessage().contains("ConstraintViolationException")) {
                    throw new ApiRequestException("You cannot delete the document, if someone has requested it.");
               } else {
                    throw new ApiRequestException("Document Deletion Failed, Please try again.");
               }

          }
     }

     @PostMapping("/admin/update-document-info")
     public void updateDocument(@RequestParam("docId") long id,
               @RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params, HttpSession session) {

          mainService.saveDocumentData(id, partFile, params, session);

     }

     @GetMapping(value = "/admin/fetch-document-to-modal")
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

     @GetMapping(value = "/admin/user/delete")

     public ResponseEntity<String> deleteUsers(@RequestParam("userId") long userId, HttpServletRequest request,
               HttpSession session) {
          try {
               if (adminAccountService.deleteData(userId, session)) {
                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
          } catch (Exception e) {
               if (e.getMessage().contains("ConstraintViolationException")) {
                    throw new ApiRequestException(
                              "Users with requests cannot be permanently deleted.");
               }

          }
          return new ResponseEntity<>("Failed", HttpStatus.OK);
     }

     @GetMapping("/admin/all-requests-view")
     public ResponseEntity<Object> viewAllRegistrarRequests(@RequestParam long requestId, HttpSession session,
               @RequestParam Map<String, String> params) {

          return mainService.viewAllRegistrarRequests(requestId);
     }

     @GetMapping("/admin/globallogs/fetch")
     public ResponseEntity<GlobalLogs> fetchGlobalLogs(@RequestParam("logId") long logId) {
          return globalLogsService.loadSpecificLogs(logId);
     }

     @GetMapping("/admin/chart/data-filter")
     public ResponseEntity<?> fetchFilterChartData(@RequestParam("s") String status) {
          return student_RequestChartServiceImpl.getCountAndStatusAndYearAndCourseWhereStatusIs(status);
     }

}
