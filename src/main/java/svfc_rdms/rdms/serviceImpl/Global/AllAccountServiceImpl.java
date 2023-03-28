package svfc_rdms.rdms.serviceImpl.Global;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.service.Global.AllAccountServices;

@Service
public class AllAccountServiceImpl implements AllAccountServices {

     @Autowired
     private UsersRepository userRepository;
     @Autowired
     private GlobalLogsServiceImpl globalLogsServiceImpl;

     @Override
     public ResponseEntity<String> changePassword(String oldPassword, String newPassword, long userId,
               HttpSession session) {
          Optional<Users> optionalUser = userRepository.findById(userId);

          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

          if (newPassword.length() < 8) {
               return new ResponseEntity<>("New password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
          }

          if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$")) {
               return new ResponseEntity<>(
                         "New password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, and 1 number",
                         HttpStatus.BAD_REQUEST);
          } else {
               oldPassword = oldPassword.replaceAll("\\s", "");
               newPassword = newPassword.replaceAll("\\s", "");
          }

          if (oldPassword.isBlank() || newPassword.isBlank()) {
               return new ResponseEntity<>("Please fill in all the required inputs.", HttpStatus.BAD_REQUEST);
          }

          if (optionalUser.isPresent()) {
               Users user = optionalUser.get();
               boolean isOldPasswordValid = passwordEncoder.matches(oldPassword, user.getPassword());

               if (isOldPasswordValid) {
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(hashedPassword);

                    if (user.getProfilePicture()[0] == 48) {
                         byte[] profilePicture = new byte[1];
                         profilePicture[0] = 0;
                         user.setProfilePicture(profilePicture);
                    }
                    userRepository.save(user);
                    String date = LocalDateTime.now().toString();
                    String logMessage = "Password changed for user " + user.getName() + ".";
                    globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "Normal", session);
                    return new ResponseEntity<>("Password updated.", HttpStatus.OK);
               } else {
                    return new ResponseEntity<>("Old password is incorrect.", HttpStatus.BAD_REQUEST);
               }
          }

          return new ResponseEntity<>("You are performing an invalid action, please try again.",
                    HttpStatus.BAD_REQUEST);
     }

     @Override
     public ResponseEntity<String> changeProfilePicture(MultipartFile image, long userId, HttpSession session) {
          try {

               Optional<Users> optionalUser = userRepository.findById(userId);
               if (image == null || image.getSize() == 0) {
                    byte[] byteImage = new byte[] { 0 };
                    Users user = optionalUser.get();
                    user.setProfilePicture(byteImage);
                    userRepository.save(user);
                    String date = LocalDateTime.now().toString();
                    String logMessage = "Profile picture changed for user " + user.getName() + ".";
                    globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "Normal", session);
                    return new ResponseEntity<>("Profile picture successfully cleared", HttpStatus.OK);

               } else {

                    if (optionalUser.isPresent()) {
                         byte[] bytes = image.getBytes();
                         Users user = optionalUser.get();
                         user.setProfilePicture(bytes);
                         userRepository.save(user);
                         String date = LocalDateTime.now().toString();
                         String logMessage = "Profile picture changed for user " + user.getName() + ".";
                         globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "Normal", session);
                         return new ResponseEntity<>("Profile picture successfully changed!", HttpStatus.OK);
                    }
               }
          } catch (IOException e) {
               return new ResponseEntity<>(
                         "Failed to change profile picture. The selected file is not a valid image. Please choose a valid image file and try again.",
                         HttpStatus.INTERNAL_SERVER_ERROR);
          }
          return new ResponseEntity<>("Profile picture not found for the specified user. No changes were made.",
                    HttpStatus.BAD_REQUEST);
     }

}
