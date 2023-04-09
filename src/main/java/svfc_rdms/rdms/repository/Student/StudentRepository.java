package svfc_rdms.rdms.repository.Student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentRepository extends JpaRepository<StudentRequest, Long> {

     long count();
     Long countByRequestBy(Users user);
     Long countByRequestStatus(String status);
     Long countByRequestByAndRequestStatus(Users user, String status);

     List<StudentRequest> findAllByRequestBy(Users user);

     Optional<StudentRequest> findOneByRequestByAndRequestId(Users user, long requestId);

     List<StudentRequest> findAllByRequestByAndRequestId(Users user, long requestId);

     @Modifying
     @Transactional
     @Query("UPDATE StudentRequest req SET req.requestStatus =:status,req.manageBy =:manageBy,req.reply =:reply WHERE req.requestId =:requestId")
     void changeStatusAndManagebyAndMessageOfRequests(String status, String manageBy, String reply, long requestId);

     @Modifying
     @Transactional
     @Query("UPDATE StudentRequest req SET req.requestStatus =:status WHERE req.requestId =:requestId")
     void studentRequestsResubmit(String status, long requestId);

     @Query("SELECT sr.year, sr.course, sr.requestStatus, COUNT(sr) " +
               "FROM StudentRequest sr " +
               "WHERE sr.requestStatus = :status AND SUBSTRING(sr.requestDate, 1, 16) = :date " +
               "GROUP BY sr.year, sr.course")
     List<Object[]> findCountAndRequestStatusAndYearAndCourseWhereStatusIsAndDateIs(
               @Param("status") String requestStatus,
               @Param("date") String requestDate);

}
