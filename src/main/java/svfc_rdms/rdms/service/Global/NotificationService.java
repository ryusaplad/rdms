package svfc_rdms.rdms.service.Global;

import java.util.List;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;

public interface NotificationService {
    List<Notifications> getAllNotificationByUser();

    List<Notifications> getAllNotificationsByAdmin();

    boolean sendNotificationGlobally(String title, String message, String messageType, String timeAndDate,
            boolean status, Users user);

    boolean sendNotificationFromUserToUser(String title, String message, String messageType,
            String timeAndDate,
            boolean status,
            Users from, Users to);
}
