package svfc_rdms.rdms;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import svfc_rdms.rdms.serviceImpl.Admin.Admin_DatabaseServiceImpl;

@Component
public class DatabaseBackupScheduler {

    @Autowired
    private Admin_DatabaseServiceImpl adminDataBaseService;

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "svfc_rdms_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String BACKUP_FOLDER = "C:\\rdms_db_backup_sql";

    @Scheduled(cron = "0 0 0 * * ?") // 0 0 0 * *= everynight backup 0 * * * * * = every minute
    public void backupDatabase() {
        try {
            LocalDateTime dateNow = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_EEEE");

            File folder = new File(BACKUP_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String backUpPath = BACKUP_FOLDER + "\\(" + dateNow.format(formatter)
                    + ")backup_sql_rdms.sql";
            adminDataBaseService.backup(HOST, PORT, DB_NAME, USERNAME, PASSWORD, backUpPath);
        } catch (Exception e) {
            System.out.println("DB " + e.getMessage());
        }
    }
}
