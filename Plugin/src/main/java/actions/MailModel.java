package actions;

import config.Constants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailModel {
    private String password = Constants.EMAIL_SENDER_PASSWORD;
    private String toEmail = "andrea.fischer@stud.uni-regensburg.de";
    private String fromEmail = Constants.EMAIL_SENDER_ADDRESS;
    private Authenticator auth;
    private Properties props;

    public MailModel() {
        configMail();
        authenticate();
    }

    public void sendMailToTutors() {
        //Test Mail an mich, später soll es natürlich an den Mailverteiler der Tutoren gehen
        Session session = Session.getInstance(props, auth);
        sendEmail(session, fromEmail, toEmail, Constants.EMAIL_SUBJECT, Constants.EMAIL_BODY);
    }

    private void  configMail() {
        props = new Properties();
        props.put("mail.smtp.user", Constants.EMAIL_SENDER_USER);
        props.put("mail.smtp.host", Constants.HOST); //SMTP Host
        props.put("mail.smtp.port", Constants.PORT); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.socketFactory.port", 587);
    }

    private void authenticate() {
        SecurityManager security = System.getSecurityManager();

        auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.EMAIL_SENDER_USER, password);
            }
        };
    }

    public static void sendEmail(Session session, String fromMail, String toEmail, String subject, String body) {
        try {
            MimeMessage message = new MimeMessage(session);
            //set message headers
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            message.setFrom(new InternetAddress(fromMail));
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            message.setSentDate(new Date());

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(message);
            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
