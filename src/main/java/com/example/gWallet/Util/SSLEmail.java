package com.example.gWallet.Util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SSLEmail {

    public static void mail(String toPerson, String body, String subject)
    {
        String fromEmail = "umangd98@gmail.com";
        String password = "priya91-";
        String toEmail =toPerson;
        System.out.println("SSLEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("umangd98@gmail.com", "priya91-");
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        System.out.println("Session created");

        EmailUtil.sendEmail(session, toEmail, subject, body);
    }


}
