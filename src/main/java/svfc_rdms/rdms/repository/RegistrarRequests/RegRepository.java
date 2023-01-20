package svfc_rdms.rdms.repository.RegistrarRequests;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.RegistrarRequest;
import svfc_rdms.rdms.model.Users;

public interface RegRepository extends JpaRepository<RegistrarRequest, Long> {
     Optional<RegistrarRequest> findOneByRequestId(long requestId);

     Optional<RegistrarRequest> findOneByRequestBy(Users user);

     Optional<RegistrarRequest> findOneByRequestTo(Users user);
}
