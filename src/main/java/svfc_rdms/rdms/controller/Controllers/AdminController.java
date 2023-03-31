package svfc_rdms.rdms.controller.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import svfc_rdms.rdms.dto.RegistrarRequest_DTO;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegsRequestRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.Admin_Registrar_ManageAccountServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Controller
public class AdminController {

     @Autowired
     private AdminServicesImpl mainService;

     @Autowired
     private DocumentRepository docRepo;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private RegsRequestRepository registrarRepo;

     @Autowired
     private Admin_Registrar_ManageAccountServiceImpl adminAccountService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsService;

     // Get Mapping Method
     @GetMapping("/admin/dashboard")
     public String dashboard_View(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               model.addAttribute("totalStudents", mainService.displayCountsByStatusAndType("Active", "Student"));
               model.addAttribute("totalRegistrars", mainService.displayCountsByStatusAndType("Active", "Registrar"));
               model.addAttribute("totalTeachers", mainService.displayCountsByStatusAndType("Active", "Teacher"));

               model.addAttribute("totalDeletedStud", mainService.displayCountsByStatusAndType("Temporary", "Student"));
               model.addAttribute("totalDeletedReg",
                         mainService.displayCountsByStatusAndType("Temporary", "Registrar"));
               model.addAttribute("totalDeletedTeach",
                         mainService.displayCountsByStatusAndType("Temporary", "Teacher"));

               return "/admin/admin_dashboard";
          }
          return "redirect:/";
     }

     @GetMapping("/admin/{userType}")
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
                    return "redirect:/";
               }

               return "/admin/admin_view_accounts";
          }
          return "redirect:/";
     }

     // Deleted pages
     @GetMapping("/admin/{userType}/deleted-accounts")
     public String deletedAccounts_View(@PathVariable String userType, HttpServletResponse response,
               HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
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
                    return "redirect:" + "/admin/" + userType + "/deleted-accounts?error=Invalid User Type";
               }
               return "/admin/admin_view_accounts";
          }
          return "redirect:/";
     }

     @GetMapping("/admin/logs")
     public String viewAdminLogs(Model model) {
          model.addAttribute("globalLogs",globalLogsService.getAllLogs());
          return "/admin/admin_global_logs";
     }

     @GetMapping(value = "/admin/user/{status}")
     @ResponseBody
     public String changeStatus(@PathVariable("status") String status, @RequestParam("userId") long userId,

               HttpServletRequest request, HttpServletResponse response, HttpSession session) {
          if (globalService.validatePages("school_admin", response, session)) {
               String referer = request.getHeader("Referer");

               if (!status.isEmpty() || !status.isBlank()) {
                    String capitalizeS = status.substring(0, 1).toUpperCase() + status.substring(1);
                    // changing status based on the input
                    if (adminAccountService.changeAccountStatus(capitalizeS, userId, session)) {
                         return "redirect:" + referer;

                    }
               }

               return "redirect:" + referer;
          }
          return "redirect:/";
     }

     @GetMapping("/admin/user/update")
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

     @GetMapping("/admin/documents-list")
     public String requestForAdmin(HttpServletResponse response, HttpSession session, Model model) {
          model.addAttribute("documentsList", mainService.getAllDocuments());
          if (globalService.validatePages("school_admin", response, session)) {
               return "/admin/admin_requests_cards";
          }
          return "redirect:/";
     }

     @RequestMapping(value = "/admin/delete-document-card", method = RequestMethod.GET)
     public String deleteFile(@RequestParam("docid") long documentId, HttpServletResponse response, HttpSession session,
               Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               String message = (mainService.deleteDocumentFile(documentId, session)) ? "Document Deleted"
                         : "Not Deleted";
               return "redirect:/documents-list?message=" + message;
          }
          return "redirect:/";

     }

     @GetMapping("/admin/image")
     public void showImage(@Param("documentId") long id, HttpServletResponse response,
               Optional<Documents> dOptional) {

          dOptional = mainService.getFileDocumentById(id);
          try {
               response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf");
               response.getOutputStream().write(dOptional.get().getImage());
               response.getOutputStream().close();
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }
     }

     // end documents managing

     // Test Student Request
     @GetMapping("/admin/student_requests")
     public String viewAllStudentRequests(HttpServletResponse response, HttpSession session, Model model) {
          List<StudentRequest> studentsRequest = mainService.displayAllRequest();
          List<StudentRequest_Dto> storeStudentRequest = new ArrayList<>();
          if (globalService.validatePages("school_admin", response, session)) {
               for (StudentRequest studReq : studentsRequest) {
                    storeStudentRequest
                              .add(new StudentRequest_Dto(studReq.getRequestId(), studReq.getRequestBy().getUserId(),
                                        studReq.getRequestBy().getType(), studReq.getYear(),
                                        studReq.getCourse(), studReq.getSemester(),
                                        studReq.getRequestDocument().getTitle(),
                                        studReq.getMessage(), studReq.getReply(), studReq.getRequestBy().getName(),
                                        studReq.getRequestDate(),
                                        studReq.getRequestStatus(), studReq.getReleaseDate(), studReq.getManageBy()));

               }

               model.addAttribute("studentRequests", storeStudentRequest);

               return "/admin/admin_student_all_requests";
          }
          return "redirect:/";

     }

     @GetMapping("/admin/registrar_requests")
     public String viewAllRegistrarRequests(HttpSession session, HttpServletResponse response,
               Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               List<RegistrarRequest_DTO> filteredRequests = new ArrayList<>();
               List<RegistrarRequest> regRequests = registrarRepo.findAll();
               if (regRequests != null) {
                    regRequests.forEach(request -> {

                         filteredRequests.add(
                                   new RegistrarRequest_DTO(request.getRequestId(), request.getRequestTitle(),
                                             request.getRequestMessage(), request.getTeacherMessage(),
                                             request.getRequestBy().getName(),
                                             request.getRequestTo().getName(), request.getRequestDate(),
                                             request.getDateOfUpdate(),
                                             request.getRequestStatus()));

                    });

                    model.addAttribute("registrar_requests", filteredRequests);
                    return "/admin/admin_registrar_all_requests";
               }
          }
          return "redirect:/";

     }

     @GetMapping("/admin/settings")
     public String settingViews(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("school_admin", response, session)) {
               return "/admin/admin_settings";
          }
          return "redirect:/";

     }

}
