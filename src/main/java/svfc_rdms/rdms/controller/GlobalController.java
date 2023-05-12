package svfc_rdms.rdms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import svfc_rdms.rdms.Enums.ValidAccounts;
import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class GlobalController {

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsServiceImpl;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private UsersRepository usersRepository;

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
          return "index";
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

     @GetMapping("/{userType}/load/userfiles")
     public ResponseEntity<Object> myDocuments(@PathVariable String userType, HttpServletResponse response,
               HttpSession session, Model model) {

          if (userType.equalsIgnoreCase("svfc-admin")) {
               userType = "school_admin";

          }

          if (session == null || session.getAttribute("username") == null) {
               return new ResponseEntity<>("You are performing invalid action, Session Empty", HttpStatus.BAD_REQUEST);
          }

          if (!globalService.validatePages(userType, response, session)) {
               return new ResponseEntity<>("You are performing invalid action, Failed to validate page",
                         HttpStatus.BAD_REQUEST);

          }

          String username = session.getAttribute("username").toString();
          Optional<Users> user = usersRepository.findByUsername(username);
          if (!user.isPresent()) {
               return new ResponseEntity<>("You are performing invalid action, User Not Found", HttpStatus.BAD_REQUEST);
          }

          // as admin
          if (userType.equalsIgnoreCase("school_admin")) {
               List<UserFiles> userFiles = fileRepository.findAll();
               List<UserFiles_Dto> userFiles_Dto = new ArrayList<>();
               for (UserFiles file : userFiles) {
                    String stringValue = file.getFileId().toString();
                    UUID uuidValue = UUID.fromString(stringValue);
                    String uploadedBy = file.getUploadedBy().getUsername() + ":"
                              + file.getUploadedBy().getName();
                    userFiles_Dto.add(new UserFiles_Dto(
                              uuidValue, file.getName(), file.getSize(),
                              file.getStatus(),
                              file.getDateUploaded(), file.getFilePurpose(), uploadedBy));
               }
               return new ResponseEntity<Object>(userFiles_Dto, HttpStatus.OK);
          }
          // none admin
          List<UserFiles> userFiles = fileRepository.findAllByUploadedBy(user.get());
          List<UserFiles_Dto> userFiles_Dto = new ArrayList<>();
          for (UserFiles file : userFiles) {
               String stringValue = file.getFileId().toString();
               UUID uuidValue = UUID.fromString(stringValue);
               String uploadedBy = file.getUploadedBy().getUsername() + ":"
                         + file.getUploadedBy().getName();
               userFiles_Dto.add(new UserFiles_Dto(
                         uuidValue, file.getName(), file.getSize(),
                         file.getStatus(),
                         file.getDateUploaded(), file.getFilePurpose(), uploadedBy));
          }
          return new ResponseEntity<Object>(userFiles_Dto, HttpStatus.OK);

     }

     @GetMapping("/{accountType}/get/file/info")
     public ResponseEntity<Object> getFileInfo(
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

          return globalService.getFileInformations(fileId);
     }
}
