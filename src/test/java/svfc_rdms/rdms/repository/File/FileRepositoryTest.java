package svfc_rdms.rdms.repository.File;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@SpringBootTest
public class FileRepositoryTest {
     @Autowired
     StudentRepository studRepo;
     @Autowired
     StudentServiceImpl studService;

     @Test
     void testFindAllByRequestWith() {
          Users user = new Users();
          user.setUserId(1);
          List<StudentRequest> srOption = studRepo.findAllByRequestByAndRequestId(user, 15);

          srOption.stream().forEach(e -> {
               System.out.println(e.getRequestId());
               StudentRequest studentRequest = studRepo.findById(e.getRequestId()).get();
               List<UserFiles> ufOptional = studService.getFilesByRequestWith(studentRequest);
               ufOptional.stream().forEach(ec -> {
                    System.out.println(ec.getFileId());
               });
          });

     }
}
