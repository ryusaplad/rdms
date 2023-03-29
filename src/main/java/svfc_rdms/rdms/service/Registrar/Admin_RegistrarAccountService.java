package svfc_rdms.rdms.service.Registrar;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface Admin_RegistrarAccountService {
    ResponseEntity<Object> saveUsersAccount(Users user, int actions, HttpSession session);

    boolean findUserName(String username);

    Optional<Users> findOneUserById(long userId);

    boolean deleteData(long userId, HttpSession session);

    boolean changeAccountStatus(String status, long userId, HttpSession session);
}
