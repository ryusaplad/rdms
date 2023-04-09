package svfc_rdms.rdms.serviceImpl.Admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.ExceptionHandler.UserNotFoundException;
import svfc_rdms.rdms.dto.RegistrarRequest_DTO;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegsRequestRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Admin.AdminService;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;

@Service
public class AdminServicesImpl implements AdminService, FileService {

     @Autowired
     UsersRepository userRepository;

     @Autowired
     StudentRepository studRepo;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     FileRepository fileRepository;

     @Autowired
     RegsRequestRepository regsRepository;

     @Autowired
     GlobalServiceControllerImpl globalService;

     @Autowired
     private NotificationServiceImpl notificationService;

     @Override
     public List<Users> diplayAllAccounts(String status, String type) {

          return userRepository.findAllByStatusAndType(status, type);
     }

     @Override
     public List<Users> diplayAllAccountsByType(String type) {
          return userRepository.findAllByType(type);
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
     public ResponseEntity<Object> saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo,
               HttpSession session, HttpServletRequest request) {

          try {

               String title = "";
               String description = "";
               Boolean status = true;
               int notificationCounter = 0;
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

                    List<Users> users = userRepository.findAllByStatusAndType("Active", "Student");

                    if (!users.isEmpty()) {
                         for (Users user : users) {
                              String notifTitle = "A document " + saveDocument.getTitle() + " has been added.";
                              String message = "Hello " + user.getName() + ", a new document titled '" + title
                                        + "' is waiting for you in the Request Document section. To access it, please go to the Request Document section and select '"
                                        + title + "' from the list.";
                              String messageType = "new_document";
                              String dateAndTime = globalService.formattedDate();
                              
                              if (notificationService.sendStudentNotification(notifTitle, message, messageType,
                                        dateAndTime,
                                        false, user, session, request)) {
                                   notificationCounter++;

                              }
                         }
                    }
                    if (notificationCounter >= 0) {
                         docRepo.save(saveDocument);
                         globalService.sendTopic("/topic/request/cards","OK");
                         globalService.sendTopic("/topic/totals", "OK");
                         return new ResponseEntity<Object>("success", HttpStatus.OK);
                    } else {
                         return new ResponseEntity<Object>("failed", HttpStatus.BAD_REQUEST);
                    }
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
               Map<String, String> documentsInfo, HttpSession session, HttpServletRequest request) {

          try {
               byte[] image = docRepo.findById(id).get().getImage();

               String title = "";
               String description = "";
               Boolean status = true;
               int notificationCounter = 0;

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
                    List<Users> users = userRepository.findAllByStatusAndType("Active", "Student");

                    if (!users.isEmpty()) {
                         for (Users user : users) {
                              String notifTitle = "A document " + updateDocument.getTitle() + " has been updated.";
                              String message = "Hello " + user.getName() + ", a document titled '" + title
                                        + "' has been updated and is waiting for you in the Request Document section. To access it, please go to the Request Document section and select '"
                                        + title + "' from the list.";
                              String messageType = "new_document";
                              String dateAndTime = globalService.formattedDate();

                              if (notificationService.sendStudentNotification(notifTitle, message, messageType,
                                        dateAndTime,
                                        false, user, session, request)) {
                                   notificationCounter++;

                              }
                         }
                    }
                    if (notificationCounter >= 0) {
                         docRepo.save(updateDocument);
                         globalService.sendTopic("/topic/request/cards","OK");
                         return new ResponseEntity<Object>("success", HttpStatus.OK);
                    } else {
                         return new ResponseEntity<Object>("failed", HttpStatus.BAD_REQUEST);
                    }

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
          Optional<UserFiles> fileOptional = fileRepository.findById(uuidValue);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return Optional.empty();
     }

     @Override
     public Boolean deleteDocumentFile(long id, HttpSession session) {
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
                    List<UserFiles> regRequestFiles = fileRepository.findAllByRegRequestsWith(regRequests);

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
     public void ensureDefaultAdminUserExists() {

          Users user = Users.builder().name("Administrator").username("admin")
                    .password(new BCryptPasswordEncoder().encode("Akosiryu123@")).type("School_Admin").status("Active")
                    .colorCode("#DC143C").build();

          Optional<Users> defaultAdminUser = userRepository.findByUsername(user.getUsername());
          if (!defaultAdminUser.isPresent()) {
               userRepository.save(user);
          }

     }

     @Override
     public void ensureDefaultAccountUsersExist() {

          List<Users> users = new ArrayList<>();

          Users account1 = Users.builder().name("Ryu Saplad").username("C-082095")
                    .password(new BCryptPasswordEncoder().encode("rdms123@")).type("Student").status("Active")
                    .build();

          Users account2 = Users.builder().name("Regs Ki").username("R-1")
                    .password(new BCryptPasswordEncoder().encode("rdms123@")).type("Registrar").status("Active")
                    .build();

          users.add(account1);
          users.add(account2);

          users.stream().forEach(user -> {
               Optional<Users> userData = userRepository.findByUsername(user.getUsername());
               if (!userData.isPresent()) {
                    String colorCode = globalService.generateRandomHexColor();
                    user.setColorCode(colorCode);
                    userRepository.save(user);
               }
          });

     }

     @Override
     public void ensureDefaultDocumentsExist() {

          ClassPathResource idFile = new ClassPathResource("static/images/ID.png");
          ClassPathResource comImage = new ClassPathResource("static/images/COM.png");
          ClassPathResource corImage = new ClassPathResource("static/images/COR.png");
          ClassPathResource gradeImage = new ClassPathResource("static/images/GRADES.jpg");

          // List all the files in the folder
          List<ClassPathResource> images = new ArrayList<>();
          images.add(idFile);
          images.add(comImage);
          images.add(corImage);
          images.add(gradeImage);

          String[] documentTitles = { "ID", "COM", "COR", "GRADES" };
          String idDescription = "Student Information - Requirements\n" +
                    "1. Year\n" +
                    "2. Course\n" +
                    "3. Semester\n\n" +
                    "File Requirements\n" +
                    "1. ID PICTURE\n- ID Picture must be white background." +
                    "\n-Wear Uniform/Org Shirt." +
                    "1. Signature Image\n- Signature Image must be in PNG format with a white background.";
          String comDescription = "Student Information - Requirements\n" +
                    "1. Year\n" +
                    "2. Course\n" +
                    "3. Semester\n\n" +
                    "File Requirements\n" +
                    "1. Signature Image\n- Signature Image must be in PNG format with a white background.";
          String corDescription = "Student Information - Requirements\n" +
                    "1. Year\n" +
                    "2. Course\n" +
                    "3. Semester\n\n" +
                    "File Requirements\n" +
                    "1. Signature Image\n- Signature Image must be in a PNG format and white background";

          String gradeDescription = "Student Information - Requirements\n" +
                    "1. Year\n" +
                    "2. Course\n" +
                    "3. Semester\n" +
                    "4. List of Subjects & Professors (Upload in a pdf/doc/docx/jpeg/jpg/png format)";

          String[] documentDescriptions = { idDescription, comDescription,
                    corDescription, gradeDescription };

          // Iterate through the files and upload them to the database
          for (int index = 0; index < images.size(); index++) {
               try {
                    Optional<Documents> documents = docRepo.findByTitle(documentTitles[index]);
                    if (!documents.isPresent()) {
                         InputStream inputStream = images.get(index).getInputStream();
                         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                         byte[] buffer = new byte[1024];
                         int length;
                         while ((length = inputStream.read(buffer)) != -1) {
                              outputStream.write(buffer, 0, length);
                         }
                         byte[] data = outputStream.toByteArray();

                         Documents document = new Documents();
                         document.setTitle(documentTitles[index]);
                         document.setDescription(documentDescriptions[index]);
                         document.setImage(data);
                         document.setStatus(true);
                         docRepo.save(document);
                    }

               } catch (IOException e) {
                    System.out.println(e.getMessage());
               }
          }

     }

     @Override
     public boolean saveFiles(UserFiles files) {
          if (files != null) {
               fileRepository.save(files);
               return true;
          } else {
               return false;
          }
     }

     @Override
     public List<UserFiles> getAllFiles() {
          return fileRepository.findAll();
     }

     @Override
     public List<UserFiles> getFilesByRequestWith(StudentRequest sr) {
          // TODO Auto-generated method stub
          throw new UnsupportedOperationException("Unimplemented method 'getFilesByRequestWith'");
     }

     @Override
     public List<UserFiles> getAllFilesByUser(long userId) {

          try {
               if (userId != -1) {
                    Users uploader = userRepository.findByuserId(userId).get();
                    List<UserFiles> studentFiles = fileRepository.findAllByUploadedBy(uploader);
                    if (!studentFiles.isEmpty()) {
                         return studentFiles;
                    }

               }

               return null;

          } catch (IllegalArgumentException e) {
               throw new UserNotFoundException("Can't display files because, Reason: User " + userId + " not found.",
                         e);
          }
     }

     @Override
     public Boolean deleteFile(UUID id) {
          Optional<UserFiles> userFile = fileRepository.findById(id);
          if (userFile.isPresent()) {
               fileRepository.delete(userFile.get());
               return true;
          }
          return false;
     }

     @Override
     public String displayAllUserFiles(HttpSession session, Model model) {
          List<UserFiles> getAllFiles = getAllFiles();
          List<UserFiles_Dto> userFiles = new ArrayList<>();

          if (getAllFiles == null) {
               model.addAttribute("files", userFiles);
               return "/admin/admin";
          }
          getAllFiles.stream().forEach(file -> {
               String stringValue = file.getFileId().toString();
               UUID uuidValue = UUID.fromString(stringValue);
               String uploadedBy = file.getUploadedBy().getUsername() + ":"
                         + file.getUploadedBy().getName();
               userFiles.add(new UserFiles_Dto(
                         uuidValue, file.getName(), file.getSize(),
                         file.getStatus(),
                         file.getDateUploaded(), file.getFilePurpose(), uploadedBy));
          });

          model.addAttribute("files", userFiles);
          model.addAttribute("page", "globalfiles");
          model.addAttribute("pageTitle", "Global Files");
          return "/admin/admin";
     }

}
