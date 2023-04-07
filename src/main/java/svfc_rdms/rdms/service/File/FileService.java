package svfc_rdms.rdms.service.File;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;

public interface FileService {
     boolean saveFiles(UserFiles files);

     List<UserFiles> getAllFiles();

     Optional<UserFiles> getFileById(String id);

     List<UserFiles> getFilesByRequestWith(StudentRequest sr);

     List<UserFiles> getAllFilesByUser(long userId);

     Boolean deleteFile(UUID id);


}
