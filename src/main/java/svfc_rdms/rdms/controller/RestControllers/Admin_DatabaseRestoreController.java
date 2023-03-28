package svfc_rdms.rdms.controller.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.serviceImpl.Admin.Admin_DatabaseServiceImpl;

@RestController
@RequestMapping("/database/backup")
public class Admin_DatabaseRestoreController {

    @Autowired
    private Admin_DatabaseServiceImpl adminDataBaseService;

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "svfc_rdms_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String BACKUP_PATH = "C:\\backup\\(2023-03-28_18-26-00_Tuesday)backup_sql_rdms.sql";

    @GetMapping("/restore")
    public ResponseEntity<?> restoreDatabase() {

        try {
            adminDataBaseService.restore(HOST, PORT, DB_NAME, USERNAME, PASSWORD, BACKUP_PATH);
            return ResponseEntity.ok("Database restored successfully.");
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }
}
