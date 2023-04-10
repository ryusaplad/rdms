package svfc_rdms.rdms.repository.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import svfc_rdms.rdms.model.RegistrarRequest;

public interface TeacherRepository extends JpaRepository<RegistrarRequest, Long> {

    @Query("SELECT COUNT(u) FROM RegistrarRequest u")
    long totalRequests();
    @Query("SELECT COUNT(u) FROM RegistrarRequest u WHERE requestStatus =:status")
    long totalRequestsByStatus(String status);
}
