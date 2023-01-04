package svfc_rdms.rdms.serviceImpl.Facilitator_Registrar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Facilitator_Registrar.Facilitator_Registrar_Service;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Service
public class Facilitator_Registrar_ServiceImpl implements Facilitator_Registrar_Service {

     @Autowired
     private StudentRepository studentRepository;

     @Autowired
     private DocumentRepository documentRepository;

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private StudentServiceImpl studentService;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Override
     public String displayAllStudentRequest(String userType, Model model) {
          String link = "";
          try {

               List<StudentRequest> fetchStudentRequest = studentRepository.findAll();

               if (userType.isEmpty() || userType.isBlank()) {
                    return "redirect:/";
               } else if (userType.equals("facilitator")) {
                    link = "/facilitator/facilitator-studreq-view";
               } else if (userType.equals("registrar")) {
                    link = "/registrar/registrar-studreq-view";
               } else {
                    throw new ApiRequestException("Not Found");
               }
               if (fetchStudentRequest.size() > -1) {
                    List<StudentRequest_Dto> studentRequests = new ArrayList<>();

                    fetchStudentRequest.stream().forEach(req -> {
                         if (userType.equals("facilitator")) {
                              if (req.getRequestStatus().equals("Pending") ||
                                        req.getRequestStatus().equals("Rejected")) {
                                   studentRequests
                                             .add(new StudentRequest_Dto(req.getRequestId(),
                                                       req.getRequestBy().getUserId(),
                                                       req.getRequestBy().getType(), req.getYear(),
                                                       req.getCourse(), req.getSemester(),
                                                       req.getRequestDocument().getTitle(),
                                                       req.getMessage(), req.getRequestBy().getName(),
                                                       req.getRequestDate(),
                                                       req.getRequestStatus(), req.getReleaseDate(),
                                                       req.getManageBy()));
                              }

                         } else {

                              studentRequests
                                        .add(new StudentRequest_Dto(req.getRequestId(),
                                                  req.getRequestBy().getUserId(),
                                                  req.getRequestBy().getType(), req.getYear(),
                                                  req.getCourse(), req.getSemester(),
                                                  req.getRequestDocument().getTitle(),
                                                  req.getMessage(), req.getRequestBy().getName(),
                                                  req.getRequestDate(),
                                                  req.getRequestStatus(), req.getReleaseDate(),
                                                  req.getManageBy()));

                         }
                    });
                    model.addAttribute("studentreq", studentRequests);
                    return link;
               }
               return "redirect:/";

          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

     @Override
     public boolean changeStatusAndManageByAndMessageOfRequests(String status, String manageBy, String message,
               long userId, long requestId) {
          Users user = usersRepository.findByuserId(userId).get();
          StudentRequest studentRequest = studentRepository.findOneByRequestByAndRequestId(user, requestId).get();

          if (message.equals("N/A")) {
               message = studentRequest.getMessage();
          } else {
               message = studentRequest.getMessage() + "\n" + message;
          }
          String manageByFromDatabase = globalService.removeDuplicateInManageBy(studentRequest.getManageBy());

          if (!manageByFromDatabase.trim().isEmpty()) {
               manageBy = globalService.removeDuplicateInManageBy(studentRequest.getManageBy() + "," + manageBy);
          }

          if (user != null && studentRequest != null) {
               studentRepository.changeStatusAndManagebyAndMessageOfRequests(status, manageBy,
                         message, studentRequest.getRequestId());
               return true;
          }
          return false;
     }



     @Override
     public ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
               Optional<MultipartFile[]> files, Map<String, String> params,
               HttpSession session) {
          try {
               if (files.isEmpty()) {
                    throw new ApiRequestException("Please upload file.");
               }
               LocalDateTime myDateObj = LocalDateTime.now();
               DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
               String formattedDate = myDateObj.format(myFormatObj);
               Users user = usersRepository.findByuserId(userId).get();
               StudentRequest studentRequest = studentRepository.findOneByRequestByAndRequestId(user, requestId).get();
               studentRequest.setReleaseDate(formattedDate);
               studentRequest.setRequestStatus("Approved");
               List<String> excludedFiles = new ArrayList<>();
               studentRepository.save(studentRequest);

               for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (entry.getKey().contains("excluded")) {
                         excludedFiles.add(entry.getValue());
                    }
                    System.out.println(entry.getKey() + ": " + entry.getValue());
               }
               UserFiles userFiles = new UserFiles();
               Users manageBy = usersRepository.findByUsername(session.getAttribute("username").toString()).get();

               for (MultipartFile filex : files.get()) {
                    if (!excludedFiles.contains(filex.getOriginalFilename())) {

                         userFiles.setData(filex.getBytes());
                         userFiles.setName(filex.getOriginalFilename());
                         userFiles.setSize(globalService.formatFileUploadSize(filex.getSize()));
                         userFiles.setDateUploaded(formattedDate);
                         userFiles.setStatus("Approved");
                         userFiles.setFilePurpose("dfs");
                         userFiles.setUploadedBy(manageBy);
                         userFiles.setRequestWith(studentRequest);
                         fileRepository.save(userFiles);

                    }
               }

               return new ResponseEntity<>("Success", HttpStatus.OK);

          } catch (Exception ex) {
               throw new ApiRequestException(ex.getMessage());
          }

     }


}
