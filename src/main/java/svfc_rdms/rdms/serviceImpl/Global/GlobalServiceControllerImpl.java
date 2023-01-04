package svfc_rdms.rdms.serviceImpl.Global;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import svfc_rdms.rdms.service.Global.GlobalControllerService;

@Service
public class GlobalServiceControllerImpl implements GlobalControllerService {

     @Override
     public boolean validatePages(HttpServletResponse response, HttpSession session) {
          try {
               response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
               response.setHeader("Pragma", "no-cache");
               response.setDateHeader("Expires", 0);
               if (session.getAttribute("username") == null ||
                         session.getAttribute("accountType") == null
                         || session.getAttribute("name") == null) {
                    // If the session is not valid, redirect to the login page
                    response.sendRedirect("/");

               } else {
                    return true;
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
}
