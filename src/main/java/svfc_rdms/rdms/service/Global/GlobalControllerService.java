package svfc_rdms.rdms.service.Global;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface GlobalControllerService {

      boolean validatePages(String validAccount, HttpServletResponse response, HttpSession session);

      String formatFileUploadSize(long size);

      String removeDuplicateInManageBy(String manageBy);

      void downloadFile(String id, Model model, HttpServletResponse response);
      ResponseEntity<String> deleteFile(String id);

      String formattedDate();

      String generateRandomHexColor();

      boolean isValidEmail(String email);

      String getClientIP(HttpServletRequest request);

      void sendTopic(String topic,String payload);

}
