package svfc_rdms.rdms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;

@Controller
public class AdminController {

     @Autowired
     MainServiceImpl repository;

     // Get Mapping Method
     @GetMapping("/admin")
     public String dashboard_View(Model model) {
          model.addAttribute("totalStudents", repository.displayCountsByStatusAndType("Active", "Student"));
          model.addAttribute("totalFacilitators", repository.displayCountsByStatusAndType("Active", "Facilitator"));
          model.addAttribute("totalRegistrars", repository.displayCountsByStatusAndType("Active", "Registrar"));
          model.addAttribute("totalTeachers", repository.displayCountsByStatusAndType("Active", "Teacher"));

          model.addAttribute("totalDeletedStud", repository.displayCountsByStatusAndType("Temporary", "Teacher"));
          model.addAttribute("totalDeletedFac", repository.displayCountsByStatusAndType("Temporary", "Student"));
          model.addAttribute("totalDeletedReg", repository.displayCountsByStatusAndType("Temporary", "Facilitator"));
          model.addAttribute("totalDeletedTeach", repository.displayCountsByStatusAndType("Temporary", "Registrar"));

          return "/dashboard";
     }

     @GetMapping("/facilitators")
     public String facilitatorAccounts_View(Model model) {
          model.addAttribute("title", "Facilitator Accounts");
          model.addAttribute("userIdFormat", "F- / f-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Active", "Facilitator"));
          return "/view_accounts";
     }

     @GetMapping("/registrars")
     public String registrarAccounts_View(Model model) {
          model.addAttribute("title", "Registrar Accounts");
          model.addAttribute("userIdFormat", "R- / r-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Active", "Registrar"));
          return "/view_accounts";
     }

     @GetMapping("/teachers")
     public String teacherAccounts_View(Model model) {
          model.addAttribute("title", "Teachers Accounts");
          model.addAttribute("userIdFormat", "T- / t-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Active", "Teacher"));
          return "/view_accounts";
     }

     @GetMapping("/students")
     public String studentAccounts_View(Model model) {
          model.addAttribute("title", "Students Accounts");
          model.addAttribute("userIdFormat", "C- / c-");
          model.addAttribute("users", new Users());
          model.addAttribute("hide", false);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Active", "Student"));
          return "/view_accounts";
     }

     // Deleted pages
     @GetMapping("/facilitators-deleted")
     public String deletedFacilitators_View(Model model) {
          model.addAttribute("title", "Deleted Facilitator Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Temporary", "Facilitator"));
          return "/view_accounts";
     }

     @GetMapping("/registrars-deleted")
     public String deletedRegistrars_View(Model model) {
          model.addAttribute("title", "Deleted Registrar Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Temporary", "Registrar"));
          return "/view_accounts";
     }

     @GetMapping("/teachers-deleted")
     public String deletedTeachers_View(Model model) {
          model.addAttribute("title", "Deleted Teachers Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Temporary", "Teacher"));
          return "/view_accounts";
     }

     @GetMapping("/students-deleted")
     public String deletedStudents_View(Model model) {
          model.addAttribute("title", "Deleted Students Accounts");
          model.addAttribute("hide", true);
          model.addAttribute("users", null);
          model.addAttribute("usersLists", repository.diplayAllAccounts("Temporary", "Student"));
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
          if (repository.deleteData(userId)) {
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
               if (repository.changeAccountStatus(capitalizeS, userId)) {

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
          model.addAttribute("users", repository.findOneUserById(id));

          return repository.findOneUserById(id);
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
                         if (repository.findUserName(userIdFormat.toLowerCase())) {
                              error = "Username is already taken, Please try again!";
                              session.setAttribute("alertType", "error");
                              session.setAttribute("alertMessage", error);

                              return "redirect:" + endPoint + "?error=Invalid Inputs";
                         } else {
                              repository.saveUsersAccount(user);
                              session.setAttribute("alertMessage", "Account Inserted!");
                              session.setAttribute("alertType", "success");
                              return "redirect:" + endPoint;
                         }
                    } else if (action == 1) {

                         try {
                              repository.saveUsersAccount(user);
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

}
