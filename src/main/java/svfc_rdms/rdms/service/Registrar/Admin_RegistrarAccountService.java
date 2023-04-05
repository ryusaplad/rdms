package svfc_rdms.rdms.service.Registrar;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.Users;

public interface Admin_RegistrarAccountService {
    ResponseEntity<Object> saveUsersAccount(Users user, int actions, HttpSession session,HttpServletRequest request);

    boolean findUserName(String username);

    Optional<Users> findOneUserById(long userId);

    boolean deleteData(long userId, HttpSession session,HttpServletRequest request);

    boolean changeAccountStatus(String status, long userId, HttpSession session,HttpServletRequest request);

    ResponseEntity<String> exportingStudentRequestToExcel(HttpServletResponse response, HttpSession session,List<StudentRequest> studReq,HttpServletRequest request);

    void exportConfirmation(HttpServletResponse response, HttpSession session,List<StudentRequest> studReq,HttpServletRequest request);

}
