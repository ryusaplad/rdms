package svfc_rdms.rdms.serviceImpl.Global;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import svfc_rdms.rdms.dto.Notification_Dto;
import svfc_rdms.rdms.dto.ServiceResponse;
import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Global.NotificationRepository;

@SpringBootTest
public class NotificationServiceImplTest {

    @Autowired
    private GlobalServiceControllerImpl globalService;

    @Autowired
    NotificationServiceImpl notificationService;

    @Autowired
    NotificationRepository notifRepository;

    @Test
    void testSendNotificationGlobally() {

    }
}
