package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Registrar.Reg_RequestServiceImpl;
import svfc_rdms.rdms.serviceImpl.Registrar.Registrar_ServiceImpl;

@Controller
public class RegistrarController {

     @Autowired
     private Registrar_ServiceImpl regs_ServiceImpl;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private AdminServicesImpl mainService;

     @Autowired
     private Reg_RequestServiceImpl regs_RequestService;

     @GetMapping(value = "/registrar/dashboard")
     public String registrarDashboard(HttpSession session, HttpServletResponse response,Model model) {
          if (globalService.validatePages("registrar", response, session)) {
               model.addAttribute("page", "dashboard");
               model.addAttribute("pageTitle", "Dashboard");
               return "/registrar/reg";
          }
          return "redirect:/";
     }

     @GetMapping(value = "/registrar/{accType}/accounts")
     public String registrarAccountsView(@PathVariable String accType, HttpSession session,
               HttpServletResponse response,
               Model model) {

          if (globalService.validatePages("registrar", response, session)) {
               String accountType = accType.substring(0, 1).toUpperCase() + accType.substring(1, accType.length());
               String idFormat = "";

               try {

                    if (accountType.equals("Teacher")) {
                         accType = "Teacher";
                         idFormat = "T- / t-";
                    } else if (accountType.equals("Student")) {
                         accType = "Student";
                         idFormat = "C- / c-";
                    } else {

                         return "redirect:/";
                    }
                    model.addAttribute("page", "accview");
                    model.addAttribute("pageTitle", "Uploaded Files");
                    model.addAttribute("accType", accountType);
                    model.addAttribute("idFormat", idFormat);
                    model.addAttribute("users", new Users());
                    model.addAttribute("usersLists", mainService.diplayAllAccountsByType(accountType));
                    return "/registrar/reg";

               } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
               }

          }
          return "redirect:/";
     }

     @GetMapping(value = "/{userType}/studrequest")
     public String studentRequests(@PathVariable String userType, HttpSession session, HttpServletResponse response,
               Model model) {
          if (globalService.validatePages(userType, response, session)) {
               model.addAttribute("page", "studrequest");
               model.addAttribute("pageTitle", "Student Requests");
               return "/registrar/reg";
          }
          return "redirect:/";

     }

     @GetMapping("/registrar/my-files")
     public String displayMyFiles(HttpServletResponse response, HttpSession session, Model model) {

          if (globalService.validatePages("registrar", response, session)) {
               
               return regs_RequestService.displayAllFilesByUserId(session, model);
          }
          return "redirect:/";

     }

     @GetMapping("/registrar/documents-list")
     public String listOfDocuments(HttpServletResponse response, HttpSession session, Model model) {
          
          if (globalService.validatePages("registrar", response, session)) {
               model.addAttribute("documentsList", mainService.getAllDocuments());
               model.addAttribute("pageTitle", "Documents");
               model.addAttribute("page", "documents");
               return "/registrar/reg";
          }
          return "redirect:/";
     }

     @GetMapping("/registrar/sent-requests")
     public String viewSentRequests(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("registrar", response, session)) {
               model.addAttribute("pageTitle", "Sent Requests");
               model.addAttribute("page", "myrequest");
               return "/registrar/reg";
          }
          return "redirect:/";
     }

}
