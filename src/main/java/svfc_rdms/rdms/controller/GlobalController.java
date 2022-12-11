package svfc_rdms.rdms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import svfc_rdms.rdms.repository.Admin.AdminRepository;
import svfc_rdms.rdms.repository.Document.DocumentRepository;
import svfc_rdms.rdms.serviceImpl.Admin.AdminServicesImpl;
import svfc_rdms.rdms.serviceImpl.Student.StudentServiceImpl;

@Controller
public class GlobalController {

     @Autowired
     AdminServicesImpl mainService;
     @Autowired
     DocumentRepository docRepo;

     @Autowired
     AdminRepository adminRepo;

     @Autowired
     StudentServiceImpl studService;

     @GetMapping(value = "/")
     public String loginPage() {

          return "/index";
     }




}
