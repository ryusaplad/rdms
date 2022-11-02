package svfc_rdms.rdms.service;

import java.util.List;

import svfc_rdms.rdms.model.Users;

public interface AdminService {

     List<Users> diplayAllAccounts(String type);

     boolean saveUsersAccount(Users user);

     boolean findUserName(String username);

     List<Users> findOneUserById(long id);

     boolean deleteData(long id);

     int displayCounts(String type);

}
