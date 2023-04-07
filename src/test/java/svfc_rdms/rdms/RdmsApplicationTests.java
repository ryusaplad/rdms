package svfc_rdms.rdms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;

@SpringBootTest
class RdmsApplicationTests {

	@Autowired
	AdminServicesImpl impl;

	@Autowired
	StudentRepository studRepo;

	@Autowired
	DocumentRepository docRepo;




}
