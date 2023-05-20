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

	// @Autowired
	// private School_ProgramRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(RdmsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		adminServicesImpl.ensureDefaultAdminUserExists();
		adminServicesImpl.ensureDefaultAccountUsersExist();
		adminServicesImpl.ensureDefaultDocumentsExist();

		// Trigger the event to save the course information
		// saveCourseInformation();
	}

	// public void saveCourseInformation() {
	// String[] courseOptions = {
	// "BEC:Bachelor of Early Childhood",
	// "BEEd:Bachelor of Elementary Education (Gen. Ed.)",
	// "BPEd:Bachelor of Physical Education",
	// "BPA:Bachelor of Public Administration",
	// "BSA:Bachelor of Science in Accountancy",
	// "BSHM:Bachelor of Science in Hospitality Management",
	// "BSIT:Bachelor of Science in Information Technology",
	// "BSEdEng:Bachelor of Secondary Education (English)",
	// "BSEdFil:Bachelor of Secondary Education (Filipino)",
	// "BSEdMath:Bachelor of Secondary Education (Math)",
	// "BSEdSci:Bachelor of Secondary Education (Science)",
	// "BSEdSocSci:Bachelor of Secondary Education (Social Studies)",
	// "BSEdVal:Bachelor of Secondary Education (Values)",
	// "BTVTEd:Bachelor of Technical Vocational Teacher Education"
	// };

	// List<Course> courseList = new ArrayList<>();

	// for (String option : courseOptions) {
	// String[] parts = option.split(":");
	// String code = parts[0];
	// String name = parts[1];

	// Course course = Course.builder()
	// .level("college")
	// .code(code)
	// .name(name)
	// .status("available")
	// .build();

	// courseList.add(course);
	// }

	// // Insert the course list into the database
	// try {
	// courseRepository.saveAll(courseList);
	// System.out.println("Course information saved successfully.");
	// } catch (Exception e) {
	// System.out.println("Failed to save course information. Please try again.");
	// }
	// }

	// public void saveCourseInformation() {
	// 	String[] courseOptions = {
	// 			"ABM:Accountancy and Business Management (ABM)",
	// 			"HUMSS:Humanities and Social Sciences (HUMSS)",
	// 			"STEM:Science, Technology, Engineering & Mathematics (STEM)",
	// 			"gasncii:General Academic Strand",
	// 			"sports:Sports",
	// 			"bhcncii:Beauty/Hair Care (NCII)",
	// 			"bppncii:Bread Pastry Production (NCII)",
	// 			"cookeryncii:Cookery (NCII)",
	// 			"dressmakingncii:Dressmaking (NCII)",
	// 			"fbsncii:Food and Beverage Services (NCII)",
	// 			"fosncii:Front Office Services (NCII)",
	// 			"hairncii:Hairdressing (NCII)",
	// 			"housencii:Housekeeping (NCII)",
	// 			"lgsncii:Local Guiding Services (NCII)",
	// 			"tailoringncii:Tailoring (NCII)",
	// 			"tourismncii:Tourism Promotion Services (NCII)",
	// 			"wellnessncii:Wellness Massage (NCII)",
	// 			"animationncii:Animation (NCII)",
	// 			"computerhardwarencii:Computer Hardware Servicing (NCII)",
	// 			"comprogncii:Computer Programming (NCII)",
	// 			"autoncii:Automotive Servicing (NCII)",
	// 			"cesncii:Consumer Electronics Servicing (NCII)",
	// 			"eincii:Electrical Installation Maintenance (NCII)",
	// 			"isncii:International Subject (IS)"
	// 	};

	// 	List<Course> courseList = new ArrayList<>();

	// 	for (String option : courseOptions) {
	// 		String[] parts = option.split(":");
	// 		String code = parts[0];
	// 		String name = parts[1];

	// 		Course course = Course.builder()
	// 				.level("SHS")
	// 				.code(code)
	// 				.name(name)
	// 				.status("available")
	// 				.build();

	// 		courseList.add(course);
	// 	}

	// 	// Insert the course list into the database
	// 	try {
	// 		courseRepository.saveAll(courseList);
	// 		System.out.println("Course information saved successfully.");
	// 	} catch (Exception e) {
	// 		System.out.println("Failed to save course information. Please try again.");
	// 	}
	// }
}
