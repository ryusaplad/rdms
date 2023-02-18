package svfc_rdms.rdms.serviceImpl.Admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.RegistrarRequest_DTO;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Admin.AdminService;

@Service
public class AdminServicesImpl implements AdminService {

     @Autowired
     UsersRepository userRepository;

     @Autowired
     StudentRepository studRepo;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     FileRepository userRepo;

     @Autowired
     RegRepository regsRepository;

     @Override
     public List<Users> diplayAllAccounts(String status, String type) {

          return userRepository.findAllByStatusAndType(status, type);
     }

     @Override
     public List<Users> diplayAllAccountsByType(String type) {
          return userRepository.findAllByType(type);
     }

     @Override
     public boolean deleteData(long userId) {

          if (findOneUserById(userId).isPresent()) {
               userRepository.deleteById(userId);
               return true;
          }
          return false;
     }

     @Override
     public boolean changeAccountStatus(String status, long userId) {
          if (findOneUserById(userId).isPresent()) {
               userRepository.changeStatusOfUser(status, userId);
               return true;
          }
          return false;
     }

     @Override
     public ResponseEntity<Object> saveUsersAccount(Users user, int actions) {

          String error = "";
          try {

               PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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
               } else if (user.getUsername().length() > 255 || user.getName().length() > 255
                         || user.getPassword().length() > 255) {
                    throw new ApiRequestException("Data/Information is too long, Please Try Again!");
               } else if (user.getPassword().length() < 8) {
                    return new ResponseEntity<>("Password must be at least 8 characters long",
                              HttpStatus.BAD_REQUEST);
               } else if (!user.getPassword()
                         .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$")) {
                    return new ResponseEntity<>(
                              "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, and 1 number",
                              HttpStatus.BAD_REQUEST);
               } else {
                    String userIdFormat = user.getUsername().toUpperCase();
                    user.setStatus("Active");
                    if (userIdFormat.contains("C")) {

                         user.setType("Student");
                    } else if (userIdFormat.contains("R-")) {

                         user.setType("Registrar");
                    } else if (userIdFormat.contains("T-")) {

                         user.setType("Teacher");
                    } else {
                         error = "Account Type Invalid, Please try again!";

                         throw new ApiRequestException(error);
                    }
                    if (actions == 0) {
                         if (findUserName(userIdFormat.toLowerCase())) {
                              error = "Username is already taken, Please try again!";

                              throw new ApiRequestException(error);
                         } else {
                              String hashedPassword = passwordEncoder.encode(user.getPassword());
                              user.setPassword(hashedPassword);
                              userRepository.saveAndFlush(user);
                              ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                              return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                         }
                    } else if (actions == 1) {

                         String hashedPassword = passwordEncoder.encode(user.getPassword());
                         user.setPassword(hashedPassword);
                         userRepository.saveAndFlush(user);
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
          if (userRepository.findByUsername(username).isPresent()) {
               Optional<Users> users = userRepository.findByUsername(username);
               if (users.isPresent()) {
                    return true;
               }
          }
          return false;

     }

     @Override
     public Optional<Users> findOneUserById(long userId) {
          Optional<Users> users = userRepository.findByuserId(userId);
          if (users.isPresent()) {
               return users;
          }
          return null;
     }

     @Override
     public int displayCountsByStatusAndType(String status, String type) {
          return userRepository.totalUsers(status, type);
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
     public List<Documents> getAllDocumentsByStatus(boolean status) {
          return docRepo.findAllByStatus(status);
     }

     @Override
     public List<String> getAllDocumentTitles() {
          return docRepo.findAllTitle();
     }

     @Override
     public Optional<Documents> getFileDocumentById(long id) {
          Optional<Documents> fileOptional = docRepo.findById(id);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return Optional.empty();
     }

     @Override
     public Optional<UserFiles> getFileById(String id) {
          String stringValue = id.toString();
          UUID uuidValue = UUID.fromString(stringValue);
          Optional<UserFiles> fileOptional = userRepo.findById(uuidValue);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return Optional.empty();
     }

     @Override
     public Boolean deleteDocumentFile(long id) {
          if (id > -1) {
               docRepo.deleteById(id);
               return true;
          }
          return null;
     }

     @Override
     public List<StudentRequest> displayAllRequest() {
          return studRepo.findAll();
     }

     @Override
     public ResponseEntity<Object> viewAllRegistrarRequests(long requestsId) {
          List<RegistrarRequest> req = regsRepository.findAll();
          List<RegistrarRequest_DTO> registrarDtoCompressor = new ArrayList<>();
          if (req != null) {
               req.forEach(regRequests -> {
                    List<UserFiles> regRequestFiles = userRepo.findAllByRegRequestsWith(regRequests);

                    RegistrarRequest_DTO regDto = new RegistrarRequest_DTO(
                              regRequests.getRequestId(),
                              regRequests.getRequestTitle(), regRequests.getRequestMessage(),
                              regRequests.getTeacherMessage(),
                              regRequests.getRequestBy().getName(),
                              regRequests.getRequestTo().getName(), regRequests.getRequestDate(),
                              regRequests.getDateOfUpdate(), regRequests.getRequestStatus());
                    registrarDtoCompressor.add(regDto);

                    if (regRequestFiles != null) {
                         regRequestFiles.forEach(userFiles -> {
                              registrarDtoCompressor.add(new RegistrarRequest_DTO(
                                        userFiles.getFileId(), userFiles.getName(),
                                        userFiles.getSize(),
                                        userFiles.getStatus(), userFiles.getDateUploaded(),
                                        userFiles.getFilePurpose(),
                                        userFiles.getUploadedBy().getName()));
                         });

                    }
               });
               return new ResponseEntity<Object>(registrarDtoCompressor, HttpStatus.OK);

          } else {
               throw new ApiRequestException("No data found for the given requests ID");
          }

     }

     @Override
     public void createDefault_Admin_User_IfNotExisted() {

          Users user = Users.builder().name("Administrator").username("admin")
                    .password(new BCryptPasswordEncoder().encode("Akosiryu123@")).type("School_Admin").status("Active")
                    .build();

          Optional<Users> defaultAdminUser = userRepository.findByUsername(user.getUsername());
          if (!defaultAdminUser.isPresent()) {
               userRepository.save(user);
          }

     }

}
