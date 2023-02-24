package svfc_rdms.rdms.serviceImpl.Global;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.NotificationRepository;
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.service.Global.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notifRepository;

    @Autowired
    private UsersRepository userRepository;

    @Override
    public List<Notifications> getAllNotificationByUser() {
        return null;
    }

    @Override
    public List<Notifications> getAllNotificationsByAdmin() {
        return null;
    }

    @Override
    public boolean sendNotificationGlobally(String title, String message, String messageType, String timeAndDate,
            boolean status,
            Users user) {

        if (user != null) {
            Notifications notification = new Notifications();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setMessageType(messageType);
            notification.setDateAndTime(timeAndDate);
            notification.setStatus(status);
            notification.setTo(user);
            notifRepository.save(notification);
            return true;
        }
        return false;
    }

    @Override
    public boolean sendNotificationFromUserToUser(String title, String message, String messageType, String timeAndDate,
            boolean status, Users sentBy, Users sendTo) {
        if (sentBy != null && sendTo != null) {
            Notifications notification = new Notifications();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setMessageType(messageType);
            notification.setDateAndTime(timeAndDate);
            notification.setStatus(status);
            notification.setFrom(sentBy);
            notification.setTo(sendTo);
            notifRepository.save(notification);
            return true;
        }
        return false;
    }

}
