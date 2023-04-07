package svfc_rdms.rdms.service.Admin;

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

         byte[] backUpDatabase(String host, String port, String dbName, String username, String password);
}