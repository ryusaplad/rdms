package svfc_rdms.rdms.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
     @GeneratedValue(generator = "uuid2")
     @GenericGenerator(name = "uuid2", strategy = "uuid2")
     @Column(columnDefinition = "BINARY(16)")
     private UUID fileId;

     @Column(name = "file", columnDefinition = "LONGBLOB")
     private byte[] data;
     private String name;
     private String size;
     private String status; // saved/ pending/
     private String dateUploaded;
     private String filePurpose; // Backup / Requirements

     @ManyToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "uploaded_by", referencedColumnName = "userId")
     private Users uploadedBy; // One User to many files

     @ManyToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "requestwith", referencedColumnName = "requestId")
     private StudentRequest requestWith;

     @ManyToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "reg_requestwith", referencedColumnName = "requestId")
     private RegistrarRequest regRequestsWith;

}
