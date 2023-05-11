package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Teacher.Teacher_ServiceImpl;

@Controller
public class TeacherController {

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private Teacher_ServiceImpl teacherService;

     @GetMapping(value = "/teacher/dashboard")
     public String teacherDashboard(HttpSession session, HttpServletResponse response,Model model) {

          if (globalService.validatePages("teacher", response, session)) {
               model.addAttribute("pageTitle", "Dashboard");
               model.addAttribute("page", "dashboard");
               return "teacher/teach";

          }
          return "redirect:/";
     }

     @GetMapping(value = "/teacher/registrar-requests")
     public String viewRegistrarRequests(HttpSession session, HttpServletResponse response, Model model) {

          if (globalService.validatePages("teacher", response, session)) {
               model.addAttribute("pageTitle", "Recieved Requests");
               model.addAttribute("page", "requests");
               return "teacher/teach";
          }
          return "redirect:/";
     }

     @GetMapping("/teacher/documents")
     public String teacherUploadedFiles(HttpSession session, HttpServletResponse response, Model model) {

          if (globalService.validatePages("teacher", response, session)) {
               
              return teacherService.displayAllFilesByUserId(session, model);

          } 
               return "redirect:/";
          
         
     }
}
