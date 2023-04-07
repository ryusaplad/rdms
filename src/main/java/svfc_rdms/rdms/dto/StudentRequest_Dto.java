package svfc_rdms.rdms.dto;

import java.util.UUID;

public class StudentRequest_Dto {

     private long requestId;
     private String year;
     private String course;
     private String semester;
     private String requestDocument;
     private String message;
     private String reply;
     private String requestDate;
     private String requestStatus;
     private String releaseDate;
     private String manageBy;
     private String requestBy;
     private String name;

     // Student User Information
     private long studentId;
     private String type;

     // UserFiles Data
     private UUID fileId;
     private byte[] data;
     private String fname;
     private String size;
     private String status;
     private String dateUploaded;
     private String filePurpose;
     private Long uploadedBy;
     private String uploaderName;
     private Long requestWith;

     public StudentRequest_Dto(long requestId, long studentId, String accountType, String year, String course,
               String semester, String requestDocument,
               String message, String reply, String requestBy, String requestDate, String requestStatus,
               String releaseDate,
               String manageBy) {
          this.requestId = requestId;
          this.studentId = studentId;
          this.type = accountType;
          this.year = year;
          this.course = course;
          this.semester = semester;
          this.requestDocument = requestDocument;
          this.message = message;
          this.reply = reply;
          this.requestBy = requestBy;
          this.requestDate = requestDate;
          this.requestStatus = requestStatus;
          this.releaseDate = releaseDate;
          this.manageBy = manageBy;

     }

     public StudentRequest_Dto(long requestId, long studentId, String requestBy, String name, String accountType,
               String year, String course,
               String semester, String requestDocument,
               String message, String reply, String requestDate, String requestStatus, String releaseDate,
               String manageBy) {
          this.requestId = requestId;
          this.studentId = studentId;
          this.type = accountType;
          this.year = year;
          this.course = course;
          this.semester = semester;
          this.requestDocument = requestDocument;
          this.message = message;
          this.reply = reply;
          this.requestBy = requestBy;
          this.name = name;
          this.requestDate = requestDate;
          this.requestStatus = requestStatus;
          this.releaseDate = releaseDate;
          this.manageBy = manageBy;

     }

     public StudentRequest_Dto(UUID fileId, byte[] data, String fname, String size, String status, String dateUploaded,
               String filePurpose, Long requestWith, String uploadedBy) {
          this.fileId = fileId;
          this.data = data;
          this.fname = fname;
          this.size = size;
          this.status = status;
          this.dateUploaded = dateUploaded;
          this.filePurpose = filePurpose;
          this.requestWith = requestWith;
          this.uploaderName = uploadedBy;
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

     public String getReply() {
          return reply;
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

     public String getName() {
          return name;
     }

     public long getStudentId() {
          return studentId;
     }

     public String getType() {
          return type;
     }

     public UUID getFileId() {
          return fileId;
     }

     public byte[] getData() {
          return data;
     }

     public String getFname() {
          return fname;
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

     public Long getUploadedBy() {
          return uploadedBy;
     }

     public String getUploaderName() {
          return uploaderName;
     }
     public Long getRequestWith() {
          return requestWith;
     }


}
