package svfc_rdms.rdms.service.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentService {
     ResponseEntity<String> saveRequest(String id,
               MultipartFile[] files, String document,
               Map<String, String> params);

     Optional<Documents> findDocumentByTitle(String title);

     List<StudentRequest> displayRequestByStudent(Users user);

     Optional<Users> getUserIdByUsername(String username);

}
