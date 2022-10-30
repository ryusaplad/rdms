package svfc_rdms.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

     // Get Mapping Method
     @GetMapping("/admin")
     public String dashboard_View() {
          return "/dashboard";
     }

     @GetMapping("/facilitators")
     public String facilitatorAccounts_View() {
          return "/view_facilitators";
     }

     @GetMapping("/registrars")
     public String registrarAccounts_View() {
          return "/view_registrars";
     }

     @GetMapping("/teachers")
     public String teacherAccounts_View() {
          return "/view_teachers";
     }

     @GetMapping("/students")
     public String studentAccounts_View() {
          return "/view_students";
     }
}
