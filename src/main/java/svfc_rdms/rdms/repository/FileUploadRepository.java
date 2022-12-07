package svfc_rdms.rdms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.UserFiles;


public interface FileUploadRepository extends JpaRepository<UserFiles,Long> {
     
}
