package svfc_rdms.rdms.serviceImpl.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.ServiceResponse;
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

          if (findOneUserById(userId).isPresent()) {
               repository.deleteById(userId);
               return true;
          }
          return false;
     }

     @Override
     public boolean changeAccountStatus(String status, long userId) {
          if (findOneUserById(userId).isPresent()) {
               repository.changeStatusOfUser(status, userId);
               return true;
          }
          return false;
     }

     @Override
     public ResponseEntity<Object> saveUsersAccount(Users user, int actions) {

          String error = "";
          try {
               if (user.getName() == null || user.getName().length() < 1 || user.getName().isEmpty()) {
                    error = "Name cannot be empty! " + user.getName();
                    throw new ApiRequestException(error);
               } else if (user.getUsername() == null || user.getUsername().length() < 1
                         || user.getUsername().isEmpty()) {
                    error = "Username cannot be empty " + user.getUsername();
                    throw new ApiRequestException(error);
               } else if (user.getPassword() == null || user.getPassword().length() < 1
                         || user.getPassword().isEmpty()) {
                    error = "Password cannot be empty" + user.getPassword();
                    throw new ApiRequestException(error);
               } else {
                    String userIdFormat = user.getUsername().toUpperCase();
                    user.setStatus("Active");
                    if (userIdFormat.contains("C")) {

                         user.setType("Student");
                    } else if (userIdFormat.contains("F-")) {

                         user.setType("Facilitator");
                    } else if (userIdFormat.contains("R-")) {

                         user.setType("Registrar");
                    } else if (userIdFormat.contains("T-")) {

                         user.setType("Teacher");
                    }
                    if (actions == 0) {
                         if (findUserName(userIdFormat.toLowerCase())) {
                              error = "Username is already taken, Please try again!";

                              throw new ApiRequestException(error);
                         } else {

                              repository.saveAndFlush(user);
                              ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                              return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                         }
                    } else if (actions == 1) {

                         repository.saveAndFlush(user);
                         ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                         return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                    }
               }
          } catch (Exception e) {
               error = e.getMessage();
               if (error.contains("ConstraintViolationException")) {

                    error = "Username is already taken, Please try again!";
                    throw new ApiRequestException(error);
               }

          }
          throw new ApiRequestException(error);
     }

     @Override
     public boolean findUserName(String username) {
          if (repository.findByUsername(username).isPresent()) {
               Optional<Users> users = repository.findByUsername(username);
               if (users.isPresent()) {
                    return true;
               }
          }
          return false;

     }

     @Override
     public Optional<Users> findOneUserById(long userId) {
          Optional<Users> users = repository.findByuserId(userId);
          if (users.isPresent()) {
               return users;
          }
          return null;
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
     public ResponseEntity<Object> saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo) {

          try {

               String title = "";
               String description = "";
               Boolean status = true;

               if (multipartFile.getSize() == 0) {
                    throw new ApiRequestException("Invalid Image, Documents Image Cannot be Empty");
               } else {
                    byte[] image = multipartFile.getBytes();
                    for (Map.Entry<String, String> entry : documentsInfo.entrySet()) {

                         if (entry.getKey().equals("title")) {
                              title = entry.getValue();
                              if (title.replace(" ", "").length() <= 0) {
                                   throw new ApiRequestException("Invalid Title, Title Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("description")) {
                              description = entry.getValue();
                              if (description.replace(" ", "").length() <= 0) {
                                   throw new ApiRequestException("Invalid Description, Description Cannot be empty.");
                              }
                         } else {
                              throw new ApiRequestException("No Documents Information has been found.");
                         }
                    }

                    Documents saveDocument = new Documents(0, title, description, image, status);

                    docRepo.save(saveDocument);
                    return new ResponseEntity<Object>("success", HttpStatus.OK);
               }

          } catch (Exception e) {
               if (e.getMessage().contains("constraint")) {
                    throw new ApiRequestException("Document Title has already  taken.");
               } else {
                    throw new ApiRequestException(e.getMessage());

               }

          }

     }

     @Override
     public ResponseEntity<Object> saveDocumentData(long id, MultipartFile multipartFile,
               Map<String, String> documentsInfo) {

          try {
               byte[] image = docRepo.findById(id).get().getImage();

               String title = "";
               String description = "";
               Boolean status = true;

               if (multipartFile.getSize() > 0) {
                    image = multipartFile.getBytes();
               }

               if (id < 0) {
                    throw new ApiRequestException(
                              "Failed to Update Document, Invalid ID, Please Try Again.");
               } else {
                    for (Map.Entry<String, String> entry : documentsInfo.entrySet()) {

                         if (entry.getKey().equals("title")) {

                              title = entry.getValue();
                              if (title.replace(" ", "").length() <= 0) {
                                   throw new ApiRequestException("Invalid Title, Title Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("description")) {
                              description = entry.getValue();
                              if (description.replace(" ", "").length() <= 0) {
                                   throw new ApiRequestException("Invalid Description, Description Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("status")) {
                              status = (entry.getValue().equals("1")) ? true : false;

                         }
                    }

                    Documents updateDocument = new Documents(id, title, description, image, status);

                    docRepo.save(updateDocument);
                    return new ResponseEntity<Object>("success", HttpStatus.OK);
               }

          } catch (Exception e) {
               if (e.getMessage().contains("constraint")) {
                    throw new ApiRequestException("Document Title has already  taken.");

               } else {
                    throw new ApiRequestException(e.getMessage());

               }
          }
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
