package svfc_rdms.rdms.controller.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class AdminController {

     @Autowired
     AdminServicesImpl mainService;

     @Autowired
     StudentServiceImpl studService;

     @Autowired
     DocumentRepository docRepo;

     // Get Mapping Method
     @GetMapping("/admin")
     public String dashboard_View(Model model) {
          model.addAttribute("totalStudents", mainService.displayCountsByStatusAndType("Active", "Student"));
          model.addAttribute("totalRegistrars", mainService.displayCountsByStatusAndType("Active", "Registrar"));
          model.addAttribute("totalTeachers", mainService.displayCountsByStatusAndType("Active", "Teacher"));

          model.addAttribute("totalDeletedStud", mainService.displayCountsByStatusAndType("Temporary", "Student"));
          model.addAttribute("totalDeletedReg", mainService.displayCountsByStatusAndType("Temporary", "Registrar"));
          model.addAttribute("totalDeletedTeach", mainService.displayCountsByStatusAndType("Temporary", "Teacher"));

          return "/admin-dashboard";
     }

     @GetMapping("/{userType}")
     public String accountsViews(@PathVariable("userType") String userType, Model model) {
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
               System.out.println("Error: " + e.getMessage());
          }

          return "/admin-view_accounts";
     }

     // Deleted pages
     @GetMapping("/{userType}/deleted-accounts")
     public String deletedAccounts_View(@PathVariable String userType, Model model) {

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
               return "redirect:" + "/admin-view_accounts?error=Invalid User Type";
          }
          return "/admin-view_accounts";
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
                    return "redirect:" + referer;

               }
          }

          return "redirect:" + referer;
     }

     @GetMapping("/user/update")
     @ResponseBody
     public List<Users> returnUserById(@RequestParam("userId") long id, Model model) {

          Optional<Users> users = mainService.findOneUserById(id);

          List<Users> usersList = new ArrayList<>();
          if (users.isPresent()) {
               users.stream().forEach(e -> {

                    usersList.add(new Users(users.get().getUserId(), users.get().getName(), users.get().getUsername(),
                              users.get().getPassword().replace(users.get()
                                        .getPassword(), ""),
                              users.get().getType(), users.get().getStatus()));
               });
               return usersList;
          }
          return usersList;

     }
     // Viewing - Adding Document

     @GetMapping("/admin-request")
     public String requestForAdmin(Model model) {
          model.addAttribute("documentsList", mainService.getAllDocuments());
          return "/admin-request_cards";
     }

     @RequestMapping(value = "/delete-document-card", method = RequestMethod.GET)
     public String deleteFile(@RequestParam("docid") long documentId, Model model) {
          String message = (mainService.deleteDocumentFile(documentId)) ? "Document Deleted" : "Not Deleted";
          return "redirect:/admin-request?message=" + message;
     }

     @GetMapping("/image")
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

     @GetMapping("/admin/documents/downloadfile")
     public void downloadFile(@Param("id") String id, Model model, HttpServletResponse response) throws IOException {
          Optional<UserFiles> temp = mainService.getFileById(id);
          if (temp != null) {
               UserFiles file = temp.get();
               response.setContentType("application/octet-stream");
               String headerKey = "Content-Disposition";
               String headerValue = "attachment; filename = " + file.getName();
               response.setHeader(headerKey, headerValue);
               ServletOutputStream outputStream = response.getOutputStream();
               outputStream.write(file.getData());
               outputStream.close();
          }
     }

     // Test Student Request
     @GetMapping("/student_requests")
     public String viewAllRequests(Model model) {
          List<StudentRequest> studentsRequest = mainService.displayAllRequest();
          List<StudentRequest_Dto> storeStudentRequest = new ArrayList<>();

          for (StudentRequest studReq : studentsRequest) {
               storeStudentRequest
                         .add(new StudentRequest_Dto(studReq.getRequestId(), studReq.getRequestBy().getUserId(),
                                   studReq.getRequestBy().getType(), studReq.getYear(),
                                   studReq.getCourse(), studReq.getSemester(), studReq.getRequestDocument().getTitle(),
                                   studReq.getMessage(), studReq.getReply(), studReq.getRequestBy().getName(),
                                   studReq.getRequestDate(),
                                   studReq.getRequestStatus(), studReq.getReleaseDate(), studReq.getManageBy()));

          }

          model.addAttribute("studentRequests", storeStudentRequest);

          return "/student_all_requests";
     }

}
