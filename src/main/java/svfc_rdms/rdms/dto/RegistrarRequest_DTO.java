package svfc_rdms.rdms.dto;

import java.util.UUID;

public class RegistrarRequest_DTO {

     private long requestId;
     private String requestTitle;
     private String requestMessage;
     private String teacherMessage;
     private String requestBy;
     private String requestTo;
     private String requestDate;
     private String dateOfUpdate;
     private String requestStatus;

     private UUID fileId;
     private String name;
     private String size;
     private String status;
     private String dateUploaded;
     private String filePurpose;
     private String uploadedBy;

     public RegistrarRequest_DTO(UUID fileId, String name, String size, String status, String dateUploaded,
               String filePurpose,
               String uploadedBy) {
          this.fileId = fileId;
          this.name = name;
          this.size = size;
          this.status = status;
          this.dateUploaded = dateUploaded;
          this.filePurpose = filePurpose;
          this.uploadedBy = uploadedBy;
     }

     public RegistrarRequest_DTO(long requestId, String requestTitle, String requestMessage, String teacherMessage,
               String requestBy,
               String requestTo, String requestDate, String dateOfUpdate, String requestStatus) {
          this.requestId = requestId;
          this.requestTitle = requestTitle;
          this.requestMessage = requestMessage;
          this.teacherMessage = teacherMessage;
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

     public String getTeacherMessage() {
          return teacherMessage;
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

     //
     public UUID getFileId() {
          return fileId;
     }

     public String getName() {
          return name;
     }

     public String getSize() {
          return size;
     }

     public String getStatus() {
          return status;
     }

     public String getDateUploaded() {
          return dateUploaded;
     }

     public String getFilePurpose() {
          return filePurpose;
     }

     public String getUploadedBy() {
          return uploadedBy;
     }

}
