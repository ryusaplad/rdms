package svfc_rdms.rdms.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;

@Controller
public class AdminController {

     @Autowired
     MainServiceImpl mainService;

     // Get Mapping Method
     @GetMapping("/admin")
     public String dashboard_View(Model model) {
          model.addAttribute("totalStudents", mainService.displayCountsByStatusAndType("Active", "Students"));
          model.addAttribute("totalFacilitators", mainService.displayCountsByStatusAndType("Active", "Facilitators"));
          model.addAttribute("totalRegistrars", mainService.displayCountsByStatusAndType("Active", "Registrars"));
          model.addAttribute("totalTeachers", mainService.displayCountsByStatusAndType("Active", "Teachers"));

          model.addAttribute("totalDeletedStud", mainService.displayCountsByStatusAndType("Temporary", "Teachers"));
          model.addAttribute("totalDeletedFac", mainService.displayCountsByStatusAndType("Temporary", "Students"));
          model.addAttribute("totalDeletedReg", mainService.displayCountsByStatusAndType("Temporary", "Facilitators"));
          model.addAttribute("totalDeletedTeach", mainService.displayCountsByStatusAndType("Temporary", "Registrars"));

          return "/admin-dashboard";
     }

     @GetMapping("/{userType}")
     public String accountsViews(@PathVariable("userType") String userType, Model model) {
          try {

               String accType = "";
               String idFormat = "";
               boolean showType = false;
               String status = "";
               if (userType.equals("facilitators")) {
                    accType = "Facilitator";
                    idFormat = "F- / f-";
                    status = "Active";
               } else if (userType.equals("registrars")) {
                    accType = "Registrar";
                    idFormat = "R- / r-";
                    status = "Active";
               } else if (userType.equals("teachers")) {
                    accType = "Teacher";
                    idFormat = "T- / t-";
                    status = "Active";
               } else if (userType.equals("students")) {
                    accType = "Student";
                    idFormat = "C- / c-";
                    status = "Active";
               } else {

                    accType = "Invalid";
                    idFormat = "";
                    status = "";
                    showType = true;

               }
               model.addAttribute("title", accType + " Accounts");
               model.addAttribute("userIdFormat", idFormat);
               model.addAttribute("users", new Users());
               model.addAttribute("hide", showType);
               model.addAttribute("usersLists", mainService.diplayAllAccounts(status, accType));

          } catch (Exception e) {
               System.out.println("Error: " + e.getMessage());
          }

          return "/admin-view_accounts";
     }

     // Deleted pages
     @GetMapping("/facilitators-deleted")
     public String deletedFacilitators_View(Model model) {
          model.addAttribute("title", "Deleted Facilitator Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Temporary", "Facilitator"));
          return "/admin-view_accounts";
     }

     @GetMapping("/registrars-deleted")
     public String deletedRegistrars_View(Model model) {
          model.addAttribute("title", "Deleted Registrar Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Temporary", "Registrar"));
          return "/admin-view_accounts";
     }

     @GetMapping("/teachers-deleted")
     public String deletedTeachers_View(Model model) {
          model.addAttribute("title", "Deleted Teachers Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Temporary", "Teacher"));
          return "/admin-view_accounts";
     }

     @GetMapping("/students-deleted")
     public String deletedStudents_View(Model model) {
          model.addAttribute("title", "Deleted Students Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Temporary", "Student"));
          return "/admin-view_accounts";
     }

     @GetMapping("/all_request")
     public String viewAllRequests() {
          return "/admin-all_requests";
     }

     @GetMapping("/admin_logs")
     public String viewAdminLogs() {
          return "/admin_logs";
     }

     @GetMapping(value = "/user/delete/")
     @ResponseBody
     public String deleteUsers(@RequestParam("userId") long userId, HttpServletRequest request) {
          String referer = request.getHeader("Referer");
          if (mainService.deleteData(userId)) {
               return "redirect:" + referer;
          } else {
          }
          return "redirect:" + referer;
     }

     @GetMapping(value = "/user/{status}")
     @ResponseBody
     public String changeStatus(@PathVariable("status") String status, @RequestParam("userId") long userId,

               HttpServletRequest request) {
          String referer = request.getHeader("Referer");

          if (!status.isEmpty() || !status.isBlank()) {
               String capitalizeS = status.substring(0, 1).toUpperCase() + status.substring(1);
               // changing status based on the input
               if (mainService.changeAccountStatus(capitalizeS, userId)) {

                    String sessionMesssage = (capitalizeS.contains("Active")) ? "Undoing Successfully."
                              : "Deleted Temporary.";

                    return "redirect:" + referer;

               }
          }

          return "redirect:" + referer;
     }

     @GetMapping("/user/update")
     @ResponseBody
     public List<Users> returnUserById(@RequestParam("userId") long id, Model model) {
          model.addAttribute("users", mainService.findOneUserById(id));
          return mainService.findOneUserById(id);
     }
     // Viewing - Adding Document

     @GetMapping("/admin-request")
     public String requestForAdmin(Model model) {
          model.addAttribute("documents", mainService.getAllFiles());
          return "/admin-request_cards";
     }

     @GetMapping(value = "add-documents")
     public String addDocumentForm(Model model) {

          model.addAttribute("documents", new Documents());
          return "/admin-add-document";
     }

     @RequestMapping(path = "/save-document-info", method = RequestMethod.POST, consumes = {
               MediaType.MULTIPART_FORM_DATA_VALUE })
     public String saveFile(@RequestParam("image") MultipartFile partFile, @RequestParam Map<String, String> params,
               Model model) {
          String resonseMessage = "";

          if (partFile.getSize() > 0 && params != null) {
               mainService.saveDocumentData(partFile, params);
               resonseMessage = "File Uploaded";
          } else {
               resonseMessage = "Failed to upload file!";

          }
          return "redirect:/admin-request?message=" + resonseMessage;
     }

     @RequestMapping(value = "/delete-document-card", method = RequestMethod.GET)
     public String deleteFile(@RequestParam("docid") long documentId, Model model) {
          String message = (mainService.deleteFile(documentId)) ? "Document Deleted" : "Not Deleted";
          return "redirect:/admin-request?message=" + message;
     }

     @GetMapping("/image")
     public void showImage(@Param("documentId") long id, HttpServletResponse response,
               Optional<Documents> dOptional) {

          dOptional = mainService.getFileById(id);
          try {
               response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf");
               response.getOutputStream().write(dOptional.get().getImage());
               response.getOutputStream().close();
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }
     }

}
