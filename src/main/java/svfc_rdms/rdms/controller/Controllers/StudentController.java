package svfc_rdms.rdms.controller.Controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.model.Documents;
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

     @GetMapping("/student/request/documents")
     public String fetchDocumentCards(Model model, HttpSession session) {
          setSessionDefaultValue("username", null, null, session);
          List<Documents> documentList = mainService.getAllDocuments();
          model.addAttribute("documentsCards", documentList);
          return "/student/student-request-cards";
     }

     @GetMapping("/student/my-requests")
     public String listOfStudentRequest(Model model, HttpSession session) {
          setSessionDefaultValue("username", null, null, session);
          return studService.displayStudentRequests(model, session);
     }

     @GetMapping(value = "/student/request/{document}")
     public String requestForm(@PathVariable String document, HttpServletRequest request, Model model) {

          try {
               if (studService.findDocumentByTitle(document).isEmpty()) {
                    return "redirect:" + "/student/request/documents";
               }
               String description = docRepo.findByTitle(document).get().getDescription();

               model.addAttribute("documentType", document);
               model.addAttribute("description", description);

          } catch (Exception e) {
               System.out.println("error in student controller: " + e.getMessage());
          }
          return "/student/student-request-form";

     }

     @GetMapping("/student/documents/image")
     public void showImage(@Param("documentId") long id, HttpServletResponse response,
               Optional<Documents> dOptional) {
          studService.student_showImageFiles(id, response, dOptional);
     }

     @GetMapping("/admin/my-request/download")
     public void downloadFile(@Param("id") long id, Model model, HttpServletResponse response) {
          studService.student_DownloadFile(id, model, response);
     }

     public void setSessionDefaultValue(String variable, String compareTo, String newValue, HttpSession session) {
          if (session.getAttribute(variable) == compareTo) {
               session.setAttribute(variable, newValue);
          }
     }

}
