package svfc_rdms.rdms.serviceImpl.Global;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.GlobalLogs;
import svfc_rdms.rdms.service.Global.GlobalLogsServices;

@Service
public class GlobalLogsServiceImpl implements GlobalLogsServices {

    @Override
    public List<GlobalLogs> getAllLogs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllLogs'");
    }

    @Override
    public ResponseEntity<Object> loadSpecificLogs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadSpecificLogs'");
    }

    @Override
    public Boolean deleteLog() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteLog'");
    }

}
