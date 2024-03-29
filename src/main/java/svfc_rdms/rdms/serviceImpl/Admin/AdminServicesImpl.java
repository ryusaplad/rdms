package svfc_rdms.rdms.serviceImpl.Admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
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
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Global.NotificationServiceImpl;

@Service
public class AdminServicesImpl implements AdminService, FileService {

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private StudentRepository studRepo;

     @Autowired
     private DocumentRepository docRepo;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private RegsRequestRepository regsRepository;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Autowired
     private GlobalLogsServiceImpl globalLogsService;

     @Autowired
     private NotificationServiceImpl notificationService;

     @Override
     public List<Users> diplayAllAccounts(String status, String type) {

          return usersRepository.findAllByStatusAndType(status, type);
     }

     @Override
     public List<Users> diplayAllAccountsByType(String type) {
          return usersRepository.findAllByType(type);
     }

     @Override
     public int displayCountsByStatusAndType(String status, String type) {
          return usersRepository.totalUsers(status, type);
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
                                   throw new ApiRequestException("Invalid Length Title, Title Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("description")) {
                              description = entry.getValue();
                              if (description.replace(" ", "").length() <= 0) {
                                   throw new ApiRequestException(
                                             "Invalid Length Description, Description Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("title")) {
                              title = entry.getValue();
                              if (title.replace(" ", "").length() > 20) {
                                   throw new ApiRequestException("Invalid Length Title, Cannot be greater than 20");
                              }
                         } else if (entry.getKey().equals("description")) {
                              description = entry.getValue();
                              if (description.replace(" ", "").length() > 1000) {
                                   throw new ApiRequestException(
                                             "Invalid Length Description, Cannot be greater than 1000");
                              }
                         } else {
                              throw new ApiRequestException("No Documents Information has been found.");
                         }
                    }

                    Documents saveDocument = new Documents(0, title, description, image, status);

                    List<Users> users = usersRepository.findAllByStatusAndType("Active", "Student");

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
                         globalService.sendTopic("/topic/request/cards", "OK");
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
                                   throw new ApiRequestException("Invalid Length Title, Title Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("description")) {
                              description = entry.getValue();
                              if (description.replace(" ", "").length() <= 0) {
                                   throw new ApiRequestException(
                                             "Invalid Length Description, Description Cannot be empty.");
                              }
                         } else if (entry.getKey().equals("title")) {
                              title = entry.getValue();
                              if (title.replace(" ", "").length() > 20) {
                                   throw new ApiRequestException("Invalid Length Title, Cannot be greater than 20");
                              }
                         } else if (entry.getKey().equals("description")) {
                              description = entry.getValue();
                              if (description.replace(" ", "").length() > 1000) {
                                   throw new ApiRequestException(
                                             "Invalid Length Description, Cannot be greater than 1000");
                              }
                         } else if (entry.getKey().equals("status")) {
                              status = (entry.getValue().equals("1")) ? true : false;

                         }
                    }

                    Documents updateDocument = new Documents(id, title, description, image, status);
                    List<Users> users = usersRepository.findAllByStatusAndType("Active", "Student");

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
                         globalService.sendTopic("/topic/request/cards", "OK");
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
          Sort ascendingSort = Sort.by("title").ascending();
          return docRepo.findAll(ascendingSort);
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
     public Boolean deleteDocumentFile(long id, HttpSession session, HttpServletRequest request) {
          if (id > -1) {
               docRepo.deleteById(id);
               String date = LocalDateTime.now().toString();
               String logMessage = "A Document has been deleted by the user " + session.getAttribute("name").toString()
                         + " with the username of : " + session.getAttribute("username").toString() + ".";
               globalLogsService.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
               globalService.sendTopic("/topic/request/cards", "OK");
               return true;
          }
          return null;
     }

     @Override
     public List<StudentRequest> displayAllRequest() {
          return studRepo.findAll();
     }

     @Override
     public ResponseEntity<Object> viewAllRegistrarRequests() {
          Sort descendingSort = Sort.by("requestId").descending();
          List<RegistrarRequest> req = regsRepository.findAll(descendingSort);
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
     public ResponseEntity<Object> viewAllRegistrarRequestsByRequestId(long requestId) {
          Optional<RegistrarRequest> req = regsRepository.findById(requestId);
          List<RegistrarRequest_DTO> registrarDtoCompressor = new ArrayList<>();
          if (req.isPresent()) {

               List<UserFiles> regRequestFiles = fileRepository.findAllByRegRequestsWith(req.get());

               RegistrarRequest_DTO regDto = new RegistrarRequest_DTO(
                         req.get().getRequestId(),
                         req.get().getRequestTitle(), req.get().getRequestMessage(),
                         req.get().getTeacherMessage(),
                         req.get().getRequestBy().getName(),
                         req.get().getRequestTo().getName(), req.get().getRequestDate(),
                         req.get().getDateOfUpdate(), req.get().getRequestStatus());
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

          Optional<Users> defaultAdminUser = usersRepository.findByUsername(user.getUsername());
          if (!defaultAdminUser.isPresent()) {
               usersRepository.save(user);
          }

     }

     @Override
     public void ensureDefaultAccountUsersExist() {

          List<Users> users = new ArrayList<>();

          Users account1 = Users.builder().name("Ryu Saplad").username("S-082095")
                    .password(new BCryptPasswordEncoder().encode("Rdms123@")).type("Student").status("Active")
                    .build();

          Users account2 = Users.builder().name("Regs Ki").username("R-1")
                    .password(new BCryptPasswordEncoder().encode("Rdms123@")).type("Registrar").status("Active")
                    .build();
          Users account3 = Users.builder().name("Melchor Erise").username("T-1")
                    .password(new BCryptPasswordEncoder().encode("Rdms123@")).type("Teacher").status("Active")
                    .build();

          

          users.add(account1);
          users.add(account2);
          users.add(account3);

          users.stream().forEach(user -> {
               Optional<Users> userData = usersRepository.findByUsername(user.getUsername());
               if (!userData.isPresent()) {
                    String colorCode = globalService.generateRandomHexColor();
                    user.setColorCode(colorCode);
                    usersRepository.save(user);
               }
          });

     }

     @Override
     public void ensureDefaultDocumentsExist() {

          ClassPathResource gradeImage = new ClassPathResource("static/images/report.PNG");
          ClassPathResource coe = new ClassPathResource("static/images/coe.PNG");
          ClassPathResource cog = new ClassPathResource("static/images/COG.PNG");
          ClassPathResource cograd = new ClassPathResource("static/images/cograd.PNG");
          ClassPathResource goodmoral = new ClassPathResource("static/images/report.PNG");
          ClassPathResource sf9 = new ClassPathResource("static/images/sf9.PNG");
          ClassPathResource sf10 = new ClassPathResource("static/images/sf10.PNG");
          ClassPathResource honorable = new ClassPathResource("static/images/transfer.PNG");
          ClassPathResource tor = new ClassPathResource("static/images/tor.PNG");

          // List all the files in the folder
          List<ClassPathResource> images = new ArrayList<>();
          images.add(gradeImage);
          images.add(coe);
          images.add(cog);
          images.add(cograd);
          images.add(goodmoral);
          images.add(sf9);
          images.add(sf10);
          images.add(honorable);
          images.add(tor);

          String[] documentTitles = { "Reports Of Grades", "Certifcate Of Enrollment", "Certificate Of Grades",
                    "Certificate Of Graduation", "Good Moral", "SF9-Form 138", "SF10-Form 137", "Honorable Dismissal",
                    "TOR" };

          String gradeDescription = "Student Information - Requirements\n" +
                    "1. Year\n" +
                    "2. Course\n" +
                    "3. Semester\n" +
                    "4. List of Subjects & Professors (Upload in a pdf/doc/docx/jpeg/jpg/png format)";

          String allDiscription = "Student Information - Requirements\n" +
                    "1. Year\n" +
                    "2. Course\n" +
                    "3. Semester\n";

          String[] documentDescriptions = { gradeDescription,
               allDiscription,allDiscription,allDiscription,
               allDiscription,allDiscription,allDiscription,allDiscription,allDiscription};

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
                    Users uploader = usersRepository.findByuserId(userId).get();
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
               return "svfc-admin/admin";
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
          return "svfc-admin/admin";
     }

}
