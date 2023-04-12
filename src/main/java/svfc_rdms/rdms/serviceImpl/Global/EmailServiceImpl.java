package svfc_rdms.rdms.serviceImpl.Global;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.service.Global.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String from, String to, String subject, String contextMessage) {
       try {
        String colorCode = "";
           
        if (contextMessage.contains("ongoing")) {
            colorCode = "28a745";
        } else if (contextMessage.contains("rejected")) {
            colorCode = "a7284a";
        }
       
        String textWithHtml = "<div style='font-family: \"GT America Regular\",\"Roboto\",\"Helvetica\",\"Arial\",sans-serif; background-color: #f2f2f2;'>"
        + "<div style='background-color: #e9ecef; padding: 20px;'>"
        + "<div style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 20px; margin: 0 auto; max-width: 600px;'>"
        + "<img src='https://portal.svfc.com.ph/_assets/img/svdfc-logo.jpg' width='80' height='80' alt='Profile Picture' style='display: block; margin: 0 auto; max-width: 100%; height: auto; border-radius: 50%; text-align: center;'>"
        + "<h1 style='margin: 10px 0 5px; font-size: 24px; font-weight: bold; color: #000000; text-align: center;'>St. Vincent de Ferrer College of Camarin, Inc.</h1>"
        + "<hr style='margin-top:15px; border: 0; border-top: 1px solid #cccccc; text-align: center;'>"
        + "<div style='display: inline-block; font-size: 22px; font-weight: bold; color: #"
        + colorCode + "; border-radius: 5px;'>Approved</div>"
        + "<hr style='border: 0; border-top: 1px solid #cccccc; text-align: center;'>"
        + "<p style='margin-bottom: 20px; font-size: 16px; color: #000000; text-align: center;'>"
        + contextMessage + "</p>"
        + "<p style='font-size: 16px; color: #000000; text-align: center;'>Use your student no to access RDMS.</p>"
        + "<a href='javascript:void(0);' class='btn' style='display: inline-block; padding: 10px 0; width: 100%; font-size: 16px; font-weight: bold; color: #ffffff; background-color: #28a745; border-radius: 5px; text-decoration: none; text-align:center;'>Go to RDMS</a>"
        + "</div>"
        + "</div>"
        + "</div>";
        MimeMessage emailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(textWithHtml, true);
        helper.setReplyTo("noreply@example.com");

     
        javaMailSender.send(emailMessage);
       } catch (MessagingException e) {
            e.printStackTrace();
       }
       
       
    }

}
