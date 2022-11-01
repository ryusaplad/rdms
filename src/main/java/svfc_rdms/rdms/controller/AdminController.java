package svfc_rdms.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

     // Get Mapping Method
     @GetMapping("/admin")
     public String dashboard_View() {
          return "/dashboard";
     }

     @GetMapping("/facilitators")
     public String facilitatorAccounts_View(Model model) {
          model.addAttribute("title", "Facilitator Accounts");
          return "/view_accounts";
     }

     @GetMapping("/registrars")
     public String registrarAccounts_View(Model model) {
          model.addAttribute("title", "Registrar Accounts");

          return "/view_accounts";
     }

     @GetMapping("/teachers")
     public String teacherAccounts_View(Model model) {
          model.addAttribute("title", "Teachers Accounts");

          return "/view_accounts";
     }

     @GetMapping("/students")
     public String studentAccounts_View(Model model) {
          model.addAttribute("title", "Students Accounts");

          return "/view_accounts";
     }

     @GetMapping("/all_request")
     public String viewAllRequests() {
          return "/all_requests";
     }

     @GetMapping("/admin-request")
     public String requestForAdmin() {
          return "/request_cards";
     }

     @GetMapping("/admin_logs")
     public String viewAdminLogs() {
          return "/admin_logs";
     }
}
