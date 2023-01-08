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


      private String year;
      private String course;
      private String semester;

      @ManyToOne
      @JoinColumn(name = "request_document", referencedColumnName = "documentId")
      private Documents requestDocument;


      private String message;
      private String reply;

      private String requestDate;
      private String requestStatus;

      private String releaseDate;

      private String manageBy;

      @ManyToOne
      @JoinColumn(name = "request_by", referencedColumnName = "userId")
      private Users requestBy;

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
