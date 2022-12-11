package svfc_rdms.rdms.service.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface AdminService {

     List<Users> diplayAllAccounts(String status, String type);

     boolean saveUsersAccount(Users user);

     boolean findUserName(String username);

     List<Users> findOneUserById(long userId);

     boolean deleteData(long userId);

     boolean changeAccountStatus(String status, long userId);

     int displayCountsByStatusAndType(String status, String type);

     List<StudentRequest> displayRequestByStatus(String status);

     Boolean saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo);

     Boolean saveDocumentData(long id, MultipartFile multipartFile, Map<String, String> documentsInfo);

     List<Documents> getAllDocuments();

     List<String> getAllDocumentTitles();

     Optional<Documents> getFileById(long id);

     Boolean deleteFile(long id);

     // Get All Request of Students
     List<StudentRequest> displayAllRequest();

}
