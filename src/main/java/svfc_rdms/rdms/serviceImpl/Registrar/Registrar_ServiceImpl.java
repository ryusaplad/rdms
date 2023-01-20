package svfc_rdms.rdms.serviceImpl.Registrar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.ExceptionHandler.UserNotFoundException;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Registrar.Registrar_Service;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Service
public class Registrar_ServiceImpl implements Registrar_Service, FileService {

     @Autowired
     private StudentRepository studentRepository;

     @Autowired
     private DocumentRepository documentRepository;

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private RegRepository regsRepository;

     @Autowired
     private StudentServiceImpl studentService;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @Override
     public String displayAllStudentRequest(String userType, Model model) {
          String link = "";
          try {

               List<StudentRequest> fetchStudentRequest = studentRepository.findAll();

               if (userType.isEmpty() || userType.isBlank()) {
                    return "redirect:/";

               } else if (userType.equals("registrar")) {
                    link = "/registrar/studreq-view";
               } else {
                    throw new ApiRequestException("Not Found");
               }
               if (fetchStudentRequest.size() > -1) {
                    List<StudentRequest_Dto> studentRequests = new ArrayList<>();

                    fetchStudentRequest.stream().forEach(req -> {

                         studentRequests
                                   .add(new StudentRequest_Dto(req.getRequestId(),
                                             req.getRequestBy().getUserId(),
                                             req.getRequestBy().getType(), req.getYear(),
                                             req.getCourse(), req.getSemester(),
                                             req.getRequestDocument().getTitle(),
                                             req.getMessage(), req.getReply(), req.getRequestBy().getName(),
                                             req.getRequestDate(),
                                             req.getRequestStatus(), req.getReleaseDate(),
                                             req.getManageBy()));

                    });
                    model.addAttribute("studentreq", studentRequests);
                    return link;
               }
               return "redirect:/";

          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

     @Override
     public String displayAllFilesByUserId(HttpSession session, Model model) {

          Users user = usersRepository.findUserIdByUsername(session.getAttribute("username").toString()).get();
          if (user.getUserId() != -1) {

               List<UserFiles> getAllFiles = getAllFilesByUser(user.getUserId());
               List<UserFiles_Dto> userFiles = new ArrayList<>();

               if (getAllFiles == null) {
                    model.addAttribute("files", userFiles);
                    return "/registrar/documents-list";
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
               return "/registrar/documents-list";

          }

          return null;

     }

     @Override
     public ResponseEntity<Object> displayAllUserAccountByType(String type) {
          List<Users> users = getAllAccountsByType(type);

          if (users != null || !users.isEmpty()) {
               List<Users> storedAccountsWithoutImage = new ArrayList<>();

               for (Users user : users) {
                    storedAccountsWithoutImage
                              .add(new Users(user.getUserId(), user.getName(), user.getUsername(),
                                        user.getPassword(),
                                        user.getType(), user.getStatus()));

               }
               users = storedAccountsWithoutImage;
               ServiceResponse<List<Users>> serviceResponse = new ServiceResponse<>("success", users);
               return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);
          }

          return new ResponseEntity<Object>("Failed to retrieve user accounts.", HttpStatus.BAD_REQUEST);
     }

     @Override
     public boolean changeStatusAndManageByAndMessageOfRequests(String status, String message,
               long userId, long requestId, HttpSession session) {
          Users user = usersRepository.findByuserId(userId).get();
          StudentRequest studentRequest = studentRepository.findOneByRequestByAndRequestId(user, requestId).get();

          if (message.equals("N/A")) {
               message = studentRequest.getReply();
          } else if (studentRequest.getReply() != null) {
               if (studentRequest.getReply().length() > 0) {
                    message = studentRequest.getReply() + "," + message;
               }

          } else if (message == null || message.isEmpty()) {
               message = "";
          }
          if (!status.isEmpty() || !status.isBlank() || session.getAttribute("name") != null) {
               String manageBy = session.getAttribute("name").toString();
               String manageByFromDatabase = globalService.removeDuplicateInManageBy(studentRequest.getManageBy());

               if (!manageByFromDatabase.trim().isEmpty()) {
                    manageBy = globalService.removeDuplicateInManageBy(studentRequest.getManageBy() + "," + manageBy);
               }

               if (user != null && studentRequest != null) {

                    studentRepository.changeStatusAndManagebyAndMessageOfRequests(status,
                              manageBy,
                              message, studentRequest.getRequestId());
                    return true;
               }

          }
          return false;

     }

     @Override
     public ResponseEntity<Object> finalizedRequestsWithFiles(long userId, long requestId,
               Optional<MultipartFile[]> files, Map<String, String> params,
               HttpSession session) {
          try {
               if (files.isEmpty()) {
                    throw new ApiRequestException("Please upload file.");
               }
               LocalDateTime myDateObj = LocalDateTime.now();
               DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
               String formattedDate = myDateObj.format(myFormatObj);
               Users user = usersRepository.findByuserId(userId).get();
               StudentRequest studentRequest = studentRepository.findOneByRequestByAndRequestId(user, requestId).get();
               studentRequest.setReleaseDate(formattedDate);
               studentRequest.setRequestStatus("Approved");
               List<String> excludedFiles = new ArrayList<>();
               studentRepository.save(studentRequest);

               for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (entry.getKey().contains("excluded")) {
                         excludedFiles.add(entry.getValue());
                    }

               }

               Users manageBy = usersRepository.findByUsername(session.getAttribute("username").toString()).get();

               for (MultipartFile filex : files.get()) {

                    if (!excludedFiles.contains(filex.getOriginalFilename())) {
                         UserFiles userFiles = new UserFiles();
                         userFiles.setData(filex.getBytes());
                         userFiles.setName(filex.getOriginalFilename());
                         userFiles.setSize(globalService.formatFileUploadSize(filex.getSize()));
                         userFiles.setDateUploaded(formattedDate);
                         userFiles.setStatus("Approved");
                         userFiles.setFilePurpose("dfs");
                         userFiles.setUploadedBy(manageBy);
                         userFiles.setRequestWith(studentRequest);
                         fileRepository.save(userFiles);

                    }
               }

               return new ResponseEntity<>("Success", HttpStatus.OK);

          } catch (Exception ex) {
               throw new ApiRequestException(ex.getMessage());
          }

     }

     @Override
     public List<Users> getAllAccountsByType(String type) {
          return usersRepository.findAllByType(type);
     }

     @Override
     public ResponseEntity<Object> saveUsersAccount(Users user, int actions) {

          String error = "";
          try {
               user.setPassword(user.getPassword().replaceAll("\\s", ""));
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
               } else {
                    String userIdFormat = user.getUsername().toUpperCase();
                    user.setStatus("Active");
                    if (userIdFormat.contains("C")) {

                         user.setType("Student");
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
                              usersRepository.saveAndFlush(user);
                              ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                              return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                         }
                    } else if (actions == 1) {

                         String hashedPassword = passwordEncoder.encode(user.getPassword());
                         user.setPassword(hashedPassword);
                         usersRepository.saveAndFlush(user);
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
          if (usersRepository.findByUsername(username).isPresent()) {
               Optional<Users> users = usersRepository.findByUsername(username);
               if (users.isPresent()) {
                    return true;
               }
          }
          return false;

     }

     @Override
     public Optional<Users> findOneUserById(long userId) {
          Optional<Users> users = usersRepository.findByuserId(userId);
          if (users.isPresent()) {
               return users;
          }
          return null;
     }

     @Override
     public boolean deleteData(long userId) {

          try {
               if (findOneUserById(userId).isPresent()) {
                    usersRepository.deleteById(userId);
                    return true;
               }
               return false;
          } catch (Exception e) {
               throw new ApiRequestException(
                         "Users with requests cannot be permanently deleted. Please contact the administrator for further assistance.");
          }
     }

     @Override
     public boolean changeAccountStatus(String status, long userId) {
          try {
               if (findOneUserById(userId).isPresent()) {
                    usersRepository.changeStatusOfUser(status, userId);
                    return true;
               }
               return false;
          } catch (Exception e) {
               throw new ApiRequestException(
                         "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");
          }

     }

     @Override
     public boolean saveFiles(UserFiles files) {
          // TODO Auto-generated method stub
          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {
          // TODO Auto-generated method stub
          return null;
     }

     @Override
     public Optional<UserFiles> getFileById(String id) {
          // TODO Auto-generated method stub
          return Optional.empty();
     }

     @Override
     public List<UserFiles> getFilesByRequestWith(StudentRequest sr) {
          // TODO Auto-generated method stub
          return null;
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
               throw new UserNotFoundException("User with id " + userId + " not found", e);
          }
     }

     @Override
     public Boolean deleteFile(long id) {
          // TODO Auto-generated method stub
          return null;
     }

     @Override
     public ResponseEntity<String> sendRequestToTeacher(long userId, HttpSession session, Map<String, String> params) {
          try {
               LocalDateTime myDateObj = LocalDateTime.now();
               DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
               String requestedDate = myDateObj.format(myFormatObj);

               String username = "";
               Users to = findOneUserById(userId).get();
               Users from = new Users();
               if ((username = session.getAttribute("username").toString()) != null) {
                    from = usersRepository.findByUsername(username).get();
               }

               RegistrarRequest registrarRequests = new RegistrarRequest();
               Optional<RegistrarRequest> optionalRegRequest = getRegistrarRequest(to);
               if (to != null) {
                    if (!optionalRegRequest.isEmpty()) {
                         registrarRequests = optionalRegRequest.get();
                    } else {
                         if (!params.isEmpty()) {
                              for (Map.Entry<String, String> parameters : params.entrySet()) {
                                   String key = parameters.getKey().toLowerCase();
                                   if (key.equals("from")) {
                                        registrarRequests.setRequestBy(from);
                                   } else if (key.equals("to")) {
                                        registrarRequests.setRequestTo(to);
                                   } else if (key.equals("message")) {
                                        registrarRequests.setRequestMessage(parameters.getValue());
                                   }
                              }
                              registrarRequests.setRequestStatus("pending");
                              registrarRequests.setRequestDate(requestedDate);
                              regsRepository.save(registrarRequests);
                         }
                    }

                    return new ResponseEntity<>("Success", HttpStatus.OK);
               }
          } catch (Exception e) {
               throw new ApiRequestException(
                         "Failed to sent request, Please Try Again!. Please contact the administrator for further assistance.");
          }
          throw new ApiRequestException(
                    "Failed to sent request, Please Try Again!. Please contact the administrator for further assistance.");
     }

     @Override
     public Optional<RegistrarRequest> getRegistrarRequest(Users user) {

          Optional<RegistrarRequest> registrarRequests = regsRepository.findOneByRequestBy(user);
          if (user != null && !registrarRequests.isEmpty()) {
               return registrarRequests;
          }
          return Optional.empty();
     }

}
