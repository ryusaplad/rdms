package svfc_rdms.rdms.controller.RestControllers;

import java.util.Map;
import java.util.Optional;

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

import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.serviceImpl.File.FileUploadServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@RestController
public class Student_RestController {

     @Autowired
     FileUploadServiceImpl studFileService;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     StudentServiceImpl studService;

     @PostMapping("/student/request/{document}/sent")
     public ResponseEntity<String> studRequestSent(@RequestParam("studentId") String id,
               @RequestParam("file[]") Optional<MultipartFile[]> files, @PathVariable String document,
               @RequestParam Map<String, String> params, HttpServletResponse response, HttpSession session) {
          validatePages(response, session);
          return studService.saveRequest(id, files, document, params);
     }

     @PostMapping("/student/request/file/update")
     public ResponseEntity<Object> updateFileRequirement(@RequestParam("file") Optional<MultipartFile> file,
               @RequestParam Map<String, String> params) {

          return studService.updateFileRequirement(file, params);
     }

     @PostMapping("/student/request/info/update")
     public ResponseEntity<Object> updateInformationRequirement(
               @RequestParam long requestId, @RequestParam Map<String, String> params) {

          return studService.updateInformationRequirement(requestId, params);
     }

     @GetMapping("/student/requests/resubmit")
     public ResponseEntity<Object> resubmitStudentRequests(
               @RequestParam("userId") long userId, @RequestParam("requestId") long requestId,
               HttpServletResponse response, HttpSession session) {

          return studService.resubmitRequests("Pending", userId, requestId);
     }

     @GetMapping("/student/my-requests/fetch")
     public ResponseEntity<Object> getUserFilesByRequestId(@RequestParam("requestId") Long requestId,
               HttpServletResponse response, HttpSession session) {
          validatePages(response, session);
          String username = session.getAttribute("username").toString();
          return studService.fetchRequestInformationToModals(username, requestId);
     }


     public ResponseEntity<String> validatePages(HttpServletResponse response, HttpSession session) {
          try {
               response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
               response.setHeader("Pragma", "no-cache");
               response.setDateHeader("Expires", 0);
               if (session.getAttribute("username") == null ||
                         session.getAttribute("accountType") == null
                         || session.getAttribute("name") == null) {
                    // If the session is not valid, redirect to the login page
                    response.sendRedirect("/");

               }
               return new ResponseEntity<>("Pass", HttpStatus.OK);
          } catch (Exception e) {
               e.printStackTrace();
          }
          return new ResponseEntity<>("Failed", HttpStatus.UNAUTHORIZED);
     }
}
