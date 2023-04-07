package svfc_rdms.rdms.repository.Global;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Users;

public interface LoginRepository extends JpaRepository<Users, Long> {

     Optional<Users> findByUsernameAndPasswordAndType(String username, String password, String type);

     Optional<Users> findPasswordByUsername(String username);
}
