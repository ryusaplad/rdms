package svfc_rdms.rdms;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import svfc_rdms.rdms.model.Documents;
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

	@Test
	void contextLoads() {
		byte x[] = new byte[1];
		long xd = 16;
		Documents docInformation = new Documents(xd, "title", "desc", x, true);

		docRepo.save(docInformation);
	}

	@Test
	void testGetAllDocumentTitles() {
		List<String> titles = impl.getAllDocumentTitles();

		titles.stream().forEach(e -> {
			System.out.println(e);
		});

		System.out.println(titles.get(titles.indexOf("ID")));

	}

}
