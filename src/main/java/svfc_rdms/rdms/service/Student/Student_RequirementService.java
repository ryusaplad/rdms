package svfc_rdms.rdms.service.Student;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface Student_RequirementService {

     ResponseEntity<Object> updateFileRequirement(Optional<MultipartFile> file,
               Map<String, String> params);

     ResponseEntity<Object> updateInformationRequirement(long requestId, Map<String, String> params);

     ResponseEntity<Object> resubmitRequests(String status, long userId, long requestId);

     String displayAllFilesByUserId(HttpSession session, Model model);

}
