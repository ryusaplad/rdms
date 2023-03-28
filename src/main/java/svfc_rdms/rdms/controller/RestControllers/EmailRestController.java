package svfc_rdms.rdms.controller.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import svfc_rdms.rdms.serviceImpl.Global.EmailServiceImpl;

@RestController
@RequestMapping("/email")
public class EmailRestController {

    @Autowired
    private EmailServiceImpl myEmailService;

    @GetMapping("/send")
    public ResponseEntity<?> emailSender() {
        myEmailService.sendEmail("Svfc_Rdms_Db@gmail.com", "changsaplad@gmail.com", "Test Email",
                "This is a test email.");
        return ResponseEntity.ok("Email Sent Successful");
    }
}
