package svfc_rdms.rdms.repository.SchoolProgram;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Course;

public interface School_ProgramRepository extends JpaRepository<Course, Long> {
  List<Course> findAllByLevelAndStatus(String level, String status);
}