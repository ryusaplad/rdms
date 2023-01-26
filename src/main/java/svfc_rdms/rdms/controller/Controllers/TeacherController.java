package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class TeacherController {

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @GetMapping(value = "/teacher/dashboard")
     public String registrarDashboard(HttpSession session, HttpServletResponse response) {
          if (globalService.validatePages("teacher", response, session)) {
               return "/teacher/teach";
          }
          return "redirect:/";
     }
}
