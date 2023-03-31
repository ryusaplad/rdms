package svfc_rdms.rdms.service.Global;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.GlobalLogs;

public interface GlobalLogsServices {

    public void saveLog(

            long logsId,
            String message,
            String messageType,
            String dateAndTime,
            String threatLevel,
            HttpSession session);

    public List<GlobalLogs> getAllLogs();

    public ResponseEntity<GlobalLogs> loadSpecificLogs(long logId);

    public Boolean deleteLog(long logId);
}
