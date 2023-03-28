package svfc_rdms.rdms.serviceImpl.Global;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.GlobalLogs;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.GlobalLogsReposistory;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.service.Global.GlobalLogsServices;

@Service
public class GlobalLogsServiceImpl implements GlobalLogsServices {

    @Autowired
    private GlobalLogsReposistory globalLogsReposistory;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void saveLog(
            long logsId,
            String message,
            String messageType,
            String dateAndTime,
            String threatLevel,
            HttpSession session) {

        String usernameAndName = "";
        if (!(usernameAndName = session.getAttribute("username").toString()).isEmpty()) {
            GlobalLogs logs = new GlobalLogs();
            logs.setMessage(message);
            logs.setMessageType(messageType);
            logs.setDateAndTime(dateAndTime);
            logs.setThreatLevel(threatLevel);
            Optional<Users> user = usersRepository.findByUsername(usernameAndName);
            if (user.isPresent()) {
                usernameAndName = user.get().getUsername() + "-" + user.get().getName();
            }
            logs.setPerformedBy(usernameAndName);
            globalLogsReposistory.save(logs);
        }

    }

    @Override
    public List<GlobalLogs> getAllLogs() {
        return globalLogsReposistory.findAll();
    }

    @Override
    public ResponseEntity<Object> loadSpecificLogs(long logId) {
        Optional<GlobalLogs> globalLogs = globalLogsReposistory.findById(logId);

        if (globalLogs.isPresent()) {
            return new ResponseEntity<>(globalLogs, HttpStatus.OK);
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Boolean deleteLog(long logId) {
        Optional<GlobalLogs> globalLogs = globalLogsReposistory.findById(logId);

        if (globalLogs.isPresent()) {
            globalLogsReposistory.delete(globalLogs.get());
            return true;
        }
        return false;
    }

}
