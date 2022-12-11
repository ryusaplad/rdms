package svfc_rdms.rdms.serviceImpl.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Admin.AdminRepository;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Admin.AdminService;

@Service
public class AdminServicesImpl implements AdminService {

     @Autowired
     AdminRepository repository;

     @Autowired
     StudentRepository studRepo;

     @Autowired
     DocumentRepository docRepo;

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
     public List<StudentRequest> displayRequestByStatus(String status) {
          return null;
     }

     @Override
     public Boolean saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo) {
          try {
              
               String title = "";
               String description = "";
               Boolean status = true;
               if (multipartFile.getSize() > 0 && documentsInfo != null) {
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
               }
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }

          return false;
     }

     @Override
     public Boolean saveDocumentData(long id, MultipartFile multipartFile, Map<String, String> documentsInfo) {
          try {
               byte[] image = docRepo.findById(id).get().getImage();
              
               String title = "";
               String description = "";
               Boolean status = true;

               if (multipartFile.getSize() > 0) {
                    image = multipartFile.getBytes();
               }

               for (Map.Entry<String, String> entry : documentsInfo.entrySet()) {

                    if (entry.getKey().equals("title")) {

                         title = entry.getValue();
                    } else if (entry.getKey().equals("description")) {
                         description = entry.getValue();
                    } else if (entry.getKey().equals("status")) {

                         status = (entry.getValue().equals("1")) ? true : false;
                    }
               }

               Documents docInformation = new Documents(id, title, description, image, status);

               docRepo.save(docInformation);

               return true;
          } catch (

          Exception e) {
               System.out.println(e.getMessage());
          }

          return false;
     }

     @Override
     public List<Documents> getAllDocuments() {
          return docRepo.findAll();
     }

     @Override
     public List<String> getAllDocumentTitles() {
          return docRepo.findAllTitle();
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

     @Override
     public List<StudentRequest> displayAllRequest() {

          return studRepo.findAll();
     }

}
