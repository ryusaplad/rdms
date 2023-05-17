package svfc_rdms.rdms.service.SchoolProgram;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.SchoolPrograms;

public interface School_ProgramService {
    
    ResponseEntity<Object> loadAllSchoolProgramInfo();
    ResponseEntity<Object> saveSchoolProgramInfo();
    ResponseEntity<Object> loadSpecificSchoolProgramInfoBySchoolLevel(String level);
    ResponseEntity<String>updateSchoolProgramInfo(long id);
    ResponseEntity<String>updateSchoolProgramStatus(long id, String status);
    ResponseEntity<String>deleteSchoolProgramInfo(long id);
}
