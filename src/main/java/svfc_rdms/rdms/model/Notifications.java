package svfc_rdms.rdms.model;

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
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notifications {

     @Id
     @SequenceGenerator(name = "notif_sequence", sequenceName = "notif_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notif_sequence")
     private long notifId;
     private String title;
     private String message; // add messages
     private String messageType; // warning / information
     private String dateAndTime; // what date created
     private Boolean status; // viewed, deleted

     @ManyToOne
     @JoinColumn(name = "notification_from", referencedColumnName = "userId")
     private Users from;

     @ManyToOne
     @JoinColumn(name = "notification_to", referencedColumnName = "userId")
     private Users to;

}
