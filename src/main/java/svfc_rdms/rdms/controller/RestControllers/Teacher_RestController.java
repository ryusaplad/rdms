package svfc_rdms.rdms.controller.RestControllers;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import svfc_rdms.rdms.serviceImpl.Teacher.Teacher_ServiceImpl;

@RestController
public class Teacher_RestController {

     @Autowired
     private Teacher_ServiceImpl teach_ServiceImpl;

     @GetMapping("/teacher/requests-view")
     public ResponseEntity<Object> viewRequestsFromRegistrar(@RequestParam long requestId, HttpSession session,
               @RequestParam Map<String, String> params) {

          return teach_ServiceImpl.viewRegistrarRequests(requestId);
     }

     @PostMapping("/teacher/re-sent-request")
     public ResponseEntity<String> sentRequestsToRegistrar(@RequestParam long requestId, HttpSession session,
               @RequestParam("file[]") Optional<MultipartFile[]> files,
               @RequestParam Map<String, String> params,HttpServletRequest request) {

          return teach_ServiceImpl.sendRequestToRegistrar(requestId, session, files, params,request);
     }
}
