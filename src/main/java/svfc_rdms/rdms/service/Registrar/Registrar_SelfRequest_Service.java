package svfc_rdms.rdms.service.Registrar;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import svfc_rdms.rdms.model.RegistrarRequest;

public interface Registrar_SelfRequest_Service {

      // Requests for Teachers
      Optional<RegistrarRequest> getRegistrarRequest(long requestsId);

      ResponseEntity<String> sendRequestToTeacher(long userId, HttpSession session, Map<String, String> params);

      String displayAllRequests(HttpSession session, Model model);

      ResponseEntity<Object> viewRegistrarRequests(long requestsId);

}
