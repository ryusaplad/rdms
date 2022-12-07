package svfc_rdms.rdms.service;

import java.util.List;

import svfc_rdms.rdms.model.StudentRequest;

public interface StudentService {
     boolean saveRequest(StudentRequest request);

     List<StudentRequest> displayRequestByStatus(String status);

     List<StudentRequest> displayAllRequest();
}
