package svfc_rdms.rdms.serviceImpl.Global;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
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
import svfc_rdms.rdms.repository.Global.UsersRepository;
import svfc_rdms.rdms.service.Global.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notifRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GlobalLogsServiceImpl globalLogsServiceImpl;

    @Autowired
    private GlobalServiceControllerImpl globalService;

    @Autowired
    private EmailServiceImpl myEmailService;

    @Override
    public ResponseEntity<Object> getAllNotificationsByUser(Users user, String userType, int lowestPage,
            int totalPage) {
        Sort descendingSort = Sort.by("notifId").descending();
        Page<Notifications> page = null;

        if (userType.equals("student")) {
            page = notifRepository.findAllByTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));

        } else if (userType.equals("registrar")) {
            page = notifRepository.findAllByToIsNull(
                    PageRequest.of(lowestPage, totalPage, descendingSort));

        } else if (userType.equals("teacher")) {
            page = notifRepository.findAllByTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));

        }
        if (userType.equals("school_admin")) {
            page = notifRepository.findAllByTo(user,
                    PageRequest.of(lowestPage, totalPage, descendingSort));

        }

        List<Notifications> notifications = page.getContent();

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

                    reciever = "Add Here";
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
    public ResponseEntity<Object> fetchDasboardAndSidebarNotif(Users user, String userType, int lowestPage,
            int totalPage, boolean status) {
        Sort descendingSort = Sort.by("notifId").descending();
        Page<Notifications> page = null;

        if (userType.equals("student")) {
            page = notifRepository.findAllByToAndStatus(user, status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));

        } else if (userType.equals("registrar")) {
            page = notifRepository.findAllByToIsNullAndStatus(status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));

        } else if (userType.equals("teacher")) {
            page = notifRepository.findAllByToAndStatus(user, status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
        } else if (userType.equals("school_admin")) {
            page = notifRepository.findAllByToAndStatus(user, status,
                    PageRequest.of(lowestPage, totalPage, descendingSort));
        }

        List<Notifications> notifications = page.getContent();

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
            String colorCode = "";
            if (message.contains("ongoing")) {
                colorCode = "28a745";
            } else if (message.contains("rejected")) {
                colorCode = "a7284a";
            }
            String userEmail = user.getEmail();
            String textWithHtml = "<div style='font-family: \"GT America Regular\",\"Roboto\",\"Helvetica\",\"Arial\",sans-serif; background-color: #f2f2f2;'>"
                    + "<div style='background-color: #e9ecef; padding: 20px;'>"
                    + "<div style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 20px; margin: 0 auto; max-width: 600px;'>"
                    + "<img src='https://scontent.fmnl17-2.fna.fbcdn.net/v/t39.30808-6/302349848_419523650165763_8414489783644060698_n.png?_nc_cat=109&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=x6aYKuWS5jwAX_zQlro&_nc_pt=1&_nc_ht=scontent.fmnl17-2.fna&oh=00_AfC12ko0zXIzkIwKAWegEorqrt8ZEzgeucUENMC6E9nJYg&oe=6434E7B7' width='80' height='80' alt='Profile Picture' style='display: block; margin: 0 auto; max-width: 100%; height: auto; border-radius: 50%; text-align: center;'>"
                    + "<h1 style='margin: 10px 0 5px; font-size: 24px; font-weight: bold; color: #000000; text-align: center;'>St. Vincent de Ferrer College of Camarin, Inc.</h1>"
                    + "<hr style='margin-top:15px; border: 0; border-top: 1px solid #cccccc; text-align: center;'>"
                    + "<div style='display: inline-block; font-size: 22px; font-weight: bold; color: #"
                    + colorCode + "; border-radius: 5px;'>Approved</div>"
                    + "<hr style='border: 0; border-top: 1px solid #cccccc; text-align: center;'>"
                    + "<p style='margin-bottom: 20px; font-size: 16px; color: #000000; text-align: center;'>"
                    + message + "</p>"
                    + "<p style='font-size: 16px; color: #000000; text-align: center;'>Use your student no to access RDMS.</p>"
                    + "<a href='javascript:void(0);' class='btn' style='display: inline-block; padding: 10px 0; width: 100%; font-size: 16px; font-weight: bold; color: #ffffff; background-color: #28a745; border-radius: 5px; text-decoration: none; text-align:center;'>Go to RDMS</a>"
                    + "</div>"
                    + "</div>"
                    + "</div>";
            try {

                myEmailService.sendEmail("noreply@gmail.com", userEmail, "Test Email",
                        textWithHtml);

                return true;
            } catch (MessagingException e) {
                return false;

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
            String logMessage = "Notification sent by user " + from + " to " + to + " with the message "
                    + message + ".";
            globalLogsServiceImpl.saveLog(0, logMessage, "Normal_Log", date, "low", session, request);
            globalService.sendTopic("/topic/notifications/", "OK");
            return true;
        }
        return false;
    }

}
