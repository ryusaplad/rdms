package svfc_rdms.rdms.repository.Student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentRepository extends JpaRepository<StudentRequest, Long> {
     List<StudentRequest> findAllByRequestBy(Users user);
     
     Optional<StudentRequest> findOneByRequestBy(Users user);
     List<StudentRequest> findAllByRequestByAndRequestId(Users user, long requestId);

     @Modifying
     @Transactional
     @Query("UPDATE StudentRequest req SET req.requestStatus =:status,req.manageBy =:manageBy,req.message =:message WHERE req.requestId =:requestId")
     void changeStatusAndManagebyAndMessageOfRequests(String status, String manageBy, String message, long requestId);

     @Modifying
     @Transactional
     @Query("UPDATE StudentRequest req SET req.requestStatus =:status WHERE req.requestId =:requestId")
     void studentRequestsResubmit(String status, long requestId);
}
