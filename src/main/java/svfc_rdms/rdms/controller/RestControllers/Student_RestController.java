package svfc_rdms.rdms.controller.RestControllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

     @PostMapping("/student/request/{document}/sent")
     public ResponseEntity<String> studRequestSent(@RequestParam("studentId") String id,
               @RequestParam("file[]") MultipartFile[] files, @PathVariable String document,
               @RequestParam Map<String, String> params) {

          return studService.saveRequest(id, files, document, params);
     }

}
