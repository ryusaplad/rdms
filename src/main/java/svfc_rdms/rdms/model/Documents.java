package svfc_rdms.rdms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documents {

     @Id
     @SequenceGenerator(name = "document_sequence", sequenceName = "document_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_sequence")
     private long documentId;
     private String title;
     private String description;
     @Column(name = "image", columnDefinition = "LONGBLOB")
     private byte image;
     private boolean status;

}
