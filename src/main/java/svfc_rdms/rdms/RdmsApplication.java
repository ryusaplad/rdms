package svfc_rdms.rdms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;

@SpringBootApplication
public class RdmsApplication implements CommandLineRunner {

	@Autowired
	private AdminServicesImpl adminServicesImpl;

	public static void main(String[] args) {
		SpringApplication.run(RdmsApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		adminServicesImpl.createDefault_Admin_User_IfNotExisted();

	}

}
