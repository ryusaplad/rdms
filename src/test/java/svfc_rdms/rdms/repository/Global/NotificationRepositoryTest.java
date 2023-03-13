package svfc_rdms.rdms.repository.Global;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.boot.test.context.SpringBootTest;

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
