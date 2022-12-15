package svfc_rdms.rdms.serviceImpl.Student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Admin.AdminRepository;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Student.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

     @Autowired
     StudentRepository studRepo;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     AdminRepository adminRepo; 

     @Override
     public boolean saveRequest(StudentRequest request) {
          if (request != null) {
               studRepo.save(request);
               return true;
          }
          return false;
     }


     @Override
     public Boolean findDocumentByTitle(String title) {

          if (docRepo.findByTitle(title) != null) {
               return true;
          }
          return false;
     }

    
     @Override
     public List<StudentRequest> displayRequestByStudent(Users user) {

          return studRepo.findAllByRequestBy(user);
     }


     @Override
     public Users getUserIdByUsername(String username) {
          return adminRepo.findUserIdByUsername(username);
     }

}
