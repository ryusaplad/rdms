package svfc_rdms.rdms.serviceImpl.File;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.service.File.FileService;

@Service
public class FileUploadServiceImpl implements FileService {

     @Autowired
     FileRepository fileRepo;

     @Override
     public boolean saveFiles(UserFiles files) {

          if (files != null) {

               fileRepo.save(files);
               return true;
          }
          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {

          return fileRepo.findAll();
     }

     @Override
     public Optional<UserFiles> getFileById(long id) {

          Optional<UserFiles> fileOptional = fileRepo.findById(id);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return null;
     }

     @Override
     public Boolean deleteFile(long id) {

          return null;
     }

}
