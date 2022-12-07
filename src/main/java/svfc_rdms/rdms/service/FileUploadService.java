package svfc_rdms.rdms.service;

import svfc_rdms.rdms.model.UserFiles;

public interface FileUploadService {
     boolean saveFiles(UserFiles files);
}
