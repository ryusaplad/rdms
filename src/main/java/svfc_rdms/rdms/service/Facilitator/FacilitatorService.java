package svfc_rdms.rdms.service.Facilitator;

import org.springframework.ui.Model;

public interface FacilitatorService {

     String displayAllStudentRequest(Model model);

     boolean changeStatusAndManageByAndMessageOfRequests(String status, String manageBy,String message, long userId);
}
