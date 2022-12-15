package svfc_rdms.rdms.repository.Global;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Users;

public interface LoginRepository extends JpaRepository<Users, Long> {

     Users findByUsernameAndPasswordAndType(String username, String password, String type);
}
