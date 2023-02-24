package svfc_rdms.rdms.controller.RestControllers;

import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.serviceImpl.Global.AllAccountServiceImpl;

@RestController
public class AllAccount_RestController {

     @Autowired
     AllAccountServiceImpl accountServiceImpl;

     @Autowired
     UsersRepository usersRepository;

     @PostMapping("/change/account/password")
     public ResponseEntity<String> changePassword(@RequestParam("oldPass") String oldPassword,
               @RequestParam("newPass") String newPassword, @RequestParam("confirmPass") String confirmPassword,
               HttpSession session, HttpServletResponse response) {
          long userId = 0;
          String username = "";
          if ((username = session.getAttribute("username").toString()) != null) {
               Optional<Users> optionalUser = usersRepository.findByUsername(username);
               if (optionalUser.isPresent()) {
                    userId = optionalUser.get().getUserId();
               }
               return accountServiceImpl.changePassword(oldPassword, newPassword, userId);
          }
          throw new ApiRequestException("Invalid Action");

     }

     @PostMapping("/change/account/profile-picture")
     public ResponseEntity<String> changeProfilePicture(@RequestParam MultipartFile image, HttpSession session) {
          long userId = 0;
          String username = "";
          if ((username = session.getAttribute("username").toString()) != null) {
               Optional<Users> optionalUser = usersRepository.findByUsername(username);
               if (optionalUser.isPresent()) {
                    userId = optionalUser.get().getUserId();
               }
               return accountServiceImpl.changeProfilePicture(image, userId);
          }
          throw new ApiRequestException("Invalid Action");
     }

     @GetMapping("/session/{sessionKey}")
     public ResponseEntity<String> getSessionValue(@PathVariable("sessionKey") String sessionKey, HttpSession session) {
          return new ResponseEntity<>((String) session.getAttribute(sessionKey), HttpStatus.OK);
     }

     @GetMapping("/load/image")
     public ResponseEntity<Object> loadImage(@RequestParam("username") String username, HttpSession session) {
          Optional<Users> user = usersRepository.findByUsername(username);
          try {
               byte[] image = null;
               if (!username.isEmpty() || !username.isBlank()) {

                    if (user.isPresent()) {
                         image = user.get().getProfilePicture();

                         // if image is empty return the name and color code to use in the ajax call. to
                         // produce avatar using name and color code.
                         if (image[0] == 48 || image == null || image[0] == 0) {
                              String[] nameData = { user.get().getName(), user.get().getColorCode() };
                              return new ResponseEntity<>(nameData, HttpStatus.NOT_FOUND);
                         }

                         byte[] encodeBase64 = Base64.getEncoder().encode(image);
                         String base64Encoded = new String(encodeBase64, "UTF-8");
                         return new ResponseEntity<>(base64Encoded, HttpStatus.OK);
                    }
                    throw new ApiRequestException("User not found!, Please Try Again");
               }
               throw new ApiRequestException("Username not found!, Please Try Again");
          } catch (Exception e) {
               String[] nameData = { user.get().getName(), user.get().getColorCode() };
               return new ResponseEntity<>(nameData, HttpStatus.NOT_FOUND);
          }
     }

     @GetMapping("/load/user-profile-info")
     public ResponseEntity<Object> profileInfo(@RequestParam("username") String username, HttpSession session) {

          try {

               if (!username.isEmpty() || !username.isBlank()) {
                    Optional<Users> optionalUser = usersRepository.findByUsername(username);
                    if (optionalUser.isPresent()) {
                         // return only name and color code.
                         String[] nameData = { optionalUser.get().getName(), optionalUser.get().getColorCode() };
                         return new ResponseEntity<>(nameData, HttpStatus.OK);
                    }
                    throw new ApiRequestException("User not found!, Please Try Again");
               }
               throw new ApiRequestException("Username not found!, Please Try Again");
          } catch (Exception e) {
               throw new ApiRequestException("Profile Picture Data not found!, Please Try Again");
          }
     }
}
