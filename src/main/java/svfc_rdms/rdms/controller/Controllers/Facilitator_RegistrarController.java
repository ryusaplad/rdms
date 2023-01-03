package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.serviceImpl.Facilitator_Registrar.Facilitator_Registrar_ServiceImpl;

@Controller
public class Facilitator_RegistrarController {
     @Autowired
     private Facilitator_Registrar_ServiceImpl faci_regs_ServiceImpl;

     @GetMapping(value = "/facilitator/dashboard")
     public String facilitatorDashboard(HttpSession session, HttpServletResponse response) {

          return "/facilitator/facili";

     }

     @GetMapping(value = "/registrar/dashboard")
     public String registrarDashboard() {
          return "/registrar/reg";
     }

     @GetMapping(value = "/{userType}/studrequest")
     public String studentRequests(@PathVariable String userType, HttpSession session, HttpServletResponse response,
               Model model) {

          return faci_regs_ServiceImpl.displayAllStudentRequest(userType, model);

     }
}
