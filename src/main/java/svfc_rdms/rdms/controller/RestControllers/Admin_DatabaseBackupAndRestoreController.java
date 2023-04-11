package svfc_rdms.rdms.controller.RestControllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.serviceImpl.Admin.Admin_DatabaseServiceImpl;

@RestController
@RequestMapping("/database/backup")
public class Admin_DatabaseBackupAndRestoreController {

    @Autowired
    private Admin_DatabaseServiceImpl adminDataBaseService;

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "svfc_rdms_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    @PostMapping("/restore")
    public ResponseEntity<?> restoreDatabase(@RequestParam("file") Optional<MultipartFile> file) {

        try {
            if (file.isPresent()) {
                String fileName = StringUtils.cleanPath(file.get().getOriginalFilename());
                if (!fileName.endsWith(".sql")) {
                    return new ResponseEntity<>("Invalid file format. Only .sql files are allowed.",
                            HttpStatus.BAD_REQUEST);
                }
                File tempFile = File.createTempFile("temp", null);
                file.get().transferTo(tempFile);
                String filePath = tempFile.getAbsolutePath();
                adminDataBaseService.restore(HOST, PORT, DB_NAME, USERNAME, PASSWORD, filePath);
                return ResponseEntity
                        .ok("Database restored successfully. Filename: " + fileName + ", Path: " + filePath);
            }
            return new ResponseEntity<>("Failed to restore database, Please try again!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> backUpDatabase() {
    
        try {
            byte[] backupBytes = adminDataBaseService.backUpDatabase(HOST, PORT, DB_NAME, USERNAME, PASSWORD);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            LocalDateTime dateNow = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_EEEE");
            String backupFileName = "(" + dateNow.format(formatter) + ")backup_database_rdms.sql";
            headers.setContentDispositionFormData("attachment", backupFileName);
            headers.setContentLength(backupBytes.length);
            InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(backupBytes));
            return new ResponseEntity<>(isr, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Failed to backup database, Please try again!", HttpStatus.BAD_REQUEST);
        }
    }
}
