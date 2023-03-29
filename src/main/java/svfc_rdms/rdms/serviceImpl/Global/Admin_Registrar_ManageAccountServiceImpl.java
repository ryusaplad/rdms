package svfc_rdms.rdms.serviceImpl.Global;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.model.ValidAccounts;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.service.Registrar.Admin_RegistrarAccountService;

@Service
public class Admin_Registrar_ManageAccountServiceImpl implements Admin_RegistrarAccountService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GlobalServiceControllerImpl globalService;

    @Autowired
    private GlobalLogsServiceImpl globalLogsServiceImpl;

    @Override
    public ResponseEntity<Object> saveUsersAccount(Users user, int actions, HttpSession session) {

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
                    if (userIdFormat.contains("C-")) {

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
                    if (userIdFormat.contains("C-")) {

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
                        globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "Normal", session);
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
                    globalLogsServiceImpl.saveLog(0, logMessage, "Mid_Log", date, "medium", session);
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
    public boolean deleteData(long userId, HttpSession session) {

        try {
            if (findOneUserById(userId).isPresent()) {
                usersRepository.deleteById(userId);
                String date = LocalDateTime.now().toString();
                String logMessage = "User " + session.getAttribute("name").toString()
                        + " deleted the user (" + findOneUserById(userId).get().getName()
                        + ")";
                globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session);
                return true;
            }
            return false;
        } catch (Exception e) {
            String date = LocalDateTime.now().toString();
            String logMessage = "User " + session.getAttribute("name").toString()
                    + " trying to delete the user (" + findOneUserById(userId).get().getName()
                    + ")::system message: " + e.getMessage();
            globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session);

            throw new ApiRequestException(
                    "Users with requests cannot be permanently deleted. Please contact the administrator for further assistance.");
        }
    }

    @Override
    public boolean changeAccountStatus(String status, long userId, HttpSession session) {
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
            globalLogsServiceImpl.saveLog(0, logMessage, "High_Log", date, "high", session);

            throw new ApiRequestException(
                    "Failed to change status, Please Try Again!. Please contact the administrator for further assistance.");
        }

    }
}