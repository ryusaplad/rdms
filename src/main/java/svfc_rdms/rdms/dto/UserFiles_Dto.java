package svfc_rdms.rdms.dto;

import java.util.UUID;

public class UserFiles_Dto {

     private UUID fileId;
     private String name;
     private String size;
     private String status;
     private String dateUploaded;
     private String filePurpose;
     private String uploadedBy;

     public UserFiles_Dto(UUID fileId, String name, String size, String status, String dateUploaded, String filePurpose,
               String uploadedBy) {
          this.fileId = fileId;
          this.name = name;
          this.size = size;
          this.status = status;
          this.dateUploaded = dateUploaded;
          this.filePurpose = filePurpose;
          this.uploadedBy = uploadedBy;
     }

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
