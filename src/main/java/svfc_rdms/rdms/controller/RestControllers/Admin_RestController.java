package svfc_rdms.rdms.controller.RestControllers;

import java.io.IOException;
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
import svfc_rdms.rdms.serviceImpl.Global.Admin_Registrar_ManageServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
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
     private Admin_Registrar_ManageServiceImpl adminAccountService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsService;
     
     @Autowired
     private StudentRequest_ChartsLogicServiceImpl student_RequestChartServiceImpl;
     
     @Autowired
     private GlobalServiceControllerImpl globalService;

     @PostMapping(value = "/svfc-admin/saveUserAcc")
     public ResponseEntity<Object> saveUser(@RequestBody Users user, Model model, HttpSession session,HttpServletRequest request) {

          return adminAccountService.saveUsersAccount(user, 0, session,request);
     }

     @PostMapping(value = "/svfc-admin/updateUserAcc")
     public ResponseEntity<Object> updateUser(@RequestBody Users user, Model model, HttpSession session,HttpServletRequest request) {

          return adminAccountService.saveUsersAccount(user, 1, session,request);
     }

     @GetMapping(value = "/svfc-admin/getAllUser")
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

     @GetMapping("/svfc-admin/student-request/fetch")
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

     @GetMapping("/svfc-admin/update-document-cards")
     public ResponseEntity<Object> loadAllDocumentCards() {

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

     @PostMapping("/svfc-admin/save-document-info")
     public void saveDocument(@RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params, HttpSession session,HttpServletRequest request) {

          mainService.saveDocumentData(partFile, params, session,request);

     }

     @DeleteMapping("/svfc-admin/delete-document-info")
     public ResponseEntity<Object> deleteDocument(@RequestParam("docId") long documentId, HttpSession session,HttpServletRequest request) {

          try {
               if (mainService.deleteDocumentFile(documentId, session,request)) {
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

     @PostMapping("/svfc-admin/update-document-info")
     public void updateDocument(@RequestParam("docId") long id,
               @RequestParam("image") MultipartFile partFile,
               @RequestParam Map<String, String> params, HttpSession session,HttpServletRequest request) {

          mainService.saveDocumentData(id, partFile, params, session,request);

     }

     @GetMapping(value = "/svfc-admin/fetch-document-to-modal")
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

     @GetMapping(value = "/svfc-admin/user/delete")

     public ResponseEntity<String> deleteUsers(@RequestParam("userId") long userId, HttpServletRequest request,
               HttpSession session) {
          try {
               if (adminAccountService.deleteData(userId, session,request)) {
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

     @GetMapping("/svfc-admin/view/request-data")
     public ResponseEntity<Object> viewAllRegistrarRequests(@RequestParam long requestId, HttpSession session,
               @RequestParam Map<String, String> params) {

          return mainService.viewAllRegistrarRequestsByRequestId(requestId);
     }

     @GetMapping("/fetch/svfc-admin/global_logs")
     public ResponseEntity<Object> viewAdminLogs(Model model) {
       return new ResponseEntity<>(globalLogsService.getAllLogs(),HttpStatus.OK);
     }

     @GetMapping("/svfc-admin/globallog/fetch")
     public ResponseEntity<GlobalLogs> fetchGlobalLogs(@RequestParam("logId") long logId) {
          return globalLogsService.loadSpecificLogs(logId);
     }

     @GetMapping("/svfc-admin/chart/data-filter")
     public ResponseEntity<?> fetchFilterChartData(@RequestParam("s") String status,@RequestParam("d") String date) {
          return student_RequestChartServiceImpl.getCountAndRequestStatusAndYearAndCourseWhereStatusIsAndDateIs(status,date);
     }

     @GetMapping("/svfc-admin/delete/file")
     public ResponseEntity<String> deleteFile(
               @RequestParam("id") String fileId,
               HttpServletResponse response,
               HttpSession session) throws IOException {

          if (session == null) {
               throw new ApiRequestException("Invalid session or service");
          }

          if (!globalService.validatePages("school_admin", response, session)) {
               throw new ApiRequestException("Invalid session requests");
          }

          globalService.deleteFile(fileId);
          return ResponseEntity.ok("File deleted");
     }

     

}
