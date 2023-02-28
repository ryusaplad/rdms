package svfc_rdms.rdms.service.Global;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Users;

public interface NotificationService {

        ResponseEntity<Object> getAllNotificationsByUser();

        ResponseEntity<Object> fetchAllNotificationByLoggedinUser(Users user, int lowestPage, int totalPage);

        boolean sendStudentNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users user);

        boolean sendRegistrarNotification(String title, String message, String messageType, String timeAndDate,
                        boolean status,
                        Users from);

}
