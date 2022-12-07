package svfc_rdms.rdms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import svfc_rdms.rdms.repository.AdminRepository;
import svfc_rdms.rdms.repository.Admin_DocumentRepository;
import svfc_rdms.rdms.serviceImpl.MainServiceImpl;
import svfc_rdms.rdms.serviceImpl.StudentServiceImpl;

@Controller
public class GlobalController {

     @Autowired
     MainServiceImpl mainService;
     @Autowired
     Admin_DocumentRepository docRepo;

     @Autowired
     AdminRepository adminRepo;

     @Autowired
     StudentServiceImpl studService;

     @GetMapping(value = "/")
     public String loginPage() {

          return "/index";
     }




}
