package svfc_rdms.rdms.service.Registrar;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Users;

public interface Registrar_Service {

      String displayAllStudentRequest(String userType, Model model);

      String displayAllFilesByUserId(HttpSession session, Model model);

      boolean changeStatusAndManageByAndMessageOfRequests(String status, String manageBy, String message, long userId,
                  long requestId);

      // Manage Request
      ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
                  Optional<MultipartFile[]> files, Map<String, String> params,
                  HttpSession session);

      // Manage Accounts
      ResponseEntity<Object> saveUsersAccount(Users user, int actions);

      List<Users> diplayAllAccountsByType(String type);

      boolean findUserName(String username);

      Optional<Users> findOneUserById(long userId);

      boolean deleteData(long userId);

      boolean changeAccountStatus(String status, long userId);

}
