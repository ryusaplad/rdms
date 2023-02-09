package svfc_rdms.rdms.serviceImpl.Global;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.LoginRepository;
import svfc_rdms.rdms.service.Global.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

     @Autowired
     LoginRepository loginRepo;

     @Override
     public ResponseEntity<String> login(Users user, String rememberMe, HttpSession session,
               HttpServletResponse response) {

          String hashPassword = loginRepo.findPasswordByUsername(user.getUsername())
                    .map(Users::getPassword)
                    .orElseThrow(() -> new ApiRequestException("Invalid username or password"));

          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          boolean isPasswordValid = passwordEncoder.matches(user.getPassword(), hashPassword);
          if (isPasswordValid) {
               Optional<Users> optional_User = loginRepo.findByUsernameAndPasswordAndType(user.getUsername(),
                         hashPassword,
                         user.getType());
               try {
                    Users foundUser = optional_User.get();
                    if (foundUser != null) {

                         if (foundUser.getStatus().equals("Active")) {
                              System.out.println(rememberMe);
                              if (rememberMe.equals("true")) {
                                   session.setAttribute("session_username", user.getUsername());
                                   session.setAttribute("session_password", user.getPassword());
                                   session.setAttribute("session_accType", user.getType());
                                   session.setAttribute("session_remember", rememberMe);
                              } else {
                                   session.removeAttribute("session_username");
                                   session.removeAttribute("session_password");
                                   session.removeAttribute("session_accType");
                                   session.removeAttribute("session_remember");
                              }
                              session.setAttribute("name", foundUser.getName());
                              session.setAttribute("username", user.getUsername());
                              session.setAttribute("accountType", user.getType());

                              return new ResponseEntity<>("success", HttpStatus.OK);
                         } else {
                              return new ResponseEntity<>("Sorry, This is account is not currently Active.",
                                        HttpStatus.UNAUTHORIZED);
                         }
                    } else {
                         return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
                    }
               } catch (Exception e) {
                    return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
               }

          } else

          {
               return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
          }
     }

}
