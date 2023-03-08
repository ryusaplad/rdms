package svfc_rdms.rdms.repository.Global;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findAllByFromAndStatus(Users from, boolean status);

    Page<Notifications> findAllByStatus(boolean status, Pageable pageable);

    Page<Notifications> findAllByToAndStatus(Users to, boolean status, Pageable pageable);

    Page<Notifications> findAllByFromAndToAndStatus(Users from, Users to, boolean status, Pageable pageable);

    Page<Notifications> findAllByToIsNullAndStatus(boolean status, Pageable pageable);

}
