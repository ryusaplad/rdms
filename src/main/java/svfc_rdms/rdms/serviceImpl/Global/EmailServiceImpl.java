package svfc_rdms.rdms.serviceImpl.Global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import svfc_rdms.rdms.service.Global.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("svfc_rdms2023@example.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setReplyTo("noreply@example.com"); // set the reply-to header to a non-existent or unmonitored email
                                                   // address
        javaMailSender.send(message);
    }

}
