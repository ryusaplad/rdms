package svfc_rdms.rdms.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.AdminRepository;
import svfc_rdms.rdms.repository.Admin_DocumentRepository;
import svfc_rdms.rdms.service.AdminService;

@Service
public class MainServiceImpl implements AdminService {

     @Autowired
     AdminRepository repository;

     @Autowired
     Admin_DocumentRepository docRepo;

     @Override
     public List<Users> diplayAllAccounts(String status, String type) {
          return repository.findAllByStatusAndType(status, type);
     }

     @Override
     public boolean deleteData(long userId) {
         
          if (findOneUserById(userId).size() > 0) {
              
               repository.deleteById(userId);
               return true;
          }
          return false;
     }

     @Override
     public boolean changeAccountStatus(String status, long userId) {
          if (findOneUserById(userId).size() > 0) {
               repository.changeStatusOfUser(status, userId);
               return true;
          }
          return false;
     }

     @Override
     public boolean saveUsersAccount(Users user) {
          if (user != null) {
               repository.saveAndFlush(user);
               return true;
          }
          return false;
     }

     @Override
     public boolean findUserName(String username) {
          List<Users> users = (repository.findByUsername(username));
          if (users.size() > 0) {
               return true;
          }
          return false;
     }

     @Override
     public List<Users> findOneUserById(long userId) {
          return repository.findByuserId(userId);
     }

     @Override
     public int displayCountsByStatusAndType(String status, String type) {
          return repository.totalUsers(status, type);
     }

     @Override
     public Boolean saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo) {
          try {
               String title = "";
               String description = "";
               Boolean status = true;

               for (Map.Entry<String, String> entry : documentsInfo.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getKey().equals("title")) {
                         title = entry.getValue();
                    } else if (entry.getKey().equals("description")) {
                         description = entry.getValue();
                    } else {
                         return false;
                    }
               }
               byte[] image = multipartFile.getBytes();
               Documents docInformation = new Documents(0, title, description, image, status);

               docRepo.save(docInformation);

               return true;
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }

          return false;
     }

     @Override
     public List<Documents> getAllFiles() {
          return docRepo.findAll();
     }

     @Override
     public Optional<Documents> getFileById(long id) {
          Optional<Documents> fileOptional = docRepo.findById(id);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return Optional.empty();
     }

     @Override
     public Boolean deleteFile(long id) {
          if (id > 0) {
               docRepo.deleteById(id);
               return true;
          }
          return null;
     }

}
