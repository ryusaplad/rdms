package svfc_rdms.rdms.repository.Document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import svfc_rdms.rdms.model.Documents;

public interface DocumentRepository extends JpaRepository<Documents, Long> {

     public Optional<Documents> findByTitle(String title);

     @Query(value = "SELECT d.title FROM Documents d")
     public List<String> findAllTitle();
}
