package svfc_rdms.rdms.controller.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrarController {
     @GetMapping(value = "/registrar/dashboard")
     public String registrarDashboard() {
          return "/registrar/reg";
     }
}
