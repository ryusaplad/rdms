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
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class StudentController {

     @Autowired
     private AdminServicesImpl mainService;
     @Autowired
     private StudentServiceImpl studService;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private DocumentRepository docRepo;

     @Autowired
     private StudentRepository studRepo;

     @Autowired
     private UsersRepository userRepo;

     @GetMapping(value = "/student/dashboard")
     public String studentDashboard(HttpSession session, HttpServletResponse response) {

          if (globalService.validatePages("student", response, session)) {

               return "/student/stud";
          }
          return "redirect:/";
     }

     @GetMapping("/student/request/documents")
     public String fetchDocumentCards(HttpSession session, HttpServletResponse response, Model model) {

          if (globalService.validatePages("student", response, session)) {

               List<Documents> documentList = mainService.getAllDocuments();
               model.addAttribute("documentsCards", documentList);
               return "/student/student-request-cards";
          }
          return "redirect:/";
     }

     @GetMapping("/student/my-requests")
     public String listOfStudentRequest(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("student", response, session)) {
               return studService.displayStudentRequests(model, session);
          }
          return null;

     }

     @GetMapping("/student/my-documents")
     public String listOfDocuments(HttpServletResponse response, HttpSession session, Model model) {

          if (globalService.validatePages("student", response, session)) {

               return studService.displayAllFilesByUserId(session, model);

          }
          return "redirect:/";

     }

     @GetMapping(value = "/student/request/{document}")
     public String requestForm(@PathVariable String document, HttpServletRequest request, HttpServletResponse response,
               HttpSession session, Model model) {
          globalService.validatePages("student", response, session);
          try {
               if (studService.findDocumentByTitle(document).isEmpty()) {
                    return "redirect:" + "/student/request/documents";
               }
               String description = docRepo.findByTitle(document).get().getDescription();

               model.addAttribute("documentType", document);
               model.addAttribute("description", description);
               return "/student/student-request-form";
          } catch (Exception e) {
               e.printStackTrace();
          }

          return null;

     }

     @GetMapping("/student/documents/image")
     public void showImage(@Param("documentId") long id, HttpServletResponse response,
               Optional<Documents> dOptional) {
          studService.student_showImageFiles(id, response, dOptional);

     }

}
