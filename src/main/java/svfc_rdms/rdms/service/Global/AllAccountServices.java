package svfc_rdms.rdms.service.Global;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AllAccountServices {

     ResponseEntity<String> changePassword(String oldPassword, String newPassword, long userId, HttpSession session,HttpServletRequest request);

     ResponseEntity<String> changeProfilePicture(MultipartFile image, long userId, HttpSession session,HttpServletRequest request);


   
}
