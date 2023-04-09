package svfc_rdms.rdms.serviceImpl.Registrar;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
     public ResponseEntity<Object> displayAllUserAccountByType(String type) {
          List<Users> users = getAllAccountsByType(type);

          if (users != null) {
               List<Users> storedAccountsWithoutImage = new ArrayList<>();

               for (Users user : users) {
                    storedAccountsWithoutImage
                              .add(new Users(user.getUserId(), user.getName(), user.getEmail(), user.getUsername(),
                                        user.getPassword(),
                                        user.getType(), user.getStatus(), "displaying"));

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
