package svfc_rdms.rdms.service.Admin;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public interface Admin_DatabaseService {

        void backup(String host,
                        String port, String dbName,
                        String username, String password,
                        String backupPath);

        void restore(String host,
                        String port, String dbName,
                        String username, String password,
                        String backupPath);

        File backUpDatabase(String host,
                        String port, String dbName,
                        String username, String password);
}