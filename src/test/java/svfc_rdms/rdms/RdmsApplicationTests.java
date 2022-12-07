package svfc_rdms.rdms;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.repository.Student_RequestRepository;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;

@SpringBootTest
class RdmsApplicationTests {

	@Autowired
	MainServiceImpl impl;

	@Autowired
	Student_RequestRepository studRepo;
	@Test
	void contextLoads() {
		List<StudentRequest> studRequest = studRepo.findAllByRequestStatus("Pending");

		for (StudentRequest studentRequest : studRequest) {
			System.out.println(studentRequest.getRequestDocument().getImage());
		}
	}

}
