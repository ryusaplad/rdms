package svfc_rdms.rdms.serviceImpl.Global;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.Enums.ValidAccounts;
import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.dto.StudentRequest_Dto;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.Global.Admin_RegistrarService;

@Service
public class Admin_Registrar_ManageServiceImpl implements Admin_RegistrarService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GlobalServiceControllerImpl globalService;

    @Autowired
    private GlobalLogsServiceImpl globalLogsServiceImpl;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public ResponseEntity<Object> saveUsersAccount(Users user, int actions, HttpSession session,
            HttpServletRequest request) {

        String error = "";
        try {

            user.setPassword(user.getPassword().replaceAll("\\s", ""));
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (user.getName() == null || user.getName().length() < 1 || user.getName().isEmpty()) {
                error = "Name cannot be empty! " + user.getName();
                throw new ApiRequestException(error);
            } else if (user.getEmail() == null || user.getEmail().length() < 1) {
                throw new ApiRequestException("Please fill up the email.");
            } else if (!user.getEmail().matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")) {
                throw new ApiRequestException("Invalid email format, please try again.");
            } else if (user.getUsername() == null || user.getUsername().length() < 1
                    || user.getUsername().isEmpty()) {
                error = "Username cannot be empty " + user.getUsername();
                throw new ApiRequestException(error);
            } else if (user.getPassword() == null || user.getPassword().length() < 1
                    || user.getPassword().isEmpty()) {
                error = "Password cannot be empty" + user.getPassword();
                throw new ApiRequestException(error);
            } else if (user.getUsername().length() > 255 || user.getName().length() > 255
                    || user.getPassword().length() > 255) {
                throw new ApiRequestException("Data/Information is too long, Please Try Again!");
            } else if (user.getPassword().length() < 8) {
                return new ResponseEntity<>("Password must be at least 8 characters long",
                        HttpStatus.BAD_REQUEST);
            } else if (!user.getPassword()
                    .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$")) {
                return new ResponseEntity<>(
                        "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, and 1 number",
                        HttpStatus.BAD_REQUEST);
            } else {

                String userIdFormat = user.getUsername().toUpperCase();
                String currentUserAccType = "";
                if (session.getAttribute("accountType") != null) {
                    currentUserAccType = session.getAttribute("accountType").toString().toUpperCase();
                }
                if (currentUserAccType.equals(ValidAccounts.SCHOOL_ADMIN.toString())) {
                    if (userIdFormat.contains("S-")) {

                        user.setType("Student");
                    } else if (userIdFormat.contains("R-")) {

                        user.setType("Registrar");
                    } else if (userIdFormat.contains("T-")) {

                        user.setType("Teacher");
                    } else {
                        error = "Account Type Invalid, Please try again!";

                        throw new ApiRequestException(error);
                    }
                } else if (currentUserAccType.equals(ValidAccounts.REGISTRAR.toString())) {
                    if (userIdFormat.contains("S-")) {

                        user.setType("Student");
                    } else if (userIdFormat.contains("T-")) {

                        user.setType("Teacher");
                    } else {
                        error = "Account Type Invalid, Please try again!";

                        throw new ApiRequestException(error);
                    }
                } else {
                    error = "You are performing invalid action, please try again.";

                    throw new ApiRequestException(error);
                }
                user.setStatus("Active");
                // Setting Default Color Code in hex
                String randomColorCode = globalService.generateRandomHexColor();
                user.setColorCode(randomColorCode);

                if (actions == 0) {
                    if (findUserName(userIdFormat.toLowerCase())) {
                        error = "Username is already taken, Please try again!";

                        throw new ApiRequestException(error);
                    } else {
                        String hashedPassword = passwordEncoder.encode(user.getPassword());
                        user.setPassword(hashedPassword);
                        byte[] image = new byte[] { 0 };
                        user.setProfilePicture(image);
                        usersRepository.saveAndFlush(user);
                        ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                        String date = LocalDateTime.now().toString();
                        String logMessage = "User " + session.getAttribute("name").toString()
                                + " added a user (" + user.getName() + ") with a user type of " + user.getType()
                                + ".";
                        globalService.sendTopic("/topic/totals", "OK");
                        globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
                        return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                    }
                } else if (actions == 1) {

                    String hashedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(hashedPassword);
                    usersRepository.saveAndFlush(user);
                    ServiceResponse<Users> serviceResponseDTO = new ServiceResponse<>("success", user);
                    String date = LocalDateTime.now().toString();
                    String logMessage = "User " + session.getAttribute("name").toString()
                            + " updated the user (" + user.getName() + ") with a user type of " + user.getType()
                            + ".";

                    globalLogsServiceImpl.saveLog(0, logMessage, "Mid_Log", date, "medium", session, request);
                    return new ResponseEntity<Object>(serviceResponseDTO, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            error = e.getMessage();
            if (error.contains("ConstraintViolationException")) {

                error = "Username is already taken, Please try again!";
                throw new ApiRequestException(error);
            }
        }
        throw new ApiRequestException(error);
    }

    @Override
    public boolean findUserName(String username) {
        if (usersRepository.findByUsername(username).isPresent()) {
            Optional<Users> users = usersRepository.findByUsername(username);
            if (users.isPresent()) {
                return true;
            }
        }
        return false;

    }

    @Override
    public Optional<Users> findOneUserById(long userId) {
        Optional<Users> users = usersRepository.findByuserId(userId);
        if (users.isPresent()) {
            return users;
        }
        return null;
    }

    @Override
    public boolean deleteData(long userId, HttpSession session, HttpServletRequest request) {

        try {
            if (findOneUserById(userId).isPresent()) {

                String date = LocalDateTime.now().toString();
                String logMessage = "User " + session.getAttribute("name").toString()
                        + " deleted the user (" + findOneUserById(userId).get().getName()
                        + ")";
                globalService.sendTopic("/topic/totals", "OK");
                globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session, request);
                usersRepository.deleteById(userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            String date = LocalDateTime.now().toString();
            String logMessage = "User " + session.getAttribute("name").toString()
                    + " trying to delete the user (" + findOneUserById(userId).get().getName()
                    + ")::system message: " + e.getMessage();
            globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session, request);

            throw new ApiRequestException(
                    "Users with requests cannot be permanently deleted. Please contact the administrator for further assistance.");
        }
    }

    @Override
    public boolean changeAccountStatus(String status, long userId, HttpSession session, HttpServletRequest request) {
        try {
            if (findOneUserById(userId).isPresent()) {
                usersRepository.changeStatusOfUser(status, userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            String date = LocalDateTime.now().toString();
            String logMessage = "User " + session.getAttribute("name").toString()
                    + " trying to change status of the user (" + findOneUserById(userId).get().getName()
                    + ")::system message: " + e.getMessage();
            globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session, request);

            throw new ApiRequestException(
                    "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");
        }

    }

    @Override
    public ResponseEntity<String> exportingStudentRequestToExcel(HttpServletResponse response, HttpSession session,
            List<StudentRequest> studReq, HttpServletRequest request) {
        if (session.getAttribute("name") == null) {
            return new ResponseEntity<>("You are performing Invalid Action!", HttpStatus.BAD_REQUEST);
        }
        if (!studReq.isEmpty()) {

            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("nodata", HttpStatus.OK);
        }

    }

    @Override
    public void exportConfirmation(HttpServletResponse response, HttpSession session, List<StudentRequest> studReq,
            HttpServletRequest request) {
        try {

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();

            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM - d - uuuu (h-mm-ss)");
            String formattedDateTime = now.format(formatter);

            // Create the file name using the formatted date and time
            String fileName = "StudentRequest -" + formattedDateTime + ".xlsx";

            // Create a new sheet
            Sheet sheet = workbook.createSheet("Student Requests");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Request ID");
            headerRow.createCell(1).setCellValue("Year");
            headerRow.createCell(2).setCellValue("Course");
            headerRow.createCell(3).setCellValue("Semester");
            headerRow.createCell(4).setCellValue("Message");
            headerRow.createCell(5).setCellValue("Name");
            headerRow.createCell(6).setCellValue("Username");
            headerRow.createCell(7).setCellValue("Reply");
            headerRow.createCell(8).setCellValue("Manage By");
            headerRow.createCell(9).setCellValue("Request Date");
            headerRow.createCell(10).setCellValue("Release Date");
            headerRow.createCell(11).setCellValue("Request Document");
            headerRow.createCell(12).setCellValue("Request Status");

            int rowNum = 1; // Start from row 1 to skip the header row
            for (StudentRequest studRequest : studReq) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue("Request - " + studRequest.getRequestId());
                dataRow.createCell(1).setCellValue(studRequest.getYear());
                dataRow.createCell(2).setCellValue(studRequest.getCourse());
                dataRow.createCell(3).setCellValue(studRequest.getSemester());
                dataRow.createCell(4).setCellValue(studRequest.getMessage());
                dataRow.createCell(5).setCellValue(studRequest.getRequestBy().getName());
                dataRow.createCell(6).setCellValue(studRequest.getRequestBy().getUsername());
                dataRow.createCell(7).setCellValue(studRequest.getReply());
                dataRow.createCell(8).setCellValue(studRequest.getManageBy());
                dataRow.createCell(9).setCellValue(studRequest.getRequestDate().toString());
                dataRow.createCell(10).setCellValue(studRequest.getReleaseDate().toString());
                dataRow.createCell(11).setCellValue(studRequest.getRequestDocument().getTitle());
                CellStyle style = workbook.createCellStyle();
                if (studRequest.getRequestStatus().equalsIgnoreCase("on-going")) {

                    style.setFillForegroundColor(
                            new XSSFColor(new byte[] { (byte) 0xE5, (byte) 0xF6, (byte) 0xDF, (byte) 0xCC },
                                    new DefaultIndexedColorMap()));
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                } else if (studRequest.getRequestStatus().equalsIgnoreCase("approved")) {
                    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                } else if (studRequest.getRequestStatus().equalsIgnoreCase("rejected")) {
                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                } else if (studRequest.getRequestStatus().equalsIgnoreCase("pending")) {
                    style.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 126, (byte) 200, (byte) 227 },
                            new DefaultIndexedColorMap()));
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }
                Cell statusCell = dataRow.createCell(12);
                statusCell.setCellStyle(style);
                statusCell.setCellValue(studRequest.getRequestStatus());
            }
            // Add more data rows as needed

            // Auto-size the columns for better visibility
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Set the response headers
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);

            // Write the workbook to the response output stream
            try {
                OutputStream outputStream = response.getOutputStream();

                workbook.write(outputStream);
                workbook.close();
                outputStream.close();

                String date = LocalDateTime.now().toString();

                String dateAndTime = globalService.formattedDate();

                List<Users> adminUsers = usersRepository.findAll();
                String type = "";
                if (session.getAttribute("accountType") != null) {
                    if (!session.getAttribute("accountType").toString().toLowerCase().equals("school_admin")) {
                        for (Users user : adminUsers) {
                            if (user.getType().toLowerCase().equals("school_admin")) {
                                if (notificationService.sendStudentNotification("Exporting Data",
                                        "A user " + session.getAttribute("name")
                                                + " is exporting student request data.",
                                        "exporting_student_data", dateAndTime,
                                        false,
                                        user, session, request)) {

                                }
                            }
                        }
                        type = "Registrar";
                    } else {
                        type = "School admin";
                    }
                    String logMessage = type + "  Exporting data User: "
                            + session.getAttribute("name")
                            + " Exported the data to spreadsheet";
                    globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session, request);
                }

            } catch (IOException e) {
                String date = LocalDateTime.now().toString();
                String logMessage = "Registrar (" + session.getAttribute("name") + ":"
                        + session.getAttribute("username")
                        + ")failed to export student requests \nSystem Message: " + e.getMessage();
                globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);

                throw new ApiRequestException(e.getMessage());
            }
        } catch (Exception e) {
            String date = LocalDateTime.now().toString();
            String logMessage = "Registrar (" + session.getAttribute("name") + ":" + session.getAttribute("username")
                    + ")failed to export student requests \nSystem Message: " + e.getMessage();
            globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);

            throw new ApiRequestException(e.getMessage());
        } finally {
            try {

                response.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResponseEntity<Object> fetchAllStudentRequest() {
        try {

            List<StudentRequest> fetchStudentRequest = studentRepository.findAll();

            if (fetchStudentRequest.size() > -1) {
                List<StudentRequest_Dto> studentRequests = new ArrayList<>();

                fetchStudentRequest.stream().forEach(req -> {

                    studentRequests
                            .add(new StudentRequest_Dto(req.getRequestId(),
                                    req.getRequestBy().getUserId(),
                                    req.getRequestBy().getType(), req.getYear(),
                                    req.getCourse(), req.getSemester(),
                                    req.getRequestDocument().getTitle(),
                                    req.getMessage(), req.getReply(), req.getRequestBy().getName(),
                                    req.getRequestDate(),
                                    req.getRequestStatus(), req.getReleaseDate(),
                                    req.getManageBy()));

                });
                return new ResponseEntity<>(studentRequests, HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid Action", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<String> importUserAccounts(List<List<String>> tableData, String userType, HttpSession session,
            HttpServletRequest request) {
        long insertedCount = 0;
        long failedCount = 0;

        try {

            List<String> failedRecords = new ArrayList<>();
            for (List<String> row : tableData) {
                if (!row.isEmpty()) {
                    // Parse input values
                    String name = row.get(0);
                    String email = row.get(1);
                    String username = row.get(2);
                    String password = row.get(3);
                    String type = userType;
                    String status = "Active";

                    // Modify username based on user type
                    if (type.equalsIgnoreCase("Student")) {
                        username = "S-" + username.substring(2);
                    } else if (type.equalsIgnoreCase("Registrar")) {
                        username = "R-" + username.substring(2);
                    } else if (type.equalsIgnoreCase("Teacher")) {
                        username = "T-" + username.substring(2);
                    }

                    // Validate email
                    if (!globalService.isValidEmail(email)) {
                        failedCount++;
                        failedRecords.add(String.join(",", "(" + email) + " - Invalid email address");
                        continue;
                    }

                    // Validate password length
                    if (password.length() < 8 || password.length() > 26) {
                        failedCount++;
                        failedRecords.add(String.join(",", "( A User: " + username)
                                + " - Password must be between 8 and 26 characters long");
                        continue;
                    }

                    // Check if user already exists
                    Optional<Users> userData = usersRepository.findByUsernameAndEmail(username, email);

                    if (userData.isPresent()) {
                        failedCount++;
                        failedRecords.add(String.join(",", "(" + email + ", " + username)
                                + " - Username or Email already exists");
                        continue;
                    }

                    // Save user
                    String colorCode = globalService.generateRandomHexColor();
                    Users user = Users.builder()
                            .name(name)
                            .email(email)
                            .username(username)
                            .password(new BCryptPasswordEncoder().encode(password))
                            .type(type)
                            .status(status)
                            .colorCode(colorCode)
                            .build();

                    try {
                        usersRepository.save(user);
                        insertedCount++;
                    } catch (DataIntegrityViolationException e) {
                        failedCount++;
                        failedRecords.add(String.join(",", "(" + email + ", " + username)
                                + " - Username or Email already exists");
                    }
                }
            }

            String message = "Importing process successful, " + insertedCount + " records inserted";
            if (failedCount > 0) {
                message += ", " + failedCount + " records failed to insert";
            }
            if (!failedRecords.isEmpty()) {
                message += "\nFailed records:\n" + String.join("\n", failedRecords);

            }
            // Log importing acc
            String currentDate = LocalDateTime.now().toString();
            String accountType = session.getAttribute("accountType").toString();
            String userName = session.getAttribute("name").toString();
            String logMessage = "User account import completed successfully by " + accountType + ": " + userName + ". Records Inserted";
            String logLevel = "High_Log";
            if (failedCount > 0) {
                logMessage += " However, " + failedCount + " records failed to insert.";
                logLevel = "Error_Log";
            }
            if (!failedRecords.isEmpty()) {
                logMessage += " Failed records:\n" + String.join("\n", failedRecords);
            }
            globalLogsServiceImpl.saveLog(0, logMessage, logLevel, currentDate, "high", session, request);

            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {

            throw new ApiRequestException("Failed to Import (Reason): " + e.getMessage());
        }
    }

}