package svfc_rdms.rdms.service.Registrar;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.RegistrarRequest;

public interface Registrar_SelfRequest_Service {

      // Requests for Teachers
      ResponseEntity<Object> displayAllRequests(HttpSession session);

      Optional<RegistrarRequest> getRegistrarRequest(long requestsId);

      ResponseEntity<String> sendRequestToTeacher(long userId, HttpSession session, Map<String, String> params,
                  HttpServletRequest request);

      ResponseEntity<Object> viewRegistrarRequest(long requestsId);

}
