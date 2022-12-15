package svfc_rdms.rdms.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

     @GetMapping(value = "/")
     public String loginPage(HttpSession session, HttpServletResponse response) {
          // Set the Cache-Control, Pragma, and Expires headers to prevent caching of the
          // login page
          String accType = "";
          if (session.getAttribute("accountType") != null && session.getAttribute("accountType").equals("Student")) {
               accType = session.getAttribute("accountType").toString().toLowerCase();
               return "redirect:/" + accType + "/dashboard";
          }
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          response.setDateHeader("Expires", 0);
          return "/index";
     }

     @GetMapping(value = "/logout")
     public String logout(HttpSession session) {
          session.removeAttribute("studentName");
          session.removeAttribute("accountType");
          session.removeAttribute("username");
          session.invalidate();

          return "redirect:/";
     }

}
