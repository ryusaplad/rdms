package svfc_rdms.rdms.model;

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

      @Column(columnDefinition = "text")
      private String message;
      @Column(columnDefinition = "text")
      private String reply;

      private String requestDate;
      private String requestStatus;

      private String releaseDate;
      private String targetDate;
      @Column(columnDefinition = "text")
      private String manageBy;

      @ManyToOne
      @JoinColumn(name = "request_by", referencedColumnName = "userId")
      private Users requestBy;



}
