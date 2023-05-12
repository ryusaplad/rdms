package svfc_rdms.rdms.repository.Global;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import svfc_rdms.rdms.model.Notifications;

@SpringBootTest
public class NotificationRepositoryTest {

@Autowired
NotificationRepository notifRepo;

@Autowired
UsersRepository userRepo;
    @Test
    void testFindAllByTo() {
       
        Page<Notifications> notif  = notifRepo.findAllByToIsNull(PageRequest.of(0, 30));
       List<Notifications> notifD =notif.getContent();
       System.out.println("Size:   "+notifD.isEmpty());
        for (Notifications notifDNotifications : notifD) {
            System.out.println("ID:   "+notifDNotifications.getNotifId());
        }
    }
}
