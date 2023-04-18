package svfc_rdms.rdms.serviceImpl.Admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.service.Admin.Admin_DatabaseService;

@Service
public class Admin_DatabaseServiceImpl implements Admin_DatabaseService {

    @Override
    public void backup(String host, String port, String dbName, String username, String password, String backupPath) {
        List<String> command = Arrays.asList("mysqldump", "-u" + username, "-p" + password,
                "--add-drop-database", "-B", dbName, "-r", backupPath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup created successfully");
            } else {
                System.out.println("Could not create the backup");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public void restore(String host, String port, String dbName, String username, String password, String backupPath) {
        List<String> command = Arrays.asList("mysql", "-u" + username, "-p" + password, "-h" + host, "-P" + port,
                dbName, "-e",
                "source " + backupPath);
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup restored successfully");
            } else {
                System.out.println("Could not restore the backup");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public byte[] backUpDatabase(String host, String port, String dbName, String username, String password) {
        try {
            String[] command = new String[] { "mysqldump", "-h" + host, "-P" + port, "-u" + username, "-p" + password,
                    dbName };
            
            Process process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
            process.waitFor();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }
}
