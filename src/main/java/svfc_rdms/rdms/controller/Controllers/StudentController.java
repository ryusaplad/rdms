package svfc_rdms.rdms.controller.Controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class StudentController {

     @Autowired
     StudentServiceImpl studService;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     StudentRepository studRepo;

     @GetMapping(value = "/request/{document}")
     public String requestForm(@PathVariable String document, HttpServletRequest request, Model model) {

          try {
               if (!studService.findDocumentByTitle(document)) {
                    return "redirect:" + "/student_request";
               }
               String description = docRepo.findByTitle(document).getDescription();
               model.addAttribute("documentType", document);
               model.addAttribute("description", description);
          } catch (Exception e) {
               System.out.println("error in global: " + e.getMessage());
          }
          return "/request-form";

     }

     @PostMapping("/request/{document}/sent")
     public String sentRequest(@PathVariable String document,
               @RequestParam Map<String, String> params, Model model) {

          try {

               StudentRequest req = new StudentRequest();
               Users user = new Users();

               for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (entry.getKey().equals("userId")) {
                         //user = studRepo.findUserIdByUsername(entry.getValue());
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
