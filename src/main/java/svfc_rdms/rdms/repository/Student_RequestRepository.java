package svfc_rdms.rdms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.StudentRequest;

public interface Student_RequestRepository extends JpaRepository<StudentRequest, Long> {

     public List<StudentRequest> findAllByRequestStatus(String status);
}
