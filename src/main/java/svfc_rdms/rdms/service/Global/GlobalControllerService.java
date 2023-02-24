package svfc_rdms.rdms.service.Global;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

public interface GlobalControllerService {

      boolean validatePages(String validAccount, HttpServletResponse response, HttpSession session);

      String formatFileUploadSize(long size);

      String removeDuplicateInManageBy(String manageBy);

      void DownloadFile(String id, Model model, HttpServletResponse response);

      String formattedDate();

      String generateRandomHexColor();

}
