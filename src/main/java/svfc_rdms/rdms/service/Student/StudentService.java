package svfc_rdms.rdms.service.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentService {

     String displayStudentRequests(Model model, HttpSession session);

     ResponseEntity<String> saveRequest(String id,
               Optional<MultipartFile[]> files, String document,
               Map<String, String> params);

     Optional<Documents> findDocumentByTitle(String title);

     Optional<StudentRequest> findRequestById(Long requestId);


     List<StudentRequest> displayAllRequestByStudent(Users user);

     List<StudentRequest> displayAllRequestByStudentAndRequestId(Users user, long requestId);

     Optional<Users> displayAllRequestByStudent(String username);

     ResponseEntity<Object> fetchRequestInformationToModals(String username,
               Long requestId);

     // File Management

     void student_showImageFiles(long id, HttpServletResponse response,
               Optional<Documents> dOptional);

     void student_DownloadFile(String id, Model model, HttpServletResponse response);

     ResponseEntity<Object> updateFileRequirement(Optional<MultipartFile> file,
               Map<String, String> params);

     ResponseEntity<Object> updateInformationRequirement(long requestId, Map<String, String> params);

     ResponseEntity<Object> resubmitRequests(String status, long userId, long requestId);

}
