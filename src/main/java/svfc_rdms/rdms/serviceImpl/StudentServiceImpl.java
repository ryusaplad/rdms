package svfc_rdms.rdms.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.repository.Student_RequestRepository;
import svfc_rdms.rdms.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

     @Autowired
     Student_RequestRepository studRepo;

     @Override
     public boolean saveRequest(StudentRequest request) {
          if (request != null) {
               studRepo.save(request);
               return true;
          }
          return false;
     }

     @Override
     public List<StudentRequest> displayRequestByStatus(String status) {

          return studRepo.findAllByRequestStatus(status);
     }

     @Override
     public List<StudentRequest> displayAllRequest() {

          return studRepo.findAll();
     }

}
