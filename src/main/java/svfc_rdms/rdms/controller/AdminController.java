package svfc_rdms.rdms.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
          model.addAttribute("totalStudents", mainService.displayCountsByStatusAndType("Active", "Student"));
          model.addAttribute("totalFacilitators", mainService.displayCountsByStatusAndType("Active", "Facilitator"));
          model.addAttribute("totalRegistrars", mainService.displayCountsByStatusAndType("Active", "Registrar"));
          model.addAttribute("totalTeachers", mainService.displayCountsByStatusAndType("Active", "Teacher"));

          model.addAttribute("totalDeletedStud", mainService.displayCountsByStatusAndType("Temporary", "Teacher"));
          model.addAttribute("totalDeletedFac", mainService.displayCountsByStatusAndType("Temporary", "Student"));
          model.addAttribute("totalDeletedReg", mainService.displayCountsByStatusAndType("Temporary", "Facilitator"));
          model.addAttribute("totalDeletedTeach", mainService.displayCountsByStatusAndType("Temporary", "Registrar"));

          return "/admin-dashboard";
     }

     @GetMapping("/facilitators")
     public String facilitatorAccounts_View(Model model) {
          model.addAttribute("title", "Facilitator Accounts");
          model.addAttribute("userIdFormat", "F- / f-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Active", "Facilitator"));
          return "/admin-view_accounts";
     }

     @GetMapping("/registrars")
     public String registrarAccounts_View(Model model) {
          model.addAttribute("title", "Registrar Accounts");
          model.addAttribute("userIdFormat", "R- / r-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Active", "Registrar"));
          return "/admin-view_accounts";
     }

     @GetMapping("/teachers")
     public String teacherAccounts_View(Model model) {
          model.addAttribute("title", "Teachers Accounts");
          model.addAttribute("userIdFormat", "T- / t-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Active", "Teacher"));
          return "/admin-view_accounts";
     }

     @GetMapping("/students")
     public String studentAccounts_View(Model model) {
          model.addAttribute("title", "Students Accounts");
          model.addAttribute("userIdFormat", "C- / c-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", mainService.diplayAllAccounts("Active", "Student"));
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

     @PostMapping(value = "/saveUser")
     public String saveUser(@ModelAttribute("users") Users user, HttpSession session, Model model) {
          return saveWithRestriction(user, session, 0);

     }

     @PostMapping(value = "/updateUser")
     public String updateUser(@ModelAttribute("users") Users user, HttpSession session, Model model) {
          return saveWithRestriction(user, session, 1);

     }

     @GetMapping(value = "/user/delete/")
     @ResponseBody
     public String deleteUsers(@RequestParam("userId") long userId, HttpSession session, HttpServletRequest request) {
          String referer = request.getHeader("Referer");
          if (mainService.deleteData(userId)) {
               session.setAttribute("alertMessage", "Deleted Successfully!");
               session.setAttribute("alertType", "success");
               return "redirect:" + referer;

          } else {
               session.setAttribute("alertMessage", "Deletition Failed!");
               session.setAttribute("alertType", "error");
          }

          return "redirect:" + referer;
     }

     @GetMapping(value = "/user/{status}")
     @ResponseBody
     public String changeStatus(@PathVariable("status") String status, @RequestParam("userId") long userId,
               HttpSession session,
               HttpServletRequest request) {
          String referer = request.getHeader("Referer");

          if (!status.isEmpty() || !status.isBlank()) {
               String capitalizeS = status.substring(0, 1).toUpperCase() + status.substring(1);
               // changing status based on the input
               if (mainService.changeAccountStatus(capitalizeS, userId)) {

                    String sessionMesssage = (capitalizeS.contains("Active")) ? "Undoing Successfully."
                              : "Deleted Temporary.";
                    session.setAttribute("alertMessage", sessionMesssage);
                    session.setAttribute("alertType", "success");
                    return "redirect:" + referer;

               } else {
                    session.setAttribute("alertMessage", "Deletition Failed!");
                    session.setAttribute("alertType", "error");
               }
          } else {
               session.setAttribute("alertMessage", "Deleting / Changing Status Failed!");
               session.setAttribute("alertType", "error");
          }

          return "redirect:" + referer;
     }

     @GetMapping("/user/update")
     @ResponseBody
     public List<Users> returnUserById(@RequestParam("userId") long id, Model model) {
          model.addAttribute("users", mainService.findOneUserById(id));

          return mainService.findOneUserById(id);
     }

     public String saveWithRestriction(Users user, HttpSession session, int action) {
          String error = "";
          String endPoint = "";
          try {
               if (user.getName() == null || user.getName().length() < 1 || user.getName().isEmpty()) {
                    error = "Some value are empty, Invalid Values";
               } else if (user.getUsername() == null || user.getUsername().length() < 1
                         || user.getUsername().isEmpty()) {
                    error = "Some value are empty, Invalid Values";
               } else if (user.getPassword() == null || user.getPassword().length() < 1
                         || user.getPassword().isEmpty()) {
                    error = "Some value are empty, Invalid Values";
               } else {
                    String userIdFormat = user.getUsername().toUpperCase();

                    user.setStatus("Active");
                    if (userIdFormat.contains("C")) {
                         endPoint = "/students";
                         user.setType("Student");
                    } else if (userIdFormat.contains("F-")) {
                         endPoint = "/facilitators";
                         user.setType("Facilitator");
                    } else if (userIdFormat.contains("R-")) {
                         endPoint = "/registrars";
                         user.setType("Registrar");
                    } else if (userIdFormat.contains("T-")) {
                         endPoint = "/teachers";
                         user.setType("Teacher");
                    }

                    if (action == 0) {
                         if (mainService.findUserName(userIdFormat.toLowerCase())) {
                              error = "Username is already taken, Please try again!";
                              session.setAttribute("alertType", "error");
                              session.setAttribute("alertMessage", error);

                              return "redirect:" + endPoint + "?error=Invalid Inputs";
                         } else {
                              mainService.saveUsersAccount(user);
                              session.setAttribute("alertMessage", "Account Inserted!");
                              session.setAttribute("alertType", "success");
                              return "redirect:" + endPoint;
                         }
                    } else if (action == 1) {

                         try {
                              mainService.saveUsersAccount(user);
                              session.setAttribute("alertMessage", "Updated Successfully!");
                              session.setAttribute("alertType", "success");
                         } catch (Exception e) {
                              error = e.getMessage();
                              if (error.contains("ConstraintViolationException")) {

                                   session.setAttribute("alertType", "error");
                                   session.setAttribute("alertMessage",
                                             "Updating Failed!. Username is already taken, Please try again!");

                              }
                         }
                         return "redirect:" + endPoint;

                    }

               }
          } catch (Exception e) {
               error = e.getMessage();
               if (error.contains("ConstraintViolationException")) {

                    session.setAttribute("alertType", "error");
                    session.setAttribute("alertMessage",
                              "Updating Failed!. Username is already taken, Please try again!");

               }
          }

          return "redirect:" + endPoint;
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
