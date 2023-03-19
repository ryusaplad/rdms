package svfc_rdms.rdms.service.Global;

import java.util.List;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.GlobalLogs;

public interface GlobalLogsServices {

    public List<GlobalLogs> getAllLogs();

    public ResponseEntity<Object> loadSpecificLogs();

    public Boolean deleteLog();
}
