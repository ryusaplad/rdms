package svfc_rdms.rdms.service.Global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AllAccountServices {

     ResponseEntity<String> changePassword(String oldPassword, String newPassword, long userId);

     ResponseEntity<String> changeProfilePicture(MultipartFile image, long userId);

}
