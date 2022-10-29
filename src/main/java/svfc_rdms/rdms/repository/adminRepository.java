package svfc_rdms.rdms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import svfc_rdms.rdms.model.Admin;

public interface adminRepository extends JpaRepository<Admin,Integer>{
     
}
