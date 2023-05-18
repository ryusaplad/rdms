package svfc_rdms.rdms.service.SchoolProgram;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

public interface School_ProgramService {

    ResponseEntity<Object> loadAllSchoolProgramInfo();

    ResponseEntity<Object> loadAllSchoolProgramInfoByLevelAndStatus(String level, String status);

    ResponseEntity<String> saveSchoolProgramInfo(Map<String, String> params, HttpSession session,
            HttpServletRequest request);

    ResponseEntity<Object> loadSpecificSchoolProgramInfoById(long id);

    ResponseEntity<String> updateSchoolProgramInfo(long id, Map<String, String> params, HttpSession session,
            HttpServletRequest request);

    ResponseEntity<String> updateSchoolProgramStatus(long id, String status, HttpSession session,
            HttpServletRequest request);

    ResponseEntity<String> deleteSchoolProgramInfo(long id, HttpSession session, HttpServletRequest request);
}
