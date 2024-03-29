package svfc_rdms.rdms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

     @Id
     @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")

     private long userId;

     private String name;

     @Column(unique = true)
     private String username;

     @Column(unique = true)
     private String email;

     private String password;

     private String type;

     private String status;

     @Column(name = "profile_image", columnDefinition = "LONGBLOB", nullable = true)
     private byte[] profilePicture;

     private String colorCode;

     @OneToMany(mappedBy = "requestBy")
     private List<RegistrarRequest> requests;

     @OneToMany(mappedBy = "requestBy")
     private List<StudentRequest> studentRequest;

     @OneToMany(mappedBy = "from")
     private List<Notifications> from;

     @OneToMany(mappedBy = "to")
     private List<Notifications> to;

     @OneToMany(mappedBy = "requestTo")
     private List<RegistrarRequest> requestTo;

     public Users(long userId, String name, String username, String password, String type, String status,
               String colorCode) {
          this.userId = userId;
          this.name = name;
          this.username = username;
          this.password = password;
          this.type = type;
          this.status = status;
          this.colorCode = colorCode;
     }

     public Users(long userId, String name, String email, String username, String password, String type, String status,
               String action) {
          this.userId = userId;
          this.email = email;
          this.name = name;
          this.username = username;
          this.password = password;
          this.type = type;
          this.status = status;

     }

}
