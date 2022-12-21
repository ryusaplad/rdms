package svfc_rdms.rdms.repository.Student;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

@SpringBootTest
public class StudentRepositoryTest {

     @Autowired
     StudentRepository studRepo;

     @Test
     void testFindAllByRequestByAndRequestId() {

          Users user = new Users();
          user.setUserId(1);
          List<StudentRequest> srOption = studRepo.findAllByRequestByAndRequestId(user, 15);

          srOption.stream().forEach(e -> {
               System.out.println(e.getRequestId());
          });
     }
}
