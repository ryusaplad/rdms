package svfc_rdms.rdms.service.Global;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface LoginService {
     ResponseEntity<String> login(Users user, HttpSession session, HttpServletResponse response);
}
