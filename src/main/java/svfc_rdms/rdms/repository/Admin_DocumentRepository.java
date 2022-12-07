package svfc_rdms.rdms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Documents;

public interface Admin_DocumentRepository extends JpaRepository<Documents, Long> {
     public Documents findByTitle(String title);

}
