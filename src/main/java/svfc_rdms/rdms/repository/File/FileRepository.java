package svfc_rdms.rdms.repository.File;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;

public interface FileRepository extends JpaRepository<UserFiles, Long> {

     List<UserFiles> findAllByRequestWith(StudentRequest requestWith);

}
