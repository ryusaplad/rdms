package svfc_rdms.rdms.dto;

public class RegistrarRequest_DTO {

     private long requestId;
     private String requestTitle;
     private String requestMessage;
     private String requestBy;
     private String requestTo;
     private String requestDate;
     private String dateOfUpdate;
     private String requestStatus;

     public RegistrarRequest_DTO(long requestId, String requestTitle, String requestMessage, String requestBy,
               String requestTo, String requestDate, String dateOfUpdate, String requestStatus) {
          this.requestId = requestId;
          this.requestTitle = requestTitle;
          this.requestMessage = requestMessage;
          this.requestBy = requestBy;
          this.requestTo = requestTo;
          this.requestDate = requestDate;
          this.dateOfUpdate = dateOfUpdate;
          this.requestStatus = requestStatus;
     }

     public long getRequestId() {
          return requestId;
     }

     public String getRequestTitle() {
          return requestTitle;
     }

     public String getRequestMessage() {
          return requestMessage;
     }

     public String getRequestBy() {
          return requestBy;
     }

     public String getRequestTo() {
          return requestTo;
     }

     public String getRequestDate() {
          return requestDate;
     }

     public String getDateOfUpdate() {
          return dateOfUpdate;
     }

     public String getRequestStatus() {
          return requestStatus;
     }

}
