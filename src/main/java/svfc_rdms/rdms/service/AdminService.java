package svfc_rdms.rdms.service;

import java.util.List;

import svfc_rdms.rdms.model.Users;

public interface AdminService {

     List<Users> diplayAllAccounts(String status, String type);

     boolean saveUsersAccount(Users user);

     boolean findUserName(String username);

     List<Users> findOneUserById(long userId);

     boolean deleteData(long userId);

     boolean changeAccountStatus(String status, long userId);

     int displayCountsByStatusAndType(String status, String type);

}
