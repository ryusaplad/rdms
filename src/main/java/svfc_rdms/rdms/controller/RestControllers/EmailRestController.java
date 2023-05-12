package svfc_rdms.rdms.controller.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.serviceImpl.Global.EmailServiceImpl;

@RestController
@RequestMapping("/email")
public class EmailRestController {

    @Autowired
    private EmailServiceImpl myEmailService;

    // @GetMapping("/send")
    // public ResponseEntity<?> emailSender() {
        

    // }
}
