package svfc_rdms.rdms.service.Global;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface NotificationService {

        ResponseEntity<Object> fetchAllNotificationsByUserToMainNotifModal(Users user, String userType, int lowestPage,
        int totalPage);

        ResponseEntity<Object> fetchTopNavBarAndSidebarNotif(Users user, String userType, int lowestPage,
                        int totalPage,boolean status);

        boolean sendStudentNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users user,HttpSession session,HttpServletRequest request);

        boolean sendRegistrarNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users from,HttpSession session,HttpServletRequest request);

        boolean sendNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users from,Users to,HttpSession session,HttpServletRequest request);

}
