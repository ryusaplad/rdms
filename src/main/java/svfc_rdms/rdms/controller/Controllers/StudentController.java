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

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class StudentController {

     @Autowired
     private AdminServicesImpl mainService;
     @Autowired
     private StudentServiceImpl studService;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private DocumentRepository docRepo;

     @Autowired
     private StudentRepository studRepo;

     @Autowired
     private UsersRepository userRepo;

     @GetMapping(value = "/student/dashboard")
     public String studentDashboard(HttpSession session, HttpServletResponse response) {

          if (globalService.validatePages("student", response, session)) {

               return "/student/stud";
          }
          return "redirect:/";
     }

     @GetMapping("/student/request/documents")
     public String fetchDocumentCards(HttpSession session, HttpServletResponse response, Model model) {

          if (globalService.validatePages("student", response, session)) {

               List<Documents> documentList = mainService.getAllDocuments();
               model.addAttribute("documentsCards", documentList);
               return "/student/student-request-cards";
          }
          return "redirect:/";
     }

     @GetMapping("/student/my-requests")
     public String listOfStudentRequest(HttpServletResponse response, HttpSession session, Model model) {
          if (globalService.validatePages("student", response, session)) {
               return studService.displayStudentRequests(model, session);
          }
          return null;

     }

     @GetMapping("/student/my-documents")
     public String listOfDocuments(HttpServletResponse response, HttpSession session, Model model) {

          if (globalService.validatePages("student", response, session)) {

               Users user = userRepo.findUserIdByUsername(session.getAttribute("username").toString()).get();
               List<UserFiles> getAllFiles = studService.getAllFilesByUser(user.getUserId());
               List<UserFiles_Dto> userFiles = new ArrayList<>();

               if (getAllFiles.size() < 0) {
                    model.addAttribute("files", userFiles);
                    return "/student/student-documents-list";
               }
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
          globalService.validatePages("student", response, session);
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
     public void downloadFile(@Param("id") String id, Model model, HttpServletResponse response, HttpSession session) {
          try {
               if (globalService.validatePages("student", response, session)) {
                    studService.student_DownloadFile(id, model, response);
            }
            response.sendRedirect("/");
       } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
       }
     }


}
