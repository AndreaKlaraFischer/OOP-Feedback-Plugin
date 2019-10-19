package actions;

import javax.mail.*;
import java.util.Properties;



//Stand 10.10. --> Mit gmail funktioniert es, mit der Unimail noch die Authentifizierungsfehlermeldung --> Das beheben,
//wäre schöner als gmail.  Ansonsten richte ich eine gmail adresse dafür ein.
//TODO: Alles in Konstanten auslagern oder zumindest schön in Variablen
public class MailModel {
    final String fromEmail = "andrea.fischer@stud.uni-regensburg.de";
   // final String fromEmail = "andreafischer87656@gmail.com"; //requires valid gmail id
    final String password = "Spezi123123"; // correct password for gmail id
    //final String password = "4okb4gug"; // correct password for gmail id
    //Test Mail an mich, später soll es natürlich an den Mailverteiler der Tutoren gehen
    final String toEmail = "andrea.fischer@stud.uni-regensburg.de"; // can be any email id

    //String host = "smtp.gmail.com";
    String host = "smtp.uni-regensburg.de";
    String port = "587";


    public void sendMailToTutors() {
        Properties props = new Properties();
        //props.put("mail.smtp.host", ); //SMTP Host
       // props.put("mail.smtp.user", fromEmail);
        props.put("mail.smtp.user", "fia06900");
        //props.put("mail.smtp.username", "mel1555");
        props.put("mail.smtp.host", host); //SMTP Host
        props.put("mail.smtp.port", port); //TLS Port
        //props.put("mail.smtp.port", "465"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        props.put("mail.smtp.socketFactory.port", 587);
        //Bei gmail hatte ich das einkommentiert.
        /*props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");*/
        //create Authenticator object to pass in Session.getInstance argument

        SecurityManager security = System.getSecurityManager();

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fia06900", password);
            }
        };
        Session session = Session.getInstance(props, auth);
        EmailUtil.sendEmail(session, toEmail,"Es geht...", "...du Bangerin.");
    }
}
