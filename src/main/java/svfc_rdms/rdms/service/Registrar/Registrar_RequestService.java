package svfc_rdms.rdms.service.Registrar;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface Registrar_RequestService {

     // Manage Students Request

     ResponseEntity<Object> fetchAllStudentRequest();

     ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
               Optional<MultipartFile[]> files, Map<String, String> params,
               HttpSession session,HttpServletRequest request);

     String displayAllFilesByUserId(HttpSession session, Model model);

     ResponseEntity<String> changeStatusAndManageByAndMessageOfRequests(String status, String message, long userId,
               long requestId, HttpSession session,HttpServletRequest request);
}
