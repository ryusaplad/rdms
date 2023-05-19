package svfc_rdms.rdms.service.SchoolProgram;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface School_ProgramService {
    
    ResponseEntity<Object> loadAllSchoolProgramInfo();
    ResponseEntity<String> saveSchoolProgramInfo(Map<String,String> params);
    ResponseEntity<Object> loadSpecificSchoolProgramInfoByLevelAndSchoolLevel(String gradeLevel, String schoolLevel);
    ResponseEntity<String>updateSchoolProgramInfo(long id);
    ResponseEntity<String>updateSchoolProgramStatus(long id, String status);
    ResponseEntity<String>deleteSchoolProgramInfo(long id);
}
