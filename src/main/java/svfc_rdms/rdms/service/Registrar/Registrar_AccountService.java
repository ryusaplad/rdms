package svfc_rdms.rdms.service.Registrar;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface Registrar_AccountService {

     ResponseEntity<Object> saveUsersAccount(Users user, int actions);

     List<Users> getAllAccountsByType(String userType);

     boolean findUserName(String username);

     Optional<Users> findOneUserById(long userId);

     boolean deleteData(long userId);

     boolean changeAccountStatus(String status, long userId);

     ResponseEntity<Object> displayAllUserAccountByType(String userType);

}
