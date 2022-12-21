package svfc_rdms.rdms.service.File;

import java.util.List;
import java.util.Optional;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;

public interface FileService {
     boolean saveFiles(UserFiles files);

     List<UserFiles> getAllFiles();

     Optional<UserFiles> getFileById(long id);

     List<UserFiles> getFilesByRequestWith(StudentRequest sr);

     Boolean deleteFile(long id);

     String formatFileUploadSize(long size);
}
