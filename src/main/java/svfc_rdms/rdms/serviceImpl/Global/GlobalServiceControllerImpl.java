package svfc_rdms.rdms.serviceImpl.Global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.service.Global.GlobalControllerService;

@Service
public class GlobalServiceControllerImpl implements GlobalControllerService {

     @Autowired
     private FileRepository fileRepository;

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
     public void DownloadFile(String id, Model model, HttpServletResponse response) {
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
          }
     }

     @Override
     public String formattedDate() {
          LocalDateTime myDateObj = LocalDateTime.now();
          DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
          String formattedDate = myDateObj.format(myFormatObj);
          return formattedDate;
     }

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

     public boolean isValidEmail(String email) {
          String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\." + "[a-zA-Z0-9_+&-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";
          Pattern pattern = Pattern.compile(emailRegex);
          return pattern.matcher(email).matches();
     }
}
