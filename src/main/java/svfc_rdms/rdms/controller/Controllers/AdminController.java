package svfc_rdms.rdms.controller.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.Admin_Registrar_ManageServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class AdminController {

     @Autowired
     private AdminServicesImpl mainService;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private Admin_Registrar_ManageServiceImpl adminAccountService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsService;

     @GetMapping("/svfc-admin/dashboard")
     public String dashboard_View(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {

               model.addAttribute("page", "dashboard");
               model.addAttribute("pageTitle", "Dashboard");

               return "/svfc-admin/admin";
          }
          return "redirect:/";
     }

     @GetMapping("/svfc-admin/{userType}")
     public String accountsViews(@PathVariable("userType") String userType, HttpServletResponse response,
               HttpSession session, Model model) {

          if (globalService.validatePages("school_admin", response, session)) {
               try {

                    String accType = "";
                    String idFormat = "";
                    boolean showType = false;
                    String status = "";
                    if (userType.equals("registrars")) {
                         accType = "Registrar";
                         idFormat = "R- / r-";
                         status = "Active";
                    } else if (userType.equals("teachers")) {
                         accType = "Teacher";
                         idFormat = "T- / t-";
                         status = "Active";
                    } else if (userType.equals("students")) {
                         accType = "Student";
                         idFormat = "S- / s-";
                         status = "Active";
                    } else {

                         accType = "Invalid";
                         idFormat = "";
                         status = "";
                         showType = true;

                    }
                    model.addAttribute("page", "account_adding");
                    model.addAttribute("pageTitle", "Accounts");
                    model.addAttribute("title", accType + " Accounts");
                    model.addAttribute("userIdFormat", idFormat);
                    model.addAttribute("users", new Users());
                    model.addAttribute("hide", showType);
                    model.addAttribute("usersLists", mainService.diplayAllAccounts(status, accType));

               } catch (Exception e) {
                    return "redirect:/";
               }

               return "/svfc-admin/admin";
          }
          return "redirect:/";
     }

     // Deleted pages
     @GetMapping("/svfc-admin/{userType}/deleted-accounts")
     public String deletedAccounts_View(@PathVariable String userType, HttpServletResponse response,
               HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               model.addAttribute("page", "account_adding");
               model.addAttribute("pageTitle", "Accounts");
               String title = "";
               boolean hideToggle = false;
               String dataStatus = "Temporary";
               String accountType = userType;

               if (accountType.equals("registrar")) {
                    title = "Deleted Registrar Accounts";
                    hideToggle = true;
               } else if (accountType.equals("teacher")) {
                    title = "Deleted Teacher Accounts";
                    hideToggle = true;
               } else if (accountType.equals("student")) {
                    title = "Deleted Student Accounts";
                    hideToggle = true;
               } else {
                    hideToggle = false;

               }
               if (hideToggle) {
                    accountType = accountType.substring(1).toUpperCase() + accountType.substring(1) + "s";
                    model.addAttribute("title", title);
                    model.addAttribute("hide", hideToggle);
                    model.addAttribute("users", null);
                    model.addAttribute("usersLists", mainService.diplayAllAccounts(dataStatus, accountType));

               } else {
                    return "redirect:" + "/svfc-admin/" + userType + "/admin?error=Invalid User Type";
               }
               return "/svfc-admin/admin";
          }
          return "redirect:/";
     }

     @GetMapping("/svfc-admin/logs")
     public String viewAdminLogs(Model model, HttpServletResponse response, HttpSession session) {
          if (globalService.validatePages("school_admin", response, session)) {
               model.addAttribute("globalLogs", globalLogsService.getAllLogs());
               model.addAttribute("page", "globallogs");
               model.addAttribute("pageTitle", "Global Logs");
               return "/svfc-admin/admin";
          }
          return "redirect:/";
     }

     @GetMapping(value = "/svfc-admin/user/{status}")
     @ResponseBody
     public String changeStatus(@PathVariable("status") String status, @RequestParam("userId") long userId,

               HttpServletRequest request, HttpServletResponse response, HttpSession session) {
          if (globalService.validatePages("school_admin", response, session)) {
               String referer = request.getHeader("Referer");

               if (!status.isEmpty() || !status.isBlank()) {
                    String capitalizeS = status.substring(0, 1).toUpperCase() + status.substring(1);
                    // changing status based on the input
                    if (adminAccountService.changeAccountStatus(capitalizeS, userId, session, request)) {
                         return "redirect:" + referer;

                    }
               }

               return "redirect:" + referer;
          }
          return "redirect:/";
     }

     @GetMapping("/svfc-admin/user/update")
     @ResponseBody
     public List<Users> returnUserById(@RequestParam("userId") long id, HttpServletResponse response,
               HttpSession session, Model model) {
          List<Users> usersList = new ArrayList<>();
          if (globalService.validatePages("school_admin", response, session)) {
               Optional<Users> users = adminAccountService.findOneUserById(id);

               if (users.isPresent()) {
                    users.stream().forEach(e -> {

                         usersList.add(
                                   new Users(users.get().getUserId(), users.get().getName(), users.get().getEmail(),
                                             users.get().getUsername(),
                                             users.get().getPassword().replace(users.get()
                                                       .getPassword(), ""),
                                             users.get().getType(), users.get().getStatus(), "update"));
                    });
                    return usersList;
               }

          }
          return usersList;

     }
     // Viewing - Adding Document

     @GetMapping("/svfc-admin/documents-list")
     public String requestForAdmin(HttpServletResponse response, HttpSession session, Model model) {

          if (globalService.validatePages("school_admin", response, session)) {
               model.addAttribute("page", "documents");
               model.addAttribute("pageTitle", "Documents");
               return "/svfc-admin/admin";
          }
          return "redirect:/";
     }

     @GetMapping("/svfc-admin/global_files")
     public String allUserFiles(HttpServletResponse response, HttpSession session, Model model) {

          if (globalService.validatePages("school_admin", response, session)) {

               return mainService.displayAllUserFiles(session, model);

          }
          return "redirect:/";

     }

     @RequestMapping(value = "/svfc-admin/delete-document-card", method = RequestMethod.GET)
     public String deleteFile(@RequestParam("docid") long documentId, HttpServletResponse response, HttpSession session,
               Model model, HttpServletRequest request) {
          if (globalService.validatePages("school_admin", response, session)) {
               String message = (mainService.deleteDocumentFile(documentId, session, request)) ? "Document Deleted"
                         : "Not Deleted";
               return "redirect:/documents-list?message=" + message;
          }
          return "redirect:/";

     }

     // end documents managing

     @GetMapping("/svfc-admin/student_requests")
     public String viewAllStudentRequests(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {

               model.addAttribute("pageTitle", "Student Requests");
               model.addAttribute("page", "student_request");
               return "/svfc-admin/admin";
          }
          return "redirect:/";

     }

     @GetMapping("/svfc-admin/registrar_requests")
     public String viewAllRegistrarRequests(HttpSession session, HttpServletResponse response,
               Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               model.addAttribute("pageTitle", "Registrar Requests");
               model.addAttribute("page", "registrar_request");
               return "/svfc-admin/admin";
          }

          return "redirect:/";

     }

     @GetMapping("/svfc-admin/settings")
     public String settingViews(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               model.addAttribute("page", "settings");
               model.addAttribute("pageTitle", "Settings");
               return "/svfc-admin/admin";
          }
          return "redirect:/";

     }

}
