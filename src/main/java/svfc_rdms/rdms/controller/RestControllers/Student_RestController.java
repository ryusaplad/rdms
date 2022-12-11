package svfc_rdms.rdms.controller.RestControllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Admin.AdminRepository;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.serviceImpl.File.FileUploadServiceImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@RestController
public class Student_RestController {

     @Autowired
     FileUploadServiceImpl studFileService;

     @Autowired
     AdminRepository adminRepo;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     StudentServiceImpl studService;

     @PostMapping("/save_file")

     public ResponseEntity<String> studFileUploadRequirement(@RequestParam("studentId") String id,
               @RequestParam("file[]") MultipartFile[] files) {
          try {

               LocalDate dateNow = LocalDate.now();

               if (files != null || id.length() < -1) {
                    Users user = adminRepo.findUserIdByUsername(id);
                    for (MultipartFile filex : files) {
                         UserFiles userFiles = new UserFiles();
                         userFiles.setData(filex.getBytes());
                         userFiles.setName(filex.getOriginalFilename());
                         userFiles.setSize(formatSize(filex.getSize()));
                         userFiles.setDateUploaded(dateNow.toString());
                         userFiles.setStatus("Pending");
                         userFiles.setUploadedBy(user);
                         studFileService.saveFiles(userFiles);
                    }

               } else {
                    return new ResponseEntity<>("No File Has been save", HttpStatus.BAD_REQUEST);
               }

          } catch (Exception ex) {
               ex.printStackTrace();
               return new ResponseEntity<>("Invalid file format!!", HttpStatus.BAD_REQUEST);
          }

          return new ResponseEntity<>("File uploaded!!", HttpStatus.OK);
     }

     public String formatSize(long size) {

          final long kilo = 1024;
          final long mega = kilo * kilo;
          final long giga = mega * kilo;
          final long tera = giga * kilo;

          double kb = size / kilo;
          double mb = kb / kilo;
          double gb = mb / kilo;
          double tb = gb / kilo;
          String formatedSize = "";
          if (size < kilo) {
               formatedSize = size + " Bytes";
          } else if (size >= kilo && size < mega) {
               formatedSize = String.format("%.2f", kb) + " KB";
          } else if (size >= mega && size < giga) {
               formatedSize = String.format("%.2f", mb) + " MB";
          } else if (size >= giga && size < tera) {
               formatedSize = String.format("%.2f", gb) + " GB";
          } else if (size >= tera) {
               formatedSize = String.format("%.2f", tb) + " TB";
          }
          return formatedSize;

     }
}
