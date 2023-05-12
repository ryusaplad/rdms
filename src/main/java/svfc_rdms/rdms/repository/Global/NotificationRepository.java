package svfc_rdms.rdms.repository.Global;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import svfc_rdms.rdms.model.Notifications;
import svfc_rdms.rdms.model.Users;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findAllByFromAndStatus(Users from, boolean status);

    Page<Notifications> findAllByStatus(boolean status, Pageable pageable);

    Page<Notifications> findAllByToAndStatus(Users to, boolean status, Pageable pageable);

    Page<Notifications> findAllByToIsNull(Pageable pageable);

    Page<Notifications> findAllByToIsNullAndFromIsNotNullOrTo(Users to, Pageable pageable);

    @Query("SELECT n FROM Notifications n WHERE (n.to IS NULL AND n.status = :status AND n.from IS NOT NULL) OR (n.to = :to AND n.status = :status AND n.from IS NOT NULL)")
    Page<Notifications> findAllByToIsNullAndStatusAndFromNotNull(@Param("status") boolean status, @Param("to") Users to, Pageable pageable);

    Page<Notifications> findAllByTo(Users to, Pageable pageable);

    Page<Notifications> findAllByFromAndToAndStatus(Users from, Users to, boolean status, Pageable pageable);

    Page<Notifications> findAllByToIsNullAndStatus(boolean status, Pageable pageable);

}
