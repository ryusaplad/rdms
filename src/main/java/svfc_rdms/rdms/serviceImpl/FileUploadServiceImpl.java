package svfc_rdms.rdms.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.repository.FileUploadRepository;
import svfc_rdms.rdms.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService {

     @Autowired
     FileUploadRepository fileRepo;

     @Override
     public boolean saveFiles(UserFiles files) {

          if (files != null) {
               fileRepo.save(files);
               return true;
          }
          return false;
     }

}
