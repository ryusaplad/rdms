package svfc_rdms.rdms.repository.Student;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentRepositoryTest {

     @Autowired
     StudentRepository studRepo;

     @Test
     void testFindAllByRequestByAndRequestId() {

          // Users user = new Users();
          // user.setUserId(1);
          // List<StudentRequest> srOption = studRepo.findAllByRequestByAndRequestId(user,
          // 15);

          // srOption.stream().forEach(e -> {
          // System.out.println(e.getRequestId());
          // });

     }

     @Test
     void testPreventionForDuplicationStringFromTheArrays() {
          String manageBy = " Facilitator " + " , " + " Registrar " + " , " + "  Registrar ";
          String[] manageByArray = manageBy.split(",");
          for (int i = 0; i < manageByArray.length; i++) {
               manageByArray[i] = manageByArray[i].trim();
          }
          Set<String> set = new HashSet<>(Arrays.asList(manageByArray));
          String[] arrayWithoutDuplicates = set.toArray(new String[0]);

          String stringwithoutDuplicate = "";
          for (String string : arrayWithoutDuplicates) {
               stringwithoutDuplicate += string + ",";
          }
          System.out.println(stringwithoutDuplicate);
          int index = stringwithoutDuplicate.toString().lastIndexOf(",");
          String value = stringwithoutDuplicate.substring(0, index);
          System.out.println(value);
     }
}
