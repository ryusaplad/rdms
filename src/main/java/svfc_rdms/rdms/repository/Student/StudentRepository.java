package svfc_rdms.rdms.repository.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.StudentRequest;

public interface StudentRepository extends JpaRepository<StudentRequest, Long> {
     // public Users findUserIdByUsername(String username);

}
