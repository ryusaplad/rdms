package svfc_rdms.rdms.service.Global;

public interface EmailService {
    public void sendEmail(String from, String to, String subject, String text);
}
