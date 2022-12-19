package svfc_rdms.rdms.serviceImpl.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.ExceptionHandler.ApiRequestException;
import svfc_rdms.rdms.model.Documents;
import svfc_rdms.rdms.model.StudentRequest;
import svfc_rdms.rdms.model.UserFiles;
import svfc_rdms.rdms.model.Users;
import svfc_rdms.rdms.repository.Admin.AdminRepository;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.repository.File.FileRepository;
import svfc_rdms.rdms.repository.Student.StudentRepository;
import svfc_rdms.rdms.service.File.FileService;
import svfc_rdms.rdms.service.Student.StudentService;

@Service
public class StudentServiceImpl implements StudentService, FileService {

     @Autowired
     StudentRepository studRepo;

     @Autowired
     DocumentRepository docRepo;

     @Autowired
     AdminRepository adminRepo;

     @Autowired
     FileRepository fileRepo;

     @Override
     public ResponseEntity<String> saveRequest(String id,
               MultipartFile[] files, String document,
               Map<String, String> params) {

          try {

               List<String> excludedFiles = new ArrayList<>();

               LocalDate dateNow = LocalDate.now();

               if (files != null || !id.isEmpty() || !document.isEmpty() || params.size() > 0) {
                    StudentRequest req = new StudentRequest();
                    Users user = new Users();

                    for (Map.Entry<String, String> entry : params.entrySet()) {

                         if (entry.getKey().equals("userId")) {
                              user = adminRepo.findUserIdByUsername(entry.getValue()).get();
                              req.setRequestBy(user);
                         } else if (entry.getKey().equals("year")) {
                              req.setYear(entry.getValue());
                         } else if (entry.getKey().equals("course")) {
                              req.setCourse(entry.getValue());
                         } else if (entry.getKey().equals("semester")) {
                              req.setSemester(entry.getValue());
                         } else if (entry.getKey().contains("excluded")) {
                              excludedFiles.add(entry.getValue());
                         } else if (entry.getKey().contains("message")) {
                              if (entry.getValue().length() > 250) {
                                   throw new ApiRequestException("Invalid Message length, must be 250 letters below.");
                              }
                              req.setMessage(entry.getValue());
                         }
                    }
                    System.out.println("Excluded Files");
                    excludedFiles.stream().forEach(e -> {
                         System.out.println(e);

                    });

                    req.setRequestDate("2022");
                    req.setRequestStatus("Pending");
                    req.setReleaseDate("2022");
                    req.setManageBy("admin");

                    if (findDocumentByTitle(document).isPresent()) {
                         if (docRepo.findByTitle(document).isPresent()) {
                              req.setRequestDocument(docRepo.findByTitle(document).get());
                         }
                    }
                    System.out.println("Included Files");
                    for (MultipartFile filex : files) {

                         if (!excludedFiles.contains(filex.getOriginalFilename())) {
                              UserFiles userFiles = new UserFiles();
                              userFiles.setData(filex.getBytes());
                              userFiles.setName(filex.getOriginalFilename());
                              userFiles.setSize(formatSize(filex.getSize()));
                              userFiles.setDateUploaded(dateNow.toString());
                              userFiles.setStatus("Pending");
                              userFiles.setFilePurpose("requirement");

                              userFiles.setUploadedBy(user);
                              userFiles.setRequestWith(req);
                              // saveFiles(userFiles);
                              System.out.println(filex.getOriginalFilename());
                         }

                    }
                    // studRepo.save(req);
                    return new ResponseEntity<>("Request Submited.", HttpStatus.OK);
               } else {
                    return new ResponseEntity<>("Required Informations, cannot be empty!", HttpStatus.BAD_REQUEST);
               }

          } catch (Exception ex) {
               ex.printStackTrace();
               return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
          }

     }

     @Override
     public Optional<Documents> findDocumentByTitle(String title) {
          Optional<Documents> documentTitleOptional = docRepo.findByTitle(title);
          if (documentTitleOptional.isPresent()) {
               return documentTitleOptional;
          }
          return null;

     }

     @Override
     public List<StudentRequest> displayRequestByStudent(Users user) {

          return studRepo.findAllByRequestBy(user);
     }

     @Override
     public Optional<Users> getUserIdByUsername(String username) {
          Optional<Users> userIdOptional = adminRepo.findUserIdByUsername(username);
          if (userIdOptional.isPresent()) {
               return userIdOptional;
          }
          return null;
     }

     @Override
     public boolean saveFiles(UserFiles files) {

          if (files != null) {

               fileRepo.save(files);
               return true;
          }
          return false;
     }

     @Override
     public List<UserFiles> getAllFiles() {

          return fileRepo.findAll();
     }

     @Override
     public Optional<UserFiles> getFileById(long id) {

          Optional<UserFiles> fileOptional = fileRepo.findById(id);
          if (fileOptional.isPresent()) {
               return fileOptional;
          }
          return null;
     }

     @Override
     public Boolean deleteFile(long id) {

          return null;
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
