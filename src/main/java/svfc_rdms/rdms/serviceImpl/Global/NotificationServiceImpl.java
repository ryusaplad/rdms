package svfc_rdms.rdms.serviceImpl.Global;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import svfc_rdms.rdms.service.Global.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notifRepository;

    @Autowired
    private GlobalLogsServiceImpl globalLogsServiceImpl;

    @Autowired
    private GlobalServiceControllerImpl globalService;

    @Autowired
    private EmailServiceImpl myEmailService;

    @Override
    public ResponseEntity<Object> fetchAllNotificationsByUserToMainNotifModal(Users user, String userType, int lowestPage,
            int totalPage) {
        Sort descendingSort = Sort.by("notifId").descending();

        Page<Notifications> page = null;
        List<Notifications> notifications = new ArrayList<>();

        if (userType.equals("student")) {
            page = notifRepository.findAllByTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            notifications = page.getContent();
        } else if (userType.equals("registrar")) {
            // Get Student Nofication check if the to is null, (why null?)
            /*
             * findAllByToIsNullOrTo = Find All By Users/Sender is Null if null that is
             * student.
             * if the to is not null that is from or
             * between registrar and teacher notification
             */
            page = notifRepository.findAllByToIsNullAndFromIsNotNullOrTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            
            notifications = page.getContent();
        } else if (userType.equals("teacher")) {
            page = notifRepository.findAllByTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            notifications = page.getContent();
        }
        if (userType.equals("school_admin")) {
            page = notifRepository.findAllByTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            notifications = page.getContent();
        }

        List<Notification_Dto> notifsDto = new ArrayList<>();
        for (Notifications notifData : notifications) {

            String reciever = "";
            String sender = "";
            if (userType.equals("student")) {

                if (user.getUserId() == notifData.getTo().getUserId()) {

                    reciever = notifData.getTo().getName() + "(" + notifData.getTo().getUsername() + ")";
                    if (notifData.getFrom() != null) {

                        sender = notifData.getFrom().getName() + "(" + notifData.getFrom().getUsername() + ")";
                    }
                    if (notifData.getTo() != null) {

                        reciever = notifData.getTo().getName() + "(" + notifData.getTo().getUsername() + ")";
                    }
                    if (notifData.getTo() == null) {

                        reciever = "NONE";
                    }
                    if (notifData.getFrom() == null) {
                        sender = "NONE";
                    }
                    notifsDto.add(
                            new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                                    notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                                    sender, reciever, page.getTotalElements()));
                } else {

                }
            } else if (userType.equals("registrar")) {

                if (notifData.getFrom() != null) {

                    sender = notifData.getFrom().getName() + "(" + notifData.getFrom().getUsername() + ")";
                }
                if (notifData.getTo() != null) {

                    reciever = notifData.getTo().getName() + "(" + notifData.getTo().getUsername() + ")";
                }
                if (notifData.getTo() == null) {

                    reciever = "All Registrar";
                }
                if (notifData.getFrom() == null) {
                    sender = "System";
                }
                notifsDto.add(
                        new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                                notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                                sender, reciever, page.getTotalElements()));

            } else if (userType.equals("teacher")) {

                if (notifData.getFrom() != null) {

                    sender = notifData.getFrom().getName() + "(" + notifData.getFrom().getUsername() + ")";
                }
                if (notifData.getTo() != null) {

                    reciever = notifData.getTo().getName() + "(" + notifData.getTo().getUsername() + ")";
                }
                if (notifData.getTo() == null) {

                    reciever = "NONE";
                }
                if (notifData.getFrom() == null) {
                    sender = "NONE";
                }
                notifsDto.add(
                        new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                                notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                                sender, reciever, page.getTotalElements()));

            } else if (userType.equals("school_admin")) {

                if (notifData.getFrom() != null) {

                    sender = notifData.getFrom().getName() + "(" + notifData.getFrom().getUsername() + ")";
                }
                if (notifData.getTo() != null) {

                    reciever = notifData.getTo().getName() + "(" + notifData.getTo().getUsername() + ")";
                }
                if (notifData.getTo() == null) {

                    reciever = "NONE";
                }
                if (notifData.getFrom() == null) {
                    sender = "NONE";
                }
                notifsDto.add(
                        new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                                notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                                sender, reciever, page.getTotalElements()));

            } else {

            }

        }

        return new ResponseEntity<>(notifsDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> fetchTopNavBarAndSidebarNotif(Users user, String userType, int lowestPage,
            int totalPage, boolean status) {
        Sort descendingSort = Sort.by("notifId").descending();
        Page<Notifications> page = null;
        List<Notifications> notifications = new ArrayList<>();
        if (userType.equals("student")) {
            page = notifRepository.findAllByToAndStatus(user, status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            notifications = page.getContent();
        } else if (userType.equals("registrar")) {

            // Get Student Nofication check if the to is null, (why null?)
            /*
             * findAllByToIsNullAndStatusOrTo = Find All By Users/Sender is Null if null that is
             * student.
             * if the to is not null that is from or
             * between registrar and teacher notification
             */
            page = notifRepository.findAllByToIsNullAndStatusAndFromNotNull(status,user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));

            notifications = page.getContent();

        } else if (userType.equals("teacher")) {
            page = notifRepository.findAllByToAndStatus(user, status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            notifications = page.getContent();
        } else if (userType.equals("school_admin")) {
            page = notifRepository.findAllByToAndStatus(user, status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
            notifications = page.getContent();
        }

        List<Notification_Dto> notifsDto = new ArrayList<>();
        for (Notifications notifData : notifications) {
            notifsDto.add(
                    new Notification_Dto(notifData.getNotifId(), notifData.getTitle(), notifData.getMessage(),
                            notifData.getMessageType(), notifData.getDateAndTime(), notifData.getStatus(),
                            "", "", page.getTotalElements()));
        }

        return new ResponseEntity<>(notifsDto, HttpStatus.OK);
    }

    @Override
    public boolean sendStudentNotification(String title, String message, String messageType, String timeAndDate,
            boolean status,
            Users user, HttpSession session, HttpServletRequest request) {

        if (user != null) {

            Notifications notification = new Notifications();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setMessageType(messageType);
            notification.setDateAndTime(timeAndDate);
            notification.setStatus(status);
            notification.setTo(user);
            notifRepository.save(notification);
            String date = LocalDateTime.now().toString();
            String logMessage = "Notification sent by user " + session.getAttribute("name").toString()
                    + " with the message: " + message + ".";
            globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
            globalService.sendTopic("/topic/notifications/", "OK");

            if (status == true) {

                String userEmail = user.getEmail();

                myEmailService.sendEmail("noreply@rdmssvfc2023.com", userEmail, "Requests Status", message);
                return true;
            } else {
                return true;
            }

        }
        return false;

    }

    @Override
    public boolean sendRegistrarNotification(String title, String message, String messageType, String timeAndDate,
            boolean status,
            Users user, HttpSession session, HttpServletRequest request) {

        if (user != null) {
            Notifications notification = new Notifications();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setMessageType(messageType);
            notification.setDateAndTime(timeAndDate);
            notification.setStatus(status);
            notification.setFrom(user);
            notifRepository.save(notification);
            String date = LocalDateTime.now().toString();
            String logMessage = "Notification sent by user " + user.getName()
                    + " with the message: " + message + ".";
            globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
            globalService.sendTopic("/topic/notifications/", "OK");
            return true;
        }
        return false;
    }

    @Override
    public boolean sendNotification(String title, String message, String messageType, String timeAndDate,
            boolean status,
            Users from, Users to, HttpSession session, HttpServletRequest request) {

        if (from != null && to != null) {
            Notifications notification = new Notifications();
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setMessageType(messageType);
            notification.setDateAndTime(timeAndDate);
            notification.setStatus(status);
            notification.setFrom(from);
            notification.setTo(to);
            notifRepository.save(notification);
            String date = LocalDateTime.now().toString();
            String logMessage = "Notification sent by user " + from.getName() + " to " + to.getName()
                    + " with the message "
                    + message + ".";
            globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
            globalService.sendTopic("/topic/notifications/", "OK");
            return true;
        }
        return false;
    }

}
