package svfc_rdms.rdms.serviceImpl.Global;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.dto.Notification_Dto;
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
    public ResponseEntity<Object> getAllNotificationsByUser() {
        List<Notifications> notifications = notifRepository.findAll();
        List<Notification_Dto> notifsDto = new ArrayList<>();

        for (Notifications notifData : notifications) {

            notifsDto.add(new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                    notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                    notifData.getFrom().getName(), notifData.getTo().getName(), notifications.size()));
        }
        return new ResponseEntity<>(notifsDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> fetchAllNotificationByLoggedinUser(Users user, int lowestPage, int totalPage) {
        Sort descendingSort = Sort.by("notifId").descending();
        Page<Notifications> page = notifRepository.findAllByToAndStatus(user, false,
                PageRequest.of(lowestPage, totalPage, descendingSort));

        List<Notifications> notifications = page.getContent();
        List<Notification_Dto> notifsDto = new ArrayList<>();

        for (Notifications notifData : notifications) {
            if (notifData.getTo() != null) {

                String reciever = "";
                String sender = "";
                if (user.getUserId() == notifData.getTo().getUserId()) {

                    reciever = notifData.getTo().getName() + "(" + notifData.getTo().getUsername() + ")";
                    if (notifData.getFrom() != null) {

                        sender = notifData.getFrom().getName() + "(" + notifData.getFrom().getUsername() + ")";
                    } else if (notifData.getTo() == null) {

                        reciever = "NONE";
                    } else if (notifData.getFrom() == null) {
                        sender = "NONE";
                    }
                    notifsDto.add(
                            new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                                    notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                                    sender, reciever, page.getTotalElements()));
                } else {

                }
            } else {

            }

        }

        return new ResponseEntity<>(notifsDto, HttpStatus.OK);
    }

    @Override
    public boolean sendStudentNotification(String title, String message, String messageType, String timeAndDate,
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
    public boolean sendRegistrarNotification(String title, String message, String messageType, String timeAndDate,
            boolean status,
            Users user) {

        if (user != null) {
            Notifications notification = new Notifications();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setMessageType(messageType);
            notification.setDateAndTime(timeAndDate);
            notification.setStatus(status);
            notification.setFrom(user);
            notifRepository.save(notification);
            return true;
        }
        return false;
    }

}
