package svfc_rdms.rdms.service.Global;

import javax.mail.MessagingException;

public interface EmailService {
    public void  sendEmail(String from, String to, String subject, String text) throws MessagingException;
}
