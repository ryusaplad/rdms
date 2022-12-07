package svfc_rdms.rdms.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.AdminRepository;
import svfc_rdms.rdms.repository.Admin_DocumentRepository;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;
import svfc_rdms.rdms.serviceImpl.StudentServiceImpl;

@Controller
public class AdminController {

     @Autowired
     MainServiceImpl mainService;

     @Autowired
     StudentServiceImpl studService;

     @Autowired
     Admin_DocumentRepository docRepo;

     @Autowired
     AdminRepository adminRepo;

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
     @GetMapping("/{userType}/deleted-accounts")
     public String deletedFacilitators_View(@PathVariable String userType, Model model) {

          String title = "";
          boolean hideToggle = false;
          String dataStatus = "Temporary";
          String accountType = userType;

          if (accountType.equals("facilitator")) {
               title = "Deleted Facilitator Accounts";
               hideToggle = true;
          } else if (accountType.equals("registrar")) {
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

     // Test Student Request
     @GetMapping("/student_requests")
     public String viewAllRequests(Model model) {
          List<StudentRequest> studentsRequest = studService.displayAllRequest();
          List<StudentRequest_Dto> storeStudentRequest = new ArrayList<>();

          for (StudentRequest studReq : studentsRequest) {
               storeStudentRequest
                         .add(new StudentRequest_Dto(studReq.getRequestId(), studReq.getRequestBy().getUserId(),
                                   studReq.getRequestBy().getType(), studReq.getYear(),
                                   studReq.getCourse(), studReq.getSemester(), studReq.getRequestDocument().getTitle(),
                                   studReq.getMessage(), studReq.getRequestBy().getName(), studReq.getRequestDate(),
                                   studReq.getRequestStatus(), studReq.getReleaseDate(), studReq.getManageBy()));

          }

          model.addAttribute("studentRequests", storeStudentRequest);

          return "/student_all_requests";
     }

     @GetMapping(value = "/admin/request/{document}")
     public String requestForm(@PathVariable String document, HttpServletRequest request, Model model) {

          try {
               if (!mainService.findDocumentByTitle(document)) {
                    return "redirect:" + "/student_request";
               }
               String description = docRepo.findByTitle(document).getDescription();
               model.addAttribute("documentType", document);
               model.addAttribute("description", description);
          } catch (Exception e) {
               System.out.println("error in global: " + e.getMessage());
          }
          return "/request-form";

     }

     @PostMapping("/admin/request/{document}/sent")
     public String sentRequest(@PathVariable String document,
               @RequestParam Map<String, String> params, Model model) {

          try {

               StudentRequest req = new StudentRequest();
               Users user = new Users();

               for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (entry.getKey().equals("userId")) {
                         user = adminRepo.findUserIdByUsername(entry.getValue());
                         req.setRequestBy(user);
                    } else if (entry.getKey().equals("year")) {
                         req.setYear(entry.getValue());
                    } else if (entry.getKey().equals("course")) {
                         req.setCourse(entry.getValue());
                    } else if (entry.getKey().equals("semester")) {
                         req.setSemester(entry.getValue());
                    }
               }
               req.setMessage("HI");
               req.setRequestDate("2022");
               req.setRequestStatus("Pending");
               req.setReleaseDate("2022");
               req.setManageBy("admin");
               if (mainService.findDocumentByTitle(document)) {
                    req.setRequestDocument(docRepo.findByTitle(document));

               }

               // if (studService.saveRequest(req)) {
               // model.addAttribute("message", "Request Successfully Sent.");
               // return "/request-form";
               // } else {
               // model.addAttribute("message", "Request Failed to sent.");
               // return "/request-form";
               // }

          } catch (Exception e) {
               model.addAttribute("message", "Request Failed to sent, Reason: " + e.getMessage());
          }
          return "redirect:/student_requests";
     }

}
