package svfc_rdms.rdms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;

@SpringBootApplication
@ComponentScan(basePackages = { "svfc_rdms.rdms", "svfc_rdms.rdms.serviceImpl" })
public class RdmsApplication implements CommandLineRunner {

	@Autowired
	private AdminServicesImpl adminServicesImpl;

	public static void main(String[] args) {
		SpringApplication.run(RdmsApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		adminServicesImpl.ensureDefaultAdminUserExists();
		adminServicesImpl.ensureDefaultAccountUsersExist();
		adminServicesImpl.ensureDefaultDocumentsExist();
	}

}
