package svfc_rdms.rdms.controller.RestControllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.service.Registrar.Registrar_SelfRequest_Service;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Global.GlobalServiceControllerImpl;
import svfc_rdms.rdms.serviceImpl.Teacher.Teacher_ServiceImpl;

@RestController
public class Teacher_Registrar_Request_RestController {

     @Autowired
     private Registrar_SelfRequest_Service registrar_SelfRequest_Service;

     @Autowired
     private Teacher_ServiceImpl teacherService;

     @Autowired
     private AdminServicesImpl adminService;

     @Autowired
     private GlobalServiceControllerImpl globalService;

     @GetMapping(value = "/{userType}/registrar/requests/load")
     public ResponseEntity<Object> loadRegistrarRequests(@PathVariable("userType") String userType, HttpSession session,
               HttpServletResponse response) {
          // Need to continue the displaying request between teacher,registrar and admin.
          if (globalService.validatePages(userType, response, session)) {
               userType = userType.toLowerCase();
               if (userType.contains("admin")) {
                    return adminService.viewAllRegistrarRequests();
               } else if (userType.contains("teacher")) {
                    return teacherService.displayAllRequests(session);
               } else if (userType.contains("registrar")) {
                    return registrar_SelfRequest_Service.displayAllRequests(session);
               }

          }
          return new ResponseEntity<>("You are performing invalid action, Please try again later.",
                    HttpStatus.BAD_REQUEST);
     }
}
