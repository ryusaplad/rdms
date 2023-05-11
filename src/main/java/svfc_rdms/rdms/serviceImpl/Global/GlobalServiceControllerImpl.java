package svfc_rdms.rdms.serviceImpl.Global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.UserFiles_Dto;
import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.RegistrarRequests.RegsRequestRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Global.GlobalControllerService;

@Service
public class GlobalServiceControllerImpl implements GlobalControllerService {

     @Autowired
     private FileRepository fileRepository;

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     private StudentRepository studentRequestRepository;

     @Autowired
     private RegsRequestRepository regsRequestRepository;
     // WebSocket
     @Autowired
     private SimpMessagingTemplate messagingTemplate;

     @Override
     public boolean validatePages(String validAccount, HttpServletResponse response, HttpSession session) {
          try {
               response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
               response.setHeader("Pragma", "no-cache");
               response.setDateHeader("Expires", 0);
               if (session.getAttribute("username") == null ||
                         session.getAttribute("accountType") == null
                         || session.getAttribute("name") == null) {
                    return false;

               } else {

                    boolean verifyAccountType = (session.getAttribute("accountType").toString()
                              .toLowerCase().trim().equalsIgnoreCase(validAccount)) ? true : false;
                    return verifyAccountType;
               }

          } catch (Exception e) {
               e.printStackTrace();
          }
          return false;
     }

     @Override
     public String formatFileUploadSize(long size) {

          final long kilo = 1024;
          final long mega = kilo * kilo;
          final long giga = mega * kilo;
          final long tera = giga * kilo;

          double kb = size / kilo;
          double mb = kb / kilo;
          double gb = mb / kilo;
          double tb = gb / kilo;
          String formatedSize = "";
          if (size < kilo) {
               formatedSize = size + " Bytes";
          } else if (size >= kilo && size < mega) {
               formatedSize = String.format("%.2f", kb) + " KB";
          } else if (size >= mega && size < giga) {
               formatedSize = String.format("%.2f", mb) + " MB";
          } else if (size >= giga && size < tera) {
               formatedSize = String.format("%.2f", gb) + " GB";
          } else if (size >= tera) {
               formatedSize = String.format("%.2f", tb) + " TB";
          }
          return formatedSize;

     }

     @Override
     public String removeDuplicateInManageBy(String manageBy) {
          String[] manageByArray = manageBy.split(",");
          for (int i = 0; i < manageByArray.length; i++) {
               manageByArray[i] = manageByArray[i].trim();
          }
          Set<String> set = new HashSet<>(Arrays.asList(manageByArray));
          String[] arrayWithoutDuplicates = set.toArray(new String[0]);

          String stringwithoutDuplicate = "";

          for (String string : arrayWithoutDuplicates) {
               stringwithoutDuplicate += string + ",";
          }

          int index = stringwithoutDuplicate.toString().lastIndexOf(",");

          return stringwithoutDuplicate.substring(0, index);
     }

     @Override
     public void downloadFile(String id, Model model, HttpServletResponse response) {
          try {
               String stringValue = id.toString();
               UUID uuidValue = UUID.fromString(stringValue);
               Optional<UserFiles> fileOptional = fileRepository.findById(uuidValue);
               Optional<UserFiles> temp = fileOptional;
               if (temp != null) {
                    UserFiles file = temp.get();

                    if (response.getHeader("Content-Disposition") == null) {
                         response.setContentType("application/octet-stream");
                         response.setHeader("Content-Disposition",
                                   "attachment; filename = " + file.getName().replace(",", "."));
                    }

                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(file.getData());
                    outputStream.close();

               }
          } catch (Exception e) {
               throw new ApiRequestException("Failed to download Reason: " + e.getMessage());
          } finally {
               try {

                    response.getOutputStream().close();
               } catch (Exception e) {
                    e.printStackTrace();
               }
          }
     }

     @Override
     public ResponseEntity<String> deleteFile(String fileId) {
          try {
               UUID uuid = UUID.fromString(fileId);

               Optional<UserFiles> optionalFile = fileRepository.findById(uuid);
               if (!optionalFile.isPresent()) {
                    return ResponseEntity
                              .status(HttpStatus.OK)
                              .body("Failed to delete file: file not found");
               }
               UserFiles file = optionalFile.get();

               Optional<Users> optionalRequestWith = Optional.ofNullable(file.getRequestWith())
                         .map(StudentRequest::getRequestBy)
                         .flatMap(requestBy -> usersRepository.findById(requestBy.getUserId()));
               Optional<Users> optionalUploadedBy = Optional.ofNullable(file.getUploadedBy())
                         .flatMap(uploadedBy -> usersRepository.findById(uploadedBy.getUserId()));

               // Reject any associated student requests and clean up references
               optionalRequestWith.ifPresent(requestWith -> {
                    List<StudentRequest> studentRequests = studentRequestRepository.findAllByRequestBy(requestWith);
                    List<UserFiles> userFiles = fileRepository.findAllByRequestWith(file.getRequestWith());

                    // Create a map to group UserFiles by StudentRequest
                    Map<StudentRequest, List<UserFiles>> filesByRequest = userFiles.stream()
                              .collect(Collectors.groupingBy(UserFiles::getRequestWith));

                    // Loop over the StudentRequest objects and clean them up
                    for (StudentRequest studentRequest : studentRequests) {
                         List<UserFiles> associatedFiles = filesByRequest.get(studentRequest);
                         if (associatedFiles != null && !associatedFiles.isEmpty()) {
                              studentRequest.setManageBy("Admin");
                              studentRequest.setRequestStatus("Rejected");
                              studentRequestRepository.save(studentRequest);
                         }
                    }
                    file.setRequestWith(null);
               });

               // Clean up uploadedBy reference
               optionalUploadedBy.ifPresent(uploadedBy -> file.setUploadedBy(null));

               // Clean up references to registrar requests
               Optional.ofNullable(file.getRegRequestsWith())
                         .ifPresent(regRequestsWith -> {
                              List<RegistrarRequest> registrarRequests = regsRequestRepository
                                        .findAllByRequestBy(regRequestsWith.getRequestBy(), null);

                              List<UserFiles> userFiles = fileRepository
                                        .findAllByRegRequestsWith(file.getRegRequestsWith());

                              // Create a map to group UserFiles by RegistrarRequest
                              Map<RegistrarRequest, List<UserFiles>> filesByRequest = userFiles.stream()
                                        .collect(Collectors.groupingBy(UserFiles::getRegRequestsWith));

                              // Loop over the RegistrarRequest objects and update their properties based on
                              // the associated UserFiles
                              for (RegistrarRequest registrarRequest : registrarRequests) {
                                   List<UserFiles> associatedFiles = filesByRequest.get(registrarRequest);
                                   if (associatedFiles != null && !associatedFiles.isEmpty()) {
                                        regsRequestRepository.save(registrarRequest);
                                   }
                              }
                              file.setRegRequestsWith(null);
                         });

               // Save the updated file and delete it
               fileRepository.save(file);
               fileRepository.deleteById(uuid);
               return ResponseEntity
                         .status(HttpStatus.OK)
                         .body("File deleted successfully");

          } catch (IllegalArgumentException e) {
               throw new ApiRequestException("Failed to delete file: invalid UUID provided");
          } catch (Exception e) {
               throw new ApiRequestException("Failed to delete file: " + e.getMessage());
          }
     }

     @Override
     public ResponseEntity<Object> getFileInformations(String id) {
          try {
               String stringValue = id.toString();
               UUID uuidValue = UUID.fromString(stringValue);
               Optional<UserFiles> fileOptional = fileRepository.findById(uuidValue);

               if (fileOptional.isPresent()) {
                    UserFiles file = fileOptional.get();
                    UserFiles_Dto fileData = new UserFiles_Dto(file.getFileId(), file.getData(), file.getName(),
                              file.getSize(), file.getStatus(), file.getDateUploaded(),
                              file.getFilePurpose(),
                              file.getUploadedBy().getName());

                    return new ResponseEntity<>(fileData, HttpStatus.OK);
               } else {
                    throw new ApiRequestException("File not found.");
               }
          } catch (Exception e) {
               throw new ApiRequestException("Failed to get file. Reason: " + e.getMessage());
          }
     }

     @Override
     public String formattedDate() {
          LocalDateTime myDateObj = LocalDateTime.now();
          DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
          String formattedDate = myDateObj.format(myFormatObj);
          return formattedDate;
     }

     @Override
     public String generateRandomHexColor() {
          Random random = new Random();

          int maxBound = 256;
          int maxTotalBound = 550;

          int red = random.nextInt(maxBound);
          int green = random.nextInt(maxBound);
          int blue = random.nextInt(maxBound);
          // Ensure that the color is not too light
          while ((red + green + blue) > maxTotalBound) {
               red = random.nextInt(maxBound);
               green = random.nextInt(maxBound);
               blue = random.nextInt(maxBound);
          }
          return String.format("#%02x%02x%02x", red, green, blue);
     }

     @Override
     public boolean isValidEmail(String email) {
          String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\." + "[a-zA-Z0-9_+&-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";
          Pattern pattern = Pattern.compile(emailRegex);
          return pattern.matcher(email).matches();
     }

     @Override
     public String getClientIP(HttpServletRequest request) {
          String xForwardedForHeader = request.getHeader("X-Forwarded-For");
          if (xForwardedForHeader == null) {
               return request.getRemoteAddr();
          } else {
               return xForwardedForHeader.split(",")[0].trim();
          }
     }

     // Send Data to Web Socket
     @Override
     public void sendTopic(String topic, String payload) {
          messagingTemplate.convertAndSend(topic, payload);
     }
}
