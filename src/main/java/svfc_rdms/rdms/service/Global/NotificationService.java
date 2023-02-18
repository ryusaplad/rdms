package svfc_rdms.rdms.service.Global;

import java.util.List;

import org.springframework.http.ResponseEntity;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;

public interface NotificationService {
    List<Notifications> getAllNotificationByUser();

    List<Notifications> getAllNotificationsByAdmin();

    boolean sendNotificationGlobally(Notifications notification);

    ResponseEntity<Object> sentNotificationFromUserToUser(Notifications notification, Users from, Users to);
}
