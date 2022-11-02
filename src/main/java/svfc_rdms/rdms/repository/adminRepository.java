package svfc_rdms.rdms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import svfc_rdms.rdms.model.Users;

public interface adminRepository extends JpaRepository<Users, Long> {

     public List<Users> findByUsername(String username);

     public List<Users> findAllByType(String type);

     public List<Users> findByuserId(long userId);

     @Query("SELECT COUNT(u) FROM Users u WHERE type =:type")
     int countUsers(String type);
}
