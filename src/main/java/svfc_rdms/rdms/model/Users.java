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
     // Primary Id
     private long userId;
     /* 100 Characters */
     private String name;
     /* Must be 5 characters and cannot be duplicate */
     // admin / student - C/c / facilitator-F/f / registrar - R/r / teacher - T/t
     @Column(unique = true)
     private String username;
     /*
      * must be 8 character password with
      * any special character and lower case and uppercase
      */
     private String password;
     // admin / student/ facilitator/ registrar/ teacher
     private String type;
     // temporarily remove / permanently deleted / active
     private String status;
     @Column(name = "profile_image", columnDefinition = "LONGBLOB", nullable = true)
     private byte profilePicture;

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
}
