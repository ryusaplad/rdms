package svfc_rdms.rdms.repository.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.Users;

public interface TeacherRepository extends JpaRepository<RegistrarRequest, Long> {
    @Query("SELECT COUNT(u) FROM RegistrarRequest u WHERE requestTo = :user")
    long totalRequests(Users user);
    
    @Query("SELECT COUNT(u) FROM RegistrarRequest u WHERE requestTo = :user AND requestStatus = :status")
    long totalRequestByUserAndStatus(Users user, String status);
    
}
