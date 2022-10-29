package svfc_rdms.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

     @GetMapping(value = "/")
     public String loginPage() {

          return "/index";
     }
}
