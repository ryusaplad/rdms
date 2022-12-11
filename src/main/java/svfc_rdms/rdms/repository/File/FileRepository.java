package svfc_rdms.rdms.repository.File;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.UserFiles;

public interface FileRepository extends JpaRepository<UserFiles, Long> {

}
