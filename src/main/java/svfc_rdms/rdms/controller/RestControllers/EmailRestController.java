package svfc_rdms.rdms.controller.RestControllers;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        String textWithHtml = "<div style='font-family: \"GT America Regular\",\"Roboto\",\"Helvetica\",\"Arial\",sans-serif; background-color: #f2f2f2;'>"
                + "<div style='background-color: #e9ecef; padding: 20px;'>"
                + "<div style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 20px; margin: 0 auto; max-width: 600px;'>"
                + "<img src='https://scontent.fmnl17-2.fna.fbcdn.net/v/t39.30808-6/302349848_419523650165763_8414489783644060698_n.png?_nc_cat=109&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=x6aYKuWS5jwAX_zQlro&_nc_pt=1&_nc_ht=scontent.fmnl17-2.fna&oh=00_AfC12ko0zXIzkIwKAWegEorqrt8ZEzgeucUENMC6E9nJYg&oe=6434E7B7' width='80' height='80' alt='Profile Picture' style='display: block; margin: 0 auto; max-width: 100%; height: auto; border-radius: 50%; text-align: center;'>"
                + "<h1 style='margin: 10px 0 5px; font-size: 24px; font-weight: bold; color: #000000; text-align: center;'>St. Vincent de Ferrer College of Camarin, Inc.</h1>"
                + "<hr style='margin-top:15px; border: 0; border-top: 1px solid #cccccc; text-align: center;'>"
                + "<div style='display: inline-block; font-size: 22px; font-weight: bold; color: #28a745; border-radius: 5px;'>Approved</div>"
                + "<hr style='border: 0; border-top: 1px solid #cccccc; text-align: center;'>"
                + "<p style='margin-bottom: 20px; font-size: 16px; color: #000000; text-align: center;'>Your requested 'ID' has been approved. Please check 'My Requests' for further information.'</p>"
                + "<p style='font-size: 16px; color: #000000; text-align: center;'>Use your student no to access RDMS.</p>"
                + "<a href='https://example.com' class='btn' style='display: inline-block; padding: 10px 0; width: 100%; font-size: 16px; font-weight: bold; color: #ffffff; background-color: #28a745; border-radius: 5px; text-decoration: none; text-align:center;'>Go to RDMS</a>"
                + "</div>"
                + "</div>"
                + "</div>";
        try {
            myEmailService.sendEmail("noreply@gmail.com", "ryusaps06@gmail.com", "Test Email",
                    textWithHtml);
            return ResponseEntity.ok("Email Sent Successful");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Email failed to send. Reason: " + e.getMessage());
        }

    }
}
