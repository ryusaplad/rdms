package svfc_rdms.rdms.service.Student;

import java.util.List;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface StudentService {
     boolean saveRequest(StudentRequest request);

     Boolean findDocumentByTitle(String title);

     List<StudentRequest> displayRequestByStudent(Users user);

     Users getUserIdByUsername(String username);

}
