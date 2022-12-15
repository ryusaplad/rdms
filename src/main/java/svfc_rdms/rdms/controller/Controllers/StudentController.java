package svfc_rdms.rdms.controller.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class StudentController {

     @Autowired
     AdminServicesImpl mainService;
     @Autowired
     StudentServiceImpl studService;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     StudentRepository studRepo;

     @GetMapping(value = "/student/dashboard")
     public String studentDashboard(HttpSession session, HttpServletResponse response) {

          try {
               // response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
               // response.setHeader("Pragma", "no-cache");
               // response.setDateHeader("Expires", 0);
               // if (session.getAttribute("username") == null ||
               // session.getAttribute("accountType") == null
               // || session.getAttribute("studentName") == null) {
               // // If the session is not valid, redirect to the login page
               // response.sendRedirect("/");

               // } else {
               // if (session.getAttribute("accountType").toString().contains("Student")) {

               // return "/student/stud";
               // } else {

               // response.sendRedirect("/");
               // }
               // }
               return "/student/stud";
          } catch (Exception e) {
               e.printStackTrace();
          }
          return null;
     }

     @GetMapping("/student/request-list")
     public String fetchDocumentCards(Model model) {
          List<Documents> documentList = mainService.getAllDocuments();
          model.addAttribute("documentsCards", documentList);
          return "/student/student-request-cards";
     }

     @GetMapping("/student/my-requests")
     public String listOfStudentRequest(Model model, HttpSession session) {

          String username = session.getAttribute("username").toString();
          Users user = studService.getUserIdByUsername(username);
          List<StudentRequest_Dto> studRequestResults = new ArrayList<>();
          List<StudentRequest> fetchStudentRequest = studService.displayRequestByStudent(user);

          fetchStudentRequest.stream().forEach(req -> {
               studRequestResults.add(new StudentRequest_Dto(req.getRequestId(), req.getRequestBy().getUserId(),
                         req.getRequestBy().getType(), req.getYear(),
                         req.getCourse(), req.getSemester(), req.getRequestDocument().getTitle(),
                         req.getMessage(), req.getRequestBy().getName(), req.getRequestDate(),
                         req.getRequestStatus(), req.getReleaseDate(), req.getManageBy()));
          });
          model.addAttribute("myrequest", studRequestResults);
          return "/student/student-request-list";
     }

     @GetMapping(value = "/student/request/{document}")
     public String requestForm(@PathVariable String document, HttpServletRequest request, Model model) {

          try {
               if (!studService.findDocumentByTitle(document)) {
                    return "redirect:" + "/student/request-list";
               }
               String description = docRepo.findByTitle(document).getDescription();
               model.addAttribute("documentType", document);
               model.addAttribute("description", description);
          } catch (Exception e) {
               System.out.println("error in student controller: " + e.getMessage());
          }
          return "/student/request-form";

     }

     @PostMapping("/request/{document}/sent")
     public String sentRequest(@PathVariable String document,
               @RequestParam Map<String, String> params, Model model) {

          try {

               StudentRequest req = new StudentRequest();
               Users user = new Users();

               for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (entry.getKey().equals("userId")) {
                         // user = studRepo.findUserIdByUsername(entry.getValue());
                         req.setRequestBy(user);
                    } else if (entry.getKey().equals("year")) {
                         req.setYear(entry.getValue());
                    } else if (entry.getKey().equals("course")) {
                         req.setCourse(entry.getValue());
                    } else if (entry.getKey().equals("semester")) {
                         req.setSemester(entry.getValue());
                    }
               }
               req.setMessage("HI");
               req.setRequestDate("2022");
               req.setRequestStatus("Pending");
               req.setReleaseDate("2022");
               req.setManageBy("admin");
               if (studService.findDocumentByTitle(document)) {
                    req.setRequestDocument(docRepo.findByTitle(document));

               }

               // if (studService.saveRequest(req)) {
               // model.addAttribute("message", "Request Successfully Sent.");
               // return "/request-form";
               // } else {
               // model.addAttribute("message", "Request Failed to sent.");
               // return "/request-form";
               // }

          } catch (Exception e) {
               model.addAttribute("message", "Request Failed to sent, Reason: " + e.getMessage());
          }
          return "redirect:/student_requests";
     }
}
