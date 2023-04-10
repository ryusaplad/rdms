package svfc_rdms.rdms.service.Teacher;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.RegistrarRequest;

public interface Teacher_Service {

     ResponseEntity<Object> viewRegistrarRequests(long requestsId);

     Optional<RegistrarRequest> getRegistrarRequest(long requestsId);

     ResponseEntity<String> sendRequestToRegistrar(long requestsId, HttpSession session,
               Optional<MultipartFile[]> files,
               Map<String, String> params, HttpServletRequest request);

     ResponseEntity<Object> displayAllRequests(HttpSession session);

     String displayAllFilesByUserId(HttpSession session, Model model);
}
