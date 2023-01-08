package svfc_rdms.rdms.service.Registrar;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface Registrar_Service {

      String displayAllStudentRequest(String userType, Model model);

      boolean changeStatusAndManageByAndMessageOfRequests(String status, String manageBy, String message, long userId,
                  long requestId);

      // registrar only
      ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
                  Optional<MultipartFile[]> files, Map<String, String> params,
                  HttpSession session);
}
