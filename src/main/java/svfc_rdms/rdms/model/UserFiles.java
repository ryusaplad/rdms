package svfc_rdms.rdms.model;

import javax.persistence.CascadeType;
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
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFiles {

     @Id
     @SequenceGenerator(name = "file_sequence", sequenceName = "file_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_sequence")

     private long fileId;

     @Column(name = "file", columnDefinition = "LONGBLOB")

     private byte[] data;
     private String name;
     private String size;
     private String status; // saved/ pending/
     private String dateUploaded;

     @ManyToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "uploaded_by", referencedColumnName = "userId")
     private Users uploadedBy; // One User to many files

}
