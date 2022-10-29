package svfc_rdms.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

     // Get Mapping Method
     @GetMapping("/admin")
     public String methodName() {
          return "/dashboard";
     }
}
