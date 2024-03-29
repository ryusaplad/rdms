package svfc_rdms.rdms.service.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface Student_RequestService {

     ResponseEntity<Object> submitRequest(String id,
               Optional<MultipartFile[]> files, String document,
               Map<String, String> params, HttpSession session, HttpServletRequest request);

     Optional<Documents> findDocumentByTitle(String title);
     
     List<Documents> displayAllDocuments(boolean status);
   
     ResponseEntity<Object> loadAllStudentRequest(HttpSession session);

     List<StudentRequest> displayAllRequestByStudent(Users user);

     List<StudentRequest> displayAllRequestByStudentAndRequestId(Users user, long requestId);

     Optional<Users> displayAllRequestByStudent(String username);

     ResponseEntity<Object> fetchRequestInformationToModals(String username, Long requestId);

}