package svfc_rdms.rdms.repository.Global;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import svfc_rdms.rdms.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
     public Optional<Users> findUserIdByUsername(String username);

     public Optional<Users> findByUsername(String username);

     public Optional<Users> findByName(String name);

     public List<Users> findAllByStatusAndType(String status, String type);

     public List<Users> findAllByType(String type);

     public Optional<Users> findByuserId(long userId);

     @Query("SELECT COUNT(u) FROM Users u WHERE status =:status AND type =:type ")
     int totalUsers(String status, String type);

     @Modifying
     @Transactional
     @Query("UPDATE Users u SET u.status =:status WHERE u.userId =:userId")
     void changeStatusOfUser(String status, long userId);
}
