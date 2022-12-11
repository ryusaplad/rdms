package svfc_rdms.rdms.service.Student;

import java.util.List;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentService {
     boolean saveRequest(StudentRequest request);

     List<StudentRequest> findRequestByUserID(long id);

     List<Users> findUsernameByUserID(long id);

     Boolean findDocumentByTitle(String title);

}
