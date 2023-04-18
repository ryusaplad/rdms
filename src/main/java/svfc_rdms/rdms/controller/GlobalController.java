package svfc_rdms.rdms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import svfc_rdms.rdms.Enums.ValidAccounts;
import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class GlobalController {

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsServiceImpl;

     @Autowired
     private AdminServicesImpl mainService;

     @GetMapping(value = "/")
     public String loginPage(HttpSession session, HttpServletResponse response, HttpServletRequest request,
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
                    accType = "svfc-admin";
               }

               return "redirect:/" + accType + "/dashboard";
          }

          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          response.setDateHeader("Expires", 0);

          Cookie[] cookies = request.getCookies();

          if (cookies != null) {
               for (Cookie cookie : cookies) {

                    if (cookie.getName().equals("login_MyUsername")) {
                         model.addAttribute("login_MyUsername", cookie.getValue());
                    } else if (cookie.getName().equals("login_MyPassword")) {
                         model.addAttribute("login_MyPassword", cookie.getValue());
                    } else if (cookie.getName().equals("login_MyAccountType")) {
                         model.addAttribute("login_MyAccountType", cookie.getValue());
                    } else if (cookie.getName().equals("login_RememberMe")) {
                         model.addAttribute("login_RememberMe", cookie.getValue());
                    }
               }
          }
          return "/index";
     }

     @GetMapping(value = "/logout")
     public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
          if (session.getAttribute("name") != null && session.getAttribute("username") != null && !session.isNew()) {
               String date = LocalDateTime.now().toString();
               String logMessage = "User Logged Out: " + session.getAttribute("name") + ":"
                         + session.getAttribute("username") + " has logged out";
               globalLogsServiceImpl.saveLog(0, logMessage, "Logout_Log", date, "low", session, request);
               clearSessionAttributes(session);
               session.invalidate();
          }
          return "redirect:/";
     }

     private void clearSessionAttributes(HttpSession session) {
          session.removeAttribute("accountType");
          session.removeAttribute("username");
          session.removeAttribute("name");
          session.removeAttribute("email");
     }

     @GetMapping("/{acctType}/files/download")
     public void downloadFile(@PathVariable String acctType, @Param("id") String id, Model model,
               HttpServletResponse response, HttpSession session) {
          try {
               String accType = "";
               ValidAccounts[] validAccountType = ValidAccounts.values();

               if (session.getAttribute("accountType") != null) {

                    if (acctType.equalsIgnoreCase("svfc-admin")) {
                         acctType = "admin";
                    }

                    for (ValidAccounts validAcc : validAccountType) {
                         if (String.valueOf(validAcc).toLowerCase()
                                   .contains(acctType)) {
                              accType = validAcc.toString().toLowerCase();
                              break;
                         }
                    }
                    if (globalService.validatePages(accType, response, session)) {
                         globalService.downloadFile(id, model, response);
                    }
               } else {
                    response.sendRedirect("/");
               }

          } catch (Exception e) {

               try {
                    response.sendRedirect("/");
               } catch (Exception exception) {

               }

          }

     }

     @GetMapping("/{accountType}/delete/file")
     public ResponseEntity<String> deleteFile(
               @PathVariable String accountType,
               @RequestParam("id") String fileId,
               HttpServletResponse response,
               HttpSession session) throws IOException {

          if (session == null) {
               throw new ApiRequestException("Invalid session or service");
          }

          if (accountType.equalsIgnoreCase("svfc-admin")) {
               accountType = "school_admin";
          }

          if (!globalService.validatePages(accountType, response, session)) {
               throw new ApiRequestException("Invalid session requests");
          }

          globalService.deleteFile(fileId);
          return ResponseEntity.ok("File deleted");
     }

     @GetMapping("/{userType}/load/image")
     public void showImage(@PathVariable("userType") String userType, @Param("documentId") long id,
               HttpServletResponse response, HttpSession session,
               Optional<Documents> dOptional) {
          if (userType.equalsIgnoreCase("svfc-admin") || userType.equalsIgnoreCase("registrar")) {
               if (userType.equalsIgnoreCase("svfc-admin")) {
                    userType = "school_admin";
               }
               if (globalService.validatePages(userType, response, session)) {
                    dOptional = mainService.getFileDocumentById(id);

                    try {
                         response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf");
                         response.getOutputStream().write(dOptional.get().getImage());
                         response.getOutputStream().close();
                    } catch (Exception e) {
                         System.out.println(e.getMessage());
                    }
               }

          }

     }

}
