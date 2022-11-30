package svfc_rdms.rdms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registrar_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarRequest {

     @Id
     @SequenceGenerator(name = "registrar_request_sequence", sequenceName = "registrar_request_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registrar_request_sequence")
     private long requestId;
     private String requestMessage; // can add message
     @Column(name = "file", columnDefinition = "LONGBLOB")
     private byte file;
     @ManyToOne
     @JoinColumn(name = "request_by", referencedColumnName = "userId")
     private Users requestBy; // One User to many Request

     @ManyToOne
     @JoinColumn(name = "request_to", referencedColumnName = "userId")
     private Users requestTo;

     private String requestDate; // what date request has been sent
     private String dateOfUpdate; // what date the request has been sent back
     private String requestStatus; // Pending / Recieve /

}
