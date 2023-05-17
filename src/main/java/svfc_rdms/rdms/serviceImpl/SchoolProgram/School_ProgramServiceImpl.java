package svfc_rdms.rdms.serviceImpl.SchoolProgram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.repository.SchoolProgram.School_ProgramRepository;
import svfc_rdms.rdms.service.SchoolProgram.School_ProgramService;

@Service
public class School_ProgramServiceImpl implements School_ProgramService{

    @Autowired
    private School_ProgramRepository programRepository;

    @Override
    public ResponseEntity<Object> loadAllSchoolProgramInfo() {
     return new ResponseEntity<>(programRepository.findAll(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> loadSpecificSchoolProgramInfoBySchoolLevel(String level) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadSpecificSchoolProgramInfo'");
    }

    @Override
    public ResponseEntity<String> updateSchoolProgramInfo(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSchoolProgramInfo'");
    }

    @Override
    public ResponseEntity<String> deleteSchoolProgramInfo(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSchoolProgramInfo'");
    }

    @Override
    public ResponseEntity<String> updateSchoolProgramStatus(long id, String status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSchoolProgramStatus'");
    }

    @Override
    public ResponseEntity<Object> saveSchoolProgramInfo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveSchoolProgramInfo'");
    }
    
}
