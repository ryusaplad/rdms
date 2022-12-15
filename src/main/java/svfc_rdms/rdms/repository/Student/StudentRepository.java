package svfc_rdms.rdms.repository.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentRepository extends JpaRepository<StudentRequest, Long> {
     public List<StudentRequest> findAllByRequestBy(Users user);
     
}
