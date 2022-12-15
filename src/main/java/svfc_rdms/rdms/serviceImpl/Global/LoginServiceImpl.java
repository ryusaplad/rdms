package svfc_rdms.rdms.serviceImpl.Global;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     public ResponseEntity<String> login(Users user, HttpSession session, HttpServletResponse response) {

          Users foundUser = loginRepo.findByUsernameAndPasswordAndType(user.getUsername(), user.getPassword(),
                    user.getType());
          try {
               if (foundUser != null) {

                    if (foundUser.getStatus().equals("Active")) {
                         session.setAttribute("studentName", foundUser.getName());
                         session.setAttribute("username", user.getUsername());
                         session.setAttribute("accountType", user.getType());

                         return new ResponseEntity<>("success", HttpStatus.OK);
                    } else {
                         return new ResponseEntity<>("Sorry, This is account is not currently Active.",
                                   HttpStatus.UNAUTHORIZED);
                    }
               } else {
                    return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
               }
          } catch (Exception e) {
               throw new ApiRequestException(e.getMessage());
          }

     }

}
