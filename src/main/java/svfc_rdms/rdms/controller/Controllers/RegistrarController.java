package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Registrar.Registrar_ServiceImpl;

@Controller
public class RegistrarController {

     @Autowired
     private Registrar_ServiceImpl regs_ServiceImpl;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @GetMapping(value = "/registrar/dashboard")
     public String registrarDashboard(HttpSession session, HttpServletResponse response) {
          if (globalService.validatePages("registrar", response, session)) {
               return "/registrar/reg";
          }
          return "redirect:/";
     }

     @GetMapping(value = "/{userType}/studrequest")
     public String studentRequests(@PathVariable String userType, HttpSession session, HttpServletResponse response,
               Model model) {
          if (globalService.validatePages(userType, response, session)) {
               return regs_ServiceImpl.displayAllStudentRequest(userType, model);
          }
          return "redirect:/";

     }
}
