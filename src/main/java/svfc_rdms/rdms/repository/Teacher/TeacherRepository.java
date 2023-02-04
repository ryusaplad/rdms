package svfc_rdms.rdms.repository.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.RegistrarRequest;

public interface TeacherRepository extends JpaRepository<RegistrarRequest, Long> {

}
