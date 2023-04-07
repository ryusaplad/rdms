package svfc_rdms.rdms.controller.RestControllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.serviceImpl.Global.LoginServiceImpl;

@RestController
public class LoginRestController {

     @Autowired
     LoginServiceImpl loginService;

     @PostMapping("/login")

     public ResponseEntity<String> accountLogin(@RequestBody Users user, @RequestParam String rememberMe,
               HttpSession session,
               HttpServletResponse response, HttpServletRequest request) {

          return loginService.login(user, rememberMe, session, response,request);
     }

}
