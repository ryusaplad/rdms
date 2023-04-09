package svfc_rdms.rdms.serviceImpl.Global;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
