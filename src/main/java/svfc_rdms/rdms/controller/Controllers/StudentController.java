package svfc_rdms.rdms.controller.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class StudentController {

     @Autowired
     AdminServicesImpl mainService;
     @Autowired
     StudentServiceImpl studService;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     StudentRepository studRepo;

     @Autowired
     UsersRepository userRepo;

     @GetMapping(value = "/student/dashboard")
     public String studentDashboard(HttpSession session, HttpServletResponse response) {
          if (validatePages(response, session)) {

               return "/student/stud";
          }
          return null;
     }

     @GetMapping("/student/request/documents")
     public String fetchDocumentCards(HttpSession session, HttpServletResponse response, Model model) {

          if (validatePages(response, session)) {

               List<Documents> documentList = mainService.getAllDocuments();
               model.addAttribute("documentsCards", documentList);
               return "/student/student-request-cards";
          }
          return null;
     }

     @GetMapping("/student/my-requests")
     public String listOfStudentRequest(HttpServletResponse response, HttpSession session, Model model) {
          if (validatePages(response, session)) {
               return studService.displayStudentRequests(model, session);
          }
          return null;

     }

     @GetMapping("/student/my-documents")
     public String listOfDocuments(HttpServletResponse response, HttpSession session, Model model) {
          if (validatePages(response, session) == true) {
               System.out.println("validate");
               Users user = userRepo.findUserIdByUsername(session.getAttribute("username").toString()).get();
               List<UserFiles> getAllFiles = studService.getAllFilesByUser(user.getUserId());
               List<UserFiles_Dto> userFiles = new ArrayList<>();
               getAllFiles.stream().forEach(file -> {
                    String stringValue = file.getFileId().toString();
                    UUID uuidValue = UUID.fromString(stringValue);
                    String uploadedBy = file.getUploadedBy().getUsername() + ":"
                              + file.getUploadedBy().getName();
                    userFiles.add(new UserFiles_Dto(
                              uuidValue, file.getName(), file.getSize(),
                              file.getStatus(),
                              file.getDateUploaded(), file.getFilePurpose(), uploadedBy));
               });
               model.addAttribute("files", userFiles);
               return "/student/student-documents-list";

          }
          return null;
     }

     @GetMapping(value = "/student/request/{document}")
     public String requestForm(@PathVariable String document, HttpServletRequest request, HttpServletResponse response,
               HttpSession session, Model model) {
          validatePages(response, session);
          try {
               if (studService.findDocumentByTitle(document).isEmpty()) {
                    return "redirect:" + "/student/request/documents";
               }
               String description = docRepo.findByTitle(document).get().getDescription();

               model.addAttribute("documentType", document);
               model.addAttribute("description", description);
               return "/student/student-request-form";
          } catch (Exception e) {
               e.printStackTrace();
          }

          return null;

     }

     @GetMapping("/student/documents/image")
     public void showImage(@Param("documentId") long id, HttpServletResponse response,
               Optional<Documents> dOptional) {
          studService.student_showImageFiles(id, response, dOptional);

     }

     @GetMapping("/student/files/download")
     public void downloadFile(@Param("id") String id, Model model, HttpServletResponse response) {

          studService.student_DownloadFile(id, model, response);
     }

     // public void setSessionDefaultValue(String variable, String compareTo, String
     // newValue, HttpSession session) {
     // if (session.getAttribute(variable) == compareTo) {
     // session.setAttribute(variable, newValue);
     // }
     // }

     public boolean validatePages(HttpServletResponse response, HttpSession session) {
          try {
               response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
               response.setHeader("Pragma", "no-cache");
               response.setDateHeader("Expires", 0);
               if (session.getAttribute("username") == null ||
                         session.getAttribute("accountType") == null
                         || session.getAttribute("name") == null) {
                    // If the session is not valid, redirect to the login page
                    response.sendRedirect("/");

               } else {
                    return true;
               }

          } catch (Exception e) {
               e.printStackTrace();
          }
          return false;
     }
}
