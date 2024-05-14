package ps.jmagna.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${mail.service.sender}")
    private String senderEmail;

    @Value("${mail.service.password}")
    private String password;

    public void sendEmail(String subject, String text, String receptorEmail) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "mail.gmail.com");
        properties.put("mail.smtp.ssl.trust", "mail.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.mail.sender", senderEmail);
        properties.put("mail.smtp.user", "ComoLoHagoAdmin");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptorEmail));
            message.setSubject(subject);
            message.setText(text);

            Transport t = session.getTransport("smtp");
            t.connect(senderEmail, password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            throw new RuntimeException("Error de mesajeria: " + me);
        }

    }

}