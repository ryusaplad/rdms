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
     private UsersRepository userRepository;

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
                              .toLowerCase().equals(validAccount)) ? true : false;

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
          }finally{
               try {
                    
                    response.getOutputStream().close();
               } catch (Exception e) {
                   e.printStackTrace();
               }
          }
     }

     @Override
     public ResponseEntity<String> deleteFile(String id) {
          try {
               String stringValue = id.toString();
               UUID uuidValue = UUID.fromString(stringValue);
               UserFiles newUserFiles = null;
               Optional<UserFiles> fileOptional = fileRepository.findById(uuidValue);
               if (!fileOptional.isPresent()) {
                    return new ResponseEntity<String>("failed to delete this file", HttpStatus.OK);
               }
               newUserFiles = fileOptional.get();

               Optional<Users> studentRequestWith = userRepository
                         .findById(newUserFiles.getRequestWith().getRequestBy().getUserId());
               Optional<Users> userRequestBy = userRepository
                         .findById(newUserFiles.getUploadedBy().getUserId());
               Optional<Users> registrarRequestWith = userRepository
                         .findById(newUserFiles.getUploadedBy().getUserId());

               // cleaning associate keys and Reject any associated requests
               if (studentRequestWith.isPresent()) {
                    List<StudentRequest> studentRequest = studentRequestRepository
                              .findAllByRequestBy(studentRequestWith.get());

                    List<UserFiles> userFiles = fileRepository.findAllByRequestWith(newUserFiles.getRequestWith());

                    // Create a map to group UserFiles by StudentRequest
                    Map<StudentRequest, List<UserFiles>> filesByRequest = userFiles.stream()
                              .collect(Collectors.groupingBy(UserFiles::getRequestWith));

                    // Loop over the StudentRequest objects and update their properties based on the
                    // associated UserFiles
                    for (StudentRequest studRequest : studentRequest) {
                         List<UserFiles> associatedFiles = filesByRequest.get(studRequest);
                         if (associatedFiles != null && !associatedFiles.isEmpty()) {
                              studRequest.setManageBy("Admin");
                              studRequest.setRequestStatus("Rejected");
                              studentRequestRepository.save(studRequest);
                         }
                    }
                    newUserFiles.setRequestWith(null);
               }
               if (userRequestBy.isPresent()) {
                    newUserFiles.setUploadedBy(null);
               }
               // cleaning associate keys and Reject any associated requests
               if (registrarRequestWith.isPresent()) {
                    List<RegistrarRequest> registrarRequest = regsRequestRepository
                              .findAllByRequestBy(registrarRequestWith.get(), null);

                    List<UserFiles> userFiles = fileRepository.findAllByRequestWith(newUserFiles.getRequestWith());

                    // Create a map to group UserFiles by RegistrarRequest
                    Map<RegistrarRequest, List<UserFiles>> filesByRequest = userFiles.stream()
                              .collect(Collectors.groupingBy(UserFiles::getRegRequestsWith));

                    // Loop over the RegistrarRequest objects and update their properties based on
                    // the
                    // associated UserFiles
                    for (RegistrarRequest regRequest : registrarRequest) {
                         List<UserFiles> associatedFiles = filesByRequest.get(regRequest);
                         if (associatedFiles != null && !associatedFiles.isEmpty()) {

                              regsRequestRepository.save(regRequest);
                         }
                    }
                    newUserFiles.setRequestWith(null);
                    newUserFiles.setRegRequestsWith(null);
               }

               fileRepository.save(newUserFiles);
               fileRepository.deleteById(uuidValue);
               return new ResponseEntity<String>("success", HttpStatus.OK);

          } catch (Exception e) {
               throw new ApiRequestException("Failed to delete this file Reason: " + e.getMessage());
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
