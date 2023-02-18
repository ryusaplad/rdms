package svfc_rdms.rdms.serviceImpl.Global;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.NotificationRepository;
import svfc_rdms.rdms.service.Global.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notifRepository;

    @Override
    public List<Notifications> getAllNotificationByUser() {
        return null;
    }

    @Override
    public List<Notifications> getAllNotificationsByAdmin() {
        return null;
    }

    @Override
    public boolean sendNotificationGlobally(Notifications notification) {

        if (notification != null) {
            notifRepository.save(notification);
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<Object> sentNotificationFromUserToUser(Notifications notification, Users from, Users to) {
        // TODO Auto-generated method stub
        return null;
    }

}
