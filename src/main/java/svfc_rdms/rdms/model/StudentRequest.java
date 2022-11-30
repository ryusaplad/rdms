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

      // the name of requestor is in the requestBy
      private String year;
      private String course;
      private String semester; // What semester the document needed?

      @ManyToOne
      @JoinColumn(name = "request_document", referencedColumnName = "documentId")
      private Documents requestDocument; // what document? / COM/COR/ID/GRADES/ using by id of document

      // Requirements - user upload - for iD only
      @Column(name = "firstRequiremement", columnDefinition = "LONGBLOB")
      private byte firstRequirementFile;
      @Column(name = "secondRequiremement", columnDefinition = "LONGBLOB")
      private byte secondRequirementFile;

      // for com/cor and grades Requirement only
      private String message;
      // end

      // end of requirements
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
      private String requestDate; // what date request has been sent
      private String requestStatus; // Pending/ On-Going / Completed / Deleted //

      private String releaseDate;
      private String manageBy; // who are the users manage the request

      @ManyToOne
      @JoinColumn(name = "request_by", referencedColumnName = "userId")
      private Users requestBy; // One User to many Request

      /*
       * Important logic for this requesting do this with thymeleaf
       * 
       * 
       * if Request document is equal to id
       * # Requirements
       * - Student Id(userId)
       * - Student Name(name)
       * - ID Picture (UserFiles)
       * - Signature Image (UserFiles)
       * --Can be found on this class--
       * - Yr/Course
       * - Semester
       * #End
       * ----------------------------
       * if Request document is equal to COR / COM
       * #Requirements
       * - Student Id(userId)
       * - Student Name(name)
       * 
       * --Can be found on this class--
       * - Yr/Course
       * - Semester
       * ---
       * - Signature(file)
       * #End
       * ----------------------------
       * if rrequest document is equal to grades
       * - Student Id(userId)
       * - Student Name(name)
       * --Can be found on this class--
       * - Yr/Course
       * - Semester
       * - list of subjects
       * - list of professors
       * ---
       * - Signature(file)
       * #End
       * ----------------------------
       * 
       */

      /*
       * New Logic
       * if document type is equal to id
       * the showing or preview image will show
       * if not the preview image will not show
       * if
       */
}
