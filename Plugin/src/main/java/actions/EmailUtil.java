package actions;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;


public class EmailUtil {

    public static void sendEmail(Session session, String toEmail, String subject, String body) {
            try {
                MimeMessage message = new MimeMessage(session);
                //set message headers
                message.addHeader("Content-type", "text/HTML; charset=UTF-8");
                message.addHeader("format", "flowed");
                message.addHeader("Content-Transfer-Encoding", "8bit");

                //TODO: Das hier anpassen
                //message.setFrom(new InternetAddress("", "NoReply-JD"));
               // message.setFrom(new InternetAddress("andreafischer87656@gmail.com"));
                message.setFrom(new InternetAddress("andrea.fischer@stud.uni-regensburg.de"));
                //message.setReplyTo(InternetAddress.parse("", false));

                message.setSubject(subject, "UTF-8");
                message.setText(body, "UTF-8");
                message.setSentDate(new Date());

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                System.out.println("Message is ready");
                Transport.send(message);

                System.out.println("EMail Sent Successfully!!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

}


