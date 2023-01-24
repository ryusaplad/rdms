package svfc_rdms.rdms.repository.RegistrarRequests;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.RegistrarRequest;

public interface RegRepository extends JpaRepository<RegistrarRequest, Long> {
     Optional<RegistrarRequest> findOneByRequestId(long requestId);

     List<RegistrarRequest> findAllByRequestStatus(String requestsStatus);

}
