package svfc_rdms.rdms.serviceImpl.Global;

import java.util.Optional;

import javax.servlet.http.Cookie;
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

                              if (rememberMe.equals("true")) {

                                   manageCookie("login_MyUsername", user.getUsername(), 30, response, "save");
                                   manageCookie("login_MyPassword", user.getPassword(), 30, response, "save");
                                   manageCookie("login_MyAccountType", user.getType(), 30, response, "save");
                                   manageCookie("login_RememberMe", rememberMe, 30, response, "save");

                              } else {

                                   manageCookie("login_MyUsername", user.getUsername(), 0, response, "clear");
                                   manageCookie("login_MyPassword", user.getPassword(), 0, response, "clear");
                                   manageCookie("login_MyAccountType", user.getType(), 0, response, "clear");
                                   manageCookie("login_RememberMe", rememberMe, 0, response, "clear");
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

     private String manageCookie(String cookieName, String cookieValue, int maxAge, HttpServletResponse response,
               String action) {
          Cookie cookie = new Cookie(cookieName, cookieValue);

          if (action.equals("save")) {
               cookie.setMaxAge(maxAge * 24 * 60 * 60);
               cookie.setSecure(true);
               cookie.setHttpOnly(true);
          } else if (action.equals("clear")) {

               cookie.setMaxAge(0);
               cookie.setSecure(true);
               cookie.setHttpOnly(true);
          }
          response.addCookie(cookie);
          return action;
     }

}
