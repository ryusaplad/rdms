package svfc_rdms.rdms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.ValidAccounts;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class GlobalController {

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @GetMapping(value = "/")
     public String loginPage(HttpSession session, HttpServletResponse response,
               Model model) {
          // Set the Cache-Control, Pragma, and Expires headers to prevent caching of the
          // login page
          String accType = "";
          ValidAccounts[] validAccountType = ValidAccounts.values();

          if (session.getAttribute("accountType") != null) {

               for (ValidAccounts validAcc : validAccountType) {
                    if (String.valueOf(validAcc).toLowerCase().contains(session.getAttribute("accountType").toString()
                              .toLowerCase())) {
                         accType = validAcc.toString().toLowerCase();
                         break;
                    }
               }
               if (accType.contains("admin")) {
                    accType = "admin";
               }

               return "redirect:/" + accType + "/dashboard";
          }

          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          response.setDateHeader("Expires", 0);
          return "/index";
     }

     @GetMapping(value = "/logout")
     public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
          session.removeAttribute("studentName");
          session.removeAttribute("accountType");
          session.removeAttribute("username");
          session.removeAttribute("name");

          if (session.getAttribute("session_remember") != null) {
               if (session.getAttribute("session_remember").equals("false")) {
                    session.invalidate();

               } else {
                    System.out.println(session.getAttribute("session_remember").toString());
               }
          }

          return "redirect:/";
     }

     @GetMapping("/{acctType}/files/download")
     public void downloadFile(@PathVariable String acctType, @Param("id") String id, Model model,
               HttpServletResponse response, HttpSession session) {
          try {
               String accType = "";
               ValidAccounts[] validAccountType = ValidAccounts.values();

               if (session.getAttribute("accountType") != null) {

                    for (ValidAccounts validAcc : validAccountType) {
                         if (String.valueOf(validAcc).toLowerCase()
                                   .contains(acctType)) {
                              accType = validAcc.toString().toLowerCase();
                              break;
                         }
                    }
                    if (globalService.validatePages(accType, response, session)) {
                         globalService.DownloadFile(id, model, response);
                    }
               }

               response.sendRedirect("/");
          } catch (Exception e) {

               throw new ApiRequestException(e.getMessage());

          }
     }

}
