package svfc_rdms.rdms.repository.Global;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.GlobalLogs;

public interface GlobalLogsReposistory extends JpaRepository<GlobalLogs, Long> {

    public List<GlobalLogs> findAllByperformedBy();
}
