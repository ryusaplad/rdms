package svfc_rdms.rdms.serviceImpl.SchoolProgram;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.SchoolPrograms;
import svfc_rdms.rdms.repository.SchoolProgram.School_ProgramRepository;
import svfc_rdms.rdms.service.SchoolProgram.School_ProgramService;

@Service
public class School_ProgramServiceImpl implements School_ProgramService {

    @Autowired
    private School_ProgramRepository programRepository;

    @Override
    public ResponseEntity<Object> loadAllSchoolProgramInfo() {
        List<SchoolPrograms> allPrograms = programRepository.findAll();
        return new ResponseEntity<>(allPrograms, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> loadSpecificSchoolProgramInfoByLevelAndSchoolLevel(String gradeLevel, String schoolLevel) {
        List<SchoolPrograms> programsBySchoolLevel = programRepository.findAllByLevelAndSchoolLevel(gradeLevel,schoolLevel);
        return new ResponseEntity<>(programsBySchoolLevel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateSchoolProgramInfo(long id) {
        Optional<SchoolPrograms> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            SchoolPrograms program = programOptional.get();
            // Update the program information here
            programRepository.save(program);
            return new ResponseEntity<>("School program information updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("School program not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteSchoolProgramInfo(long id) {
        Optional<SchoolPrograms> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            programRepository.deleteById(id);
            return new ResponseEntity<>("School program deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("School program not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateSchoolProgramStatus(long id, String status) {
        Optional<SchoolPrograms> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            SchoolPrograms program = programOptional.get();
            // Update the program status here
            program.setStatus(status);
            programRepository.save(program);
            return new ResponseEntity<>("School program status updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("School program not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> saveSchoolProgramInfo(Map<String, String> params) {
        try {
            String schoolLevel = params.get("schoolLevel");
            String level = params.get("level");
            String courseAndCategory = params.get("course");
            String status = "available";

            SchoolPrograms program = SchoolPrograms.builder()
                    .schoolLevel(schoolLevel)
                    .level(level)
                    .courseOrcategory(courseAndCategory)
                    .status(status)
                    .build();

            programRepository.save(program);
            return new ResponseEntity<>("School program information saved successfully.",
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("School program information failed to save, Please Try Again.", HttpStatus.OK);
        }
    }

}
