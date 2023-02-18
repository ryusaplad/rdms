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

import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Student.Student_RequestServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.Student_RequirementServiceImpl;

@RestController
public class Student_RestController {


     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private Student_RequestServiceImpl requestServiceImpl;

     @Autowired
     private Student_RequirementServiceImpl requirementServiceImpl;

     @PostMapping("/student/request/{document}/sent")
     public ResponseEntity<Object> studRequestSent(@RequestParam("studentId") String id,
               @RequestParam("file[]") Optional<MultipartFile[]> files, @PathVariable String document,
               @RequestParam Map<String, String> params, HttpServletResponse response, HttpSession session) {

          if (globalService.validatePages("student", response, session)) {
               return requestServiceImpl.saveRequest(id, files, document, params);
          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.", HttpStatus.OK);
     }

     @PostMapping("/student/request/file/update")
     public ResponseEntity<Object> updateFileRequirement(@RequestParam("file") Optional<MultipartFile> file,
               @RequestParam Map<String, String> params, HttpServletResponse response, HttpSession session) {
          if (globalService.validatePages("student", response, session)) {
               return requirementServiceImpl.updateFileRequirement(file, params);
          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.", HttpStatus.OK);
     }

     @PostMapping("/student/request/info/update")
     public ResponseEntity<Object> updateInformationRequirement(
               @RequestParam long requestId, @RequestParam Map<String, String> params, HttpServletResponse response,
               HttpSession session) {
          if (globalService.validatePages("student", response, session)) {
               return requirementServiceImpl.updateInformationRequirement(requestId, params);
          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.", HttpStatus.OK);

     }

     @GetMapping("/student/requests/resubmit")
     public ResponseEntity<Object> resubmitStudentRequests(
               @RequestParam("userId") long userId, @RequestParam("requestId") long requestId,
               HttpServletResponse response, HttpSession session) {
          if (globalService.validatePages("student", response, session)) {
               return requirementServiceImpl.resubmitRequests("Pending", userId, requestId);
          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.", HttpStatus.OK);

     }

     @GetMapping("/student/my-requests/fetch")
     public ResponseEntity<Object> getUserFilesByRequestId(@RequestParam("requestId") Long requestId,
               HttpServletResponse response, HttpSession session) {

          if (globalService.validatePages("student", response, session)) {
               String username = session.getAttribute("username").toString();
               return requestServiceImpl.fetchRequestInformationToModals(username, requestId);
          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.", HttpStatus.OK);

     }

}
