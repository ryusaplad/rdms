package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import svfc_rdms.rdms.serviceImpl.Facilitator.FacilitatorServiceImpl;

@Controller
public class FacilitatorController {
     @Autowired
     private FacilitatorServiceImpl facilitatorServiceImpl;

     @GetMapping(value = "/facilitator/dashboard")
     public String facilitatorDashboard(HttpSession session, HttpServletResponse response) {

          return "/facilitator/facili";

     }

     @GetMapping(value = "/facilitator/studrequest")
     public String studentRequests(HttpSession session, HttpServletResponse response, Model model) {

          return facilitatorServiceImpl.displayAllStudentRequest(model);

     }
}
