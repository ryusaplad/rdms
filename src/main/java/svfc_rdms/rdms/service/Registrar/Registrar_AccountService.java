package svfc_rdms.rdms.service.Registrar;

import java.util.List;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface Registrar_AccountService {
     List<Users> getAllAccountsByType(String userType);

     ResponseEntity<Object> displayAllUserAccountByType(String userType);

}
