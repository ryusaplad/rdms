package svfc_rdms.rdms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
     @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
