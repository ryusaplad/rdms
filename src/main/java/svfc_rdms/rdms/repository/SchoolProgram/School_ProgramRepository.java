package svfc_rdms.rdms.repository.SchoolProgram;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.SchoolPrograms;

public interface School_ProgramRepository extends JpaRepository<SchoolPrograms, Long> {
    List<SchoolPrograms> findAllBySchoolLevel(String schoolLevel);
    List<SchoolPrograms> findAllByLevelAndSchoolLevel(String level, String schoolLevel);
}