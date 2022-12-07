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
@Table(name = "student_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
      @Id
      @SequenceGenerator(name = "student_request_sequence", sequenceName = "student_request_sequence", allocationSize = 1)
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_request_sequence")
      private long requestId;

      // the name of requestor is in the requestBy
      private String year;
      private String course;
      private String semester; // What semester the document needed?

      @ManyToOne
      @JoinColumn(name = "request_document", referencedColumnName = "documentId")
      private Documents requestDocument; // what document? / COM/COR/ID/GRADES/ using by id of document


      private String message;

      private String requestDate; // what date request has been sent
      private String requestStatus; // Pending/ On-Going / Completed / Deleted //

      private String releaseDate;

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
      private String manageBy; // who are the users manage the request

      @ManyToOne
      @JoinColumn(name = "request_by", referencedColumnName = "userId")
      private Users requestBy; // One User to many Request

      public StudentRequest(long requestId, String year, String course, String semester, String requestDocument,
                  String message, Users requestBy, String requestDate, String requestStatus, String releaseDate,
                  String manageBy) {
            this.requestId = requestId;
            this.year = year;
            this.course = course;
            this.semester = semester;
            requestDocument = this.requestDocument.getTitle();
            this.message = message;
            this.requestBy = requestBy;
            this.requestDate = requestDate;
            this.requestStatus = requestStatus;
            this.releaseDate = releaseDate;
            this.manageBy = manageBy;

      }

}
