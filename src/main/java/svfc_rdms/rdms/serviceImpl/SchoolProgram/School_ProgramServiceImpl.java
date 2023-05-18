package svfc_rdms.rdms.serviceImpl.SchoolProgram;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.Course;
import svfc_rdms.rdms.repository.SchoolProgram.School_ProgramRepository;
import svfc_rdms.rdms.service.SchoolProgram.School_ProgramService;
import svfc_rdms.rdms.serviceImpl.Global.GlobalLogsServiceImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;

@Service
public class School_ProgramServiceImpl implements School_ProgramService {

    @Autowired
    private School_ProgramRepository programRepository;

    @Autowired
    private GlobalLogsServiceImpl globalLogsService;

    @Autowired
    private GlobalServiceControllerImpl globalService;

    @Override
    public ResponseEntity<Object> loadAllSchoolProgramInfo() {
        List<Course> allPrograms = programRepository.findAll();
        return new ResponseEntity<>(allPrograms, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> loadAllSchoolProgramInfoByLevelAndStatus(String level, String status) {
        List<Course> programs = programRepository.findAllByLevelAndStatus(level, status);
        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> loadSpecificSchoolProgramInfoById(long id) {
        Optional<Course> programsBySchoolLevel = programRepository.findById(id);
        return new ResponseEntity<>(programsBySchoolLevel.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateSchoolProgramInfo(long id, Map<String, String> params, HttpSession session,
            HttpServletRequest request) {
        String level = params.get("level");
        String code = params.get("code");
        String name = params.get("course");
        Optional<Course> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            Course program = programOptional.get();
            // Update the program information here
            if (!level.isEmpty() && !code.isEmpty() && !name.isEmpty()) {
                program.setLevel(level);
                program.setCode(code);
                program.setName(name);
            }
            programRepository.save(program);
            String date = LocalDateTime.now().toString();
            String logMessage = "School program information with CODE and NAME " + code + ":" + name
                    + " has been updated by the user " + session.getAttribute("name").toString()
                    + " (username: " + session.getAttribute("username").toString() + ").";
            globalLogsService.saveLog(0, logMessage, "Mid_Log", date, "medium", session, request);
            globalService.sendTopic("/topic/request/cards", "OK");
            return new ResponseEntity<>("School program information updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("School program not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteSchoolProgramInfo(long id, HttpSession session, HttpServletRequest request) {
        Optional<Course> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            programRepository.deleteById(id);
            String date = LocalDateTime.now().toString();
            String logMessage = "School program information with CODE and NAME " + programOptional.get().getCode() + ":"
                    + programOptional.get().getName()
                    + " has been deleted by the user " + session.getAttribute("name").toString()
                    + " (username: " + session.getAttribute("username").toString() + ").";
            globalLogsService.saveLog(0, logMessage, "High_Log", date, "high", session, request);
            globalService.sendTopic("/topic/request/cards", "OK");
            return new ResponseEntity<>("School program deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("School program not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateSchoolProgramStatus(long id, String status, HttpSession session,
            HttpServletRequest request) {
        Optional<Course> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            Course program = programOptional.get();
            // Update the program status here
            program.setStatus(status);
            programRepository.save(program);
            String date = LocalDateTime.now().toString();
            String logMessage = "School program information with CODE and NAME " + programOptional.get().getCode() + ":"
                    + programOptional.get().getName()
                    + " has been updated by the user " + session.getAttribute("name").toString()
                    + " (username: " + session.getAttribute("username").toString() + ").";
            globalLogsService.saveLog(0, logMessage, "Mid_Log", date, "medium", session, request);
            globalService.sendTopic("/topic/request/cards", "OK");
            return new ResponseEntity<>("School program status updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("School program not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> saveSchoolProgramInfo(Map<String, String> params, HttpSession session,
            HttpServletRequest request) {
        try {
            String level = params.get("level");
            String code = params.get("code");
            String name = params.get("course");
            String status = "available";

            Course program = Course.builder()
                    .level(level)
                    .code(code)
                    .name(name)
                    .status(status)
                    .build();

            programRepository.save(program);
            String date = LocalDateTime.now().toString();
            String logMessage = "School program information with CODE and NAME " + code + ":" + name
                    + " has been added by the user " + session.getAttribute("name").toString()
                    + " (username: " + session.getAttribute("username").toString() + ").";
            globalLogsService.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
            globalService.sendTopic("/topic/request/cards", "OK");
            return new ResponseEntity<>("School program information saved successfully.",
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("School program information failed to save, Please Try Again.", HttpStatus.OK);
        }
    }

}
