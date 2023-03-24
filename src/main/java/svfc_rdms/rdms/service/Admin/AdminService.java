package svfc_rdms.rdms.service.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;

public interface AdminService {

     // DefaultAdmin
     void ensureDefaultAdminUserExists();

     // DefaultAccounts
     void ensureDefaultAccountUsersExist();

     // DefaultDocuments
     void ensureDefaultDocumentsExist();

     List<Users> diplayAllAccounts(String status, String type);

     List<Users> diplayAllAccountsByType(String type);

     ResponseEntity<Object> saveUsersAccount(Users user, int actions);

     boolean findUserName(String username);

     Optional<Users> findOneUserById(long userId);

     boolean deleteData(long userId,HttpSession session);

     boolean changeAccountStatus(String status, long userId,HttpSession session);

     int displayCountsByStatusAndType(String status, String type);

     List<StudentRequest> displayRequestByStatus(String status);

     ResponseEntity<Object> saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo,HttpSession session);

     ResponseEntity<Object> saveDocumentData(long id, MultipartFile multipartFile, Map<String, String> documentsInfo,HttpSession session);

     List<Documents> getAllDocuments();

     List<Documents> getAllDocumentsByStatus(boolean status);

     List<String> getAllDocumentTitles();

     Optional<Documents> getFileDocumentById(long id);

     Boolean deleteDocumentFile(long id,HttpSession session);

     // Get All Request of Students
     List<StudentRequest> displayAllRequest();

     // Get All Request of registrars
     ResponseEntity<Object> viewAllRegistrarRequests(long requestsId);

     // get all user files;

     Optional<UserFiles> getFileById(String id);

}
