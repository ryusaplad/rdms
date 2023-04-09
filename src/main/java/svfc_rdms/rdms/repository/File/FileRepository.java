package svfc_rdms.rdms.repository.File;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;

public interface FileRepository extends JpaRepository<UserFiles, UUID> {

     Long countByUploadedBy(Users user);
     Long countByUploadedByAndStatus(Users user, String status);

     List<UserFiles> findAllByRequestWith(StudentRequest requestWith);

     List<UserFiles> findAllByUploadedBy(Users uploadedBy);

     List<UserFiles> findAllByRegRequestsWith(RegistrarRequest regRequestsWith);

}
