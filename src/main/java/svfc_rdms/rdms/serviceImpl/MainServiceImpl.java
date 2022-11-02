package svfc_rdms.rdms.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.adminRepository;
import svfc_rdms.rdms.service.AdminService;

@Service
public class MainServiceImpl implements AdminService {

     @Autowired
     adminRepository repository;

     @Override
     public List<Users> diplayAllAccounts(String type) {
          return repository.findAllByType(type);
     }

     @Override
     public boolean deleteData(long userId) {
          System.out.println(findOneUserById(userId).size());
          if (findOneUserById(userId).size() > 0) {
               repository.deleteById(userId);
               return true;
          }
          return false;
     }

     @Override
     public boolean saveUsersAccount(Users user) {
          if (user != null) {
               repository.saveAndFlush(user);
               return true;
          }
          return false;
     }

     @Override
     public boolean findUserName(String username) {
          List<Users> users = (repository.findByUsername(username));
          if (users.size() > 0) {
               return true;
          }
          return false;
     }

     @Override
     public List<Users> findOneUserById(long id) {
          return repository.findByuserId(id);
     }

     @Override
     public int displayCounts(String type) {
          return repository.countUsers(type);
     }

}
