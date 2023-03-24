package svfc_rdms.rdms.serviceImpl.Registrar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.service.Registrar.Registrar_AccountService;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Service
public class Reg_AccountServiceImpl implements Registrar_AccountService {

     @Autowired
     private UsersRepository usersRepository;

     @Autowired
     GlobalServiceControllerImpl globalService;
     @Autowired
     private GlobalLogsServiceImpl globalLogsServiceImpl;

     @Override
     public ResponseEntity<Object> saveUsersAccount(Users user, int actions, HttpSession session) {

          String error = "";
          try {

               user.setPassword(user.getPassword().replaceAll("\\s", ""));
               PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
               if (user.getName() == null || user.getName().length() < 1 || user.getName().isEmpty()) {
                    error = "Name cannot be empty! " + user.getName();
                    throw new ApiRequestException(error);
               } else if (user.getUsername() == null || user.getUsername().length() < 1
                         || user.getUsername().isEmpty()) {
                    error = "Username cannot be empty " + user.getUsername();
                    throw new ApiRequestException(error);
               } else if (user.getPassword() == null || user.getPassword().length() < 1
                         || user.getPassword().isEmpty()) {
                    error = "Password cannot be empty" + user.getPassword();
                    throw new ApiRequestException(error);
               } else if (user.getUsername().length() > 255 || user.getName().length() > 255
                         || user.getPassword().length() > 255) {
                    throw new ApiRequestException("Data/Information is too long, Please Try Again!");
               } else if (user.getPassword().length() < 8) {
                    return new ResponseEntity<>("Password must be at least 8 characters long",
                              HttpStatus.BAD_REQUEST);
               } else if (!user.getPassword()
                         .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$")) {
                    return new ResponseEntity<>(
                              "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, and 1 number",
                              HttpStatus.BAD_REQUEST);
               } else {

                    String userIdFormat = user.getUsername().toUpperCase();
                    user.setStatus("Active");
                    // Setting Default Color Code in hex
                    String randomColorCode = globalService.generateRandomHexColor();
                    user.setColorCode(randomColorCode);

                    if (userIdFormat.contains("C")) {

                         user.setType("Student");
                    } else if (userIdFormat.contains("T-")) {

                         user.setType("Teacher");
                    } else {
                         error = "Account Type Invalid, Please try again!";

                         throw new ApiRequestException(error);
                    }
                    if (actions == 0) {
                         if (findUserName(userIdFormat.toLowerCase())) {
                              error = "Username is already taken, Please try again!";

                              throw new ApiRequestException(error);
                         } else {
                              String hashedPassword = passwordEncoder.encode(user.getPassword());
                              user.setPassword(hashedPassword);
                              byte[] image = new byte[] { 0 };
                              user.setProfilePicture(image);
                              usersRepository.saveAndFlush(user);
                              ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                              String date = LocalDateTime.now().toString();
                              String logMessage = "[" + date + "] Account added Successfully! User: "
                                        + session.getAttribute("name").toString() + " added a user (" + user.getName()
                                        + ")";
                              globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "Normal", session);
                              return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                         }
                    } else if (actions == 1) {

                         String hashedPassword = passwordEncoder.encode(user.getPassword());
                         user.setPassword(hashedPassword);
                         usersRepository.saveAndFlush(user);
                         ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                         String date = LocalDateTime.now().toString();
                         String logMessage = "[" + date + "] Account Updated Successfully! User: "
                                   + session.getAttribute("name").toString() + " updated a user (" + user.getName()
                                   + ")";
                         globalLogsServiceImpl.saveLog(0, logMessage, "Mid_Log", date, "medium", session);
                         return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                    }
               }
          } catch (Exception e) {
               error = e.getMessage();
               if (error.contains("ConstraintViolationException")) {

                    error = "Username is already taken, Please try again!";
                    throw new ApiRequestException(error);
               }
          }
          throw new ApiRequestException(error);
     }

     @Override
     public boolean findUserName(String username) {
          if (usersRepository.findByUsername(username).isPresent()) {
               Optional<Users> users = usersRepository.findByUsername(username);
               if (users.isPresent()) {
                    return true;
               }
          }
          return false;

     }

     @Override
     public Optional<Users> findOneUserById(long userId) {
          Optional<Users> users = usersRepository.findByuserId(userId);
          if (users.isPresent()) {
               return users;
          }
          return null;
     }

     @Override
     public boolean deleteData(long userId, HttpSession session) {

          try {
               if (findOneUserById(userId).isPresent()) {
                    usersRepository.deleteById(userId);
                    String date = LocalDateTime.now().toString();
                    String logMessage = "[" + date + "] Account Deleted Successfully! User: "
                              + session.getAttribute("name").toString() + " deleted a user ("
                              + findOneUserById(userId).get().getName() + ")";
                    globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session);
                    return true;
               }
               return false;
          } catch (Exception e) {
               throw new ApiRequestException(
                         "Users with requests cannot be permanently deleted. Please contact the administrator for further assistance.");
          }
     }

     @Override
     public boolean changeAccountStatus(String status, long userId, HttpSession session) {
          try {
               if (findOneUserById(userId).isPresent()) {
                    usersRepository.changeStatusOfUser(status, userId);
                    return true;
               }
               return false;
          } catch (Exception e) {
               throw new ApiRequestException(
                         "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");
          }

     }

     @Override
     public ResponseEntity<Object> displayAllUserAccountByType(String type) {
          List<Users> users = getAllAccountsByType(type);

          if (users != null) {
               List<Users> storedAccountsWithoutImage = new ArrayList<>();

               for (Users user : users) {
                    storedAccountsWithoutImage
                              .add(new Users(user.getUserId(), user.getName(), user.getUsername(),
                                        user.getPassword(),
                                        user.getType(), user.getStatus()));

               }
               users = storedAccountsWithoutImage;
               ServiceResponse<List<Users>> serviceResponse = new ServiceResponse<>("success", users);
               return new ResponseEntity<Object>(serviceResponse, HttpStatus.OK);
          }

          return new ResponseEntity<Object>("Failed to retrieve user accounts.", HttpStatus.BAD_REQUEST);
     }

     @Override
     public List<Users> getAllAccountsByType(String type) {
          return usersRepository.findAllByType(type);
     }
}
