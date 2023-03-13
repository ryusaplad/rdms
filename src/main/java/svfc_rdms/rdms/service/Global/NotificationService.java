package svfc_rdms.rdms.service.Global;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface NotificationService {

        ResponseEntity<Object> getAllNotificationsByUser(Users user, String userType, int lowestPage,
        int totalPage);

        ResponseEntity<Object> fetchDasboardAndSidebarNotif(Users user, String userType, int lowestPage,
                        int totalPage,boolean status);

        boolean sendStudentNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users user);

        boolean sendRegistrarNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users from);

        boolean sendNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users from,Users to);

}
