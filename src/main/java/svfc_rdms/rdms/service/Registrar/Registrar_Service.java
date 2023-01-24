package svfc_rdms.rdms.service.Registrar;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.Users;

public interface Registrar_Service {


      // Manage Students Request
      String displayAllStudentRequest(String userType, Model model);

      String displayAllFilesByUserId(HttpSession session, Model model);

      ResponseEntity<Object> displayAllUserAccountByType(String userType);

      boolean changeStatusAndManageByAndMessageOfRequests(String status, String message, long userId,
                  long requestId, HttpSession session);


      ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
                  Optional<MultipartFile[]> files, Map<String, String> params,
                  HttpSession session);

      // Requests for Teachers
      Optional<RegistrarRequest> getRegistrarRequest(long requestsId);

      ResponseEntity<String> sendRequestToTeacher(long userId, HttpSession session, Map<String, String> params);

      String displayAllRequestsByStatus(Model model);

      ResponseEntity<Object> viewRegistrarRequests(long requestsId);

      // Manage Accounts
      ResponseEntity<Object> saveUsersAccount(Users user, int actions);

      List<Users> getAllAccountsByType(String userType);

      boolean findUserName(String username);

      Optional<Users> findOneUserById(long userId);

      boolean deleteData(long userId);

      boolean changeAccountStatus(String status, long userId);

}
