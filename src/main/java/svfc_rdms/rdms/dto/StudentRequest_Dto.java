package svfc_rdms.rdms.dto;

public class StudentRequest_Dto {

     private long requestId;
     private String year;
     private String course;
     private String semester;
     private String requestDocument;
     private String message;
     private String requestDate;
     private String requestStatus;
     private String releaseDate;
     private String manageBy;
     private String requestBy;

     // Student User Information
     private long studentId;
     private String type;

     public StudentRequest_Dto(long requestId, long studentId, String accountType, String year, String course,
               String semester, String requestDocument,
               String message, String requestBy, String requestDate, String requestStatus, String releaseDate,
               String manageBy) {
          this.requestId = requestId;
          this.studentId = studentId;
          this.type = accountType;
          this.year = year;
          this.course = course;
          this.semester = semester;
          this.requestDocument = requestDocument;
          this.message = message;
          this.requestBy = requestBy;
          this.requestDate = requestDate;
          this.requestStatus = requestStatus;
          this.releaseDate = releaseDate;
          this.manageBy = manageBy;

     }

     public long getRequestId() {
          return requestId;
     }

     public String getYear() {
          return year;
     }

     public String getCourse() {
          return course;
     }

     public String getSemester() {
          return semester;
     }

     public String getRequestDocument() {
          return requestDocument;
     }

     public String getMessage() {
          return message;
     }

     public String getRequestDate() {
          return requestDate;
     }

     public String getRequestStatus() {
          return requestStatus;
     }

     public String getReleaseDate() {
          return releaseDate;
     }

     public String getManageBy() {
          return manageBy;
     }

     public String getRequestBy() {
          return requestBy;
     }

     public long getStudentId() {
          return studentId;
     }

     public String getType() {
          return type;
     }

}
