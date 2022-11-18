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
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersRequest {

     @Id
     @SequenceGenerator(name = "request_sequence", sequenceName = "request_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_sequence")
     private long requestId;

     private String requestDocument; // what document? / COM/COR/ID/GRADES/
     // the name of requestor is in the requestBy
     private String yrAnCourse;
     private String semester; // What semester the document needed?
     // for grades Requirement only
     private String subjects;
     private String professors;
     // end
     private String requestType; // Walk -in / Or Not
     private String requestMessage; // can add message
     private String requestDate; // what date request has been sent
     private String requestStatus; // Pending/ On-Going / Completed / Deleted
     /*
      * the purpose of this, when the one who manage the request and send the request
      * to other person like registrar or teacher
      * the manageBy will be overwrite
      * 
      * Ex:
      * if the facilitator manage the student request
      * the manageby will be "Manage By Facilitator"
      * 
      * and If the facilitator send the request to the registrar
      * the manageBy will change to Manage by Facilitator , Registrar
      * 
      * Same to the teacher.
      */
     private String releaseDate;
     private String manageBy; // who are the users manage the request

     @ManyToOne
     @JoinColumn(name = "request_by", referencedColumnName = "userId")
     private Users requestBy; // One User to many Request

}
