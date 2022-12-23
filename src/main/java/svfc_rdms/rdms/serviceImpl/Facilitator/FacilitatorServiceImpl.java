package svfc_rdms.rdms.serviceImpl.Facilitator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Facilitator.FacilitatorService;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Service
public class FacilitatorServiceImpl implements FacilitatorService {

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

     @Override
     public String displayAllStudentRequest(Model model) {
          try {

               List<StudentRequest> fetchStudentRequest = studentRepository.findAll();
               if (fetchStudentRequest.size() > -1) {
                    List<StudentRequest_Dto> studentRequests = new ArrayList<>();

                    fetchStudentRequest.stream().forEach(req -> {
                         if (req.getRequestStatus().equals("Pending") || req.getRequestStatus().equals("Rejected")) {
                              studentRequests
                                        .add(new StudentRequest_Dto(req.getRequestId(), req.getRequestBy().getUserId(),
                                                  req.getRequestBy().getType(), req.getYear(),
                                                  req.getCourse(), req.getSemester(),
                                                  req.getRequestDocument().getTitle(),
                                                  req.getMessage(), req.getRequestBy().getName(), req.getRequestDate(),
                                                  req.getRequestStatus(), req.getReleaseDate(), req.getManageBy()));
                         }

                    });
                    model.addAttribute("studentreq", studentRequests);
                    return "/facilitator/facilitator-studreq-view";
               }
               return "redirect:/";

          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

     @Override
     public boolean changeStatusAndManageByAndMessageOfRequests(String status, String manageBy, String message,
               long userId) {
          Users user = usersRepository.findByuserId(userId).get();
          StudentRequest studentRequest = studentRepository.findOneByRequestBy(user).get();

          if (message.equals("N/A")) {
               message = studentRequest.getMessage();
          } else {
               message = studentRequest.getMessage() + "\n" + message;
          }
          if (user != null && studentRequest != null) {
               studentRepository.changeStatusAndManagebyAndMessageOfRequests(status, manageBy,
                         message, studentRequest.getRequestId());
               return true;
          }
          return false;
     }

}
