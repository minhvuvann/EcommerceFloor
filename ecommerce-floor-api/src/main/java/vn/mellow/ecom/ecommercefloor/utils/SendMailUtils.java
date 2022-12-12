package vn.mellow.ecom.ecommercefloor.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class SendMailUtils {
    public static String pwdMail ="qiswwsjutmgbraru";

    public static Session loginMail(String email, String pwd) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.starttls.required", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");



        Session resSession = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, pwd);
            }
        });
        return resSession;
    }

    public static void sendMailTo(Session sessMail, String emailFrom, String nameFrom, String emailTo, String subjectEMail, String messMail) {
        try {
            Message mess = new MimeMessage(sessMail);
            mess.setFrom(new InternetAddress(emailFrom, nameFrom));
            mess.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            mess.setSubject(subjectEMail);
            mess.setText(messMail);
            Transport.send(mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}