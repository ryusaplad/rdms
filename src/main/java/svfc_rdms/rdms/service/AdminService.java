package svfc_rdms.rdms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.Users;

public interface AdminService {

     List<Users> diplayAllAccounts(String status, String type);

     boolean saveUsersAccount(Users user);

     boolean findUserName(String username);

     List<Users> findOneUserById(long userId);

     boolean deleteData(long userId);

     boolean changeAccountStatus(String status, long userId);

     int displayCountsByStatusAndType(String status, String type);

     // Manage Document

     Boolean saveDocumentData(MultipartFile multipartFile, Map<String, String> documentsInfo);

     List<Documents> getAllFiles();

     Optional<Documents> getFileById(long id);

     Boolean deleteFile(long id);

     // File saveMultipleFiles(MultipartFile[] files);

     // String checkFile(MultipartFile file, int maxSize);

}
