package svfc_rdms.rdms.service.Student;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface Student_RequirementService {

     ResponseEntity<Object> updateFileRequirement(Optional<MultipartFile> file,
               Map<String, String> params);

     ResponseEntity<Object> addFileRequirement(Map<String, MultipartFile> params, long requestId, HttpSession session);

     ResponseEntity<Object> updateInformationRequirement(long requestId, Map<String, String> params);

     ResponseEntity<Object> resubmitRequest(String status, long userId, long requestId, HttpSession session,
               HttpServletRequest request);

     String displayAllFilesByUserId(HttpSession session, Model model);

}
