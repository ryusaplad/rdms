package svfc_rdms.rdms.repository.Global;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Notifications;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

}
