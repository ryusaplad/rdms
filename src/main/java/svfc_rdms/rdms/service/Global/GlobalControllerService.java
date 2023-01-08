package svfc_rdms.rdms.service.Global;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface GlobalControllerService {

     public boolean validatePages(String validAccount, HttpServletResponse response, HttpSession session);

     public String formatFileUploadSize(long size);

     public String removeDuplicateInManageBy(String manageBy);

}
