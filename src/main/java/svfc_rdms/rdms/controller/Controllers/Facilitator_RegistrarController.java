package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.serviceImpl.Facilitator_Registrar.Facilitator_Registrar_ServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class Facilitator_RegistrarController {
     @Autowired
     private Facilitator_Registrar_ServiceImpl faci_regs_ServiceImpl;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @GetMapping(value = "/facilitator/dashboard")
     public String facilitatorDashboard(HttpSession session, HttpServletResponse response) {
          if (globalService.validatePages(response, session)) {
               return "/facilitator/facili";
          }

          return null;
     }

     @GetMapping(value = "/registrar/dashboard")
     public String registrarDashboard(HttpSession session, HttpServletResponse response) {
          if (globalService.validatePages(response, session)) {
               return "/registrar/reg";
          }
          return null;
     }

     @GetMapping(value = "/{userType}/studrequest")
     public String studentRequests(@PathVariable String userType, HttpSession session, HttpServletResponse response,
               Model model) {
          if (globalService.validatePages(response, session)) {
               return faci_regs_ServiceImpl.displayAllStudentRequest(userType, model);
          }
          return null;

     }
}
