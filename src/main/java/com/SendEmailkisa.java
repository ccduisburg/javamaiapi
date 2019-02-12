package com;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class SendEmailkisa {

    public static void main(String[] args) {

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = System.getProperties();
        props.setProperty("proxySet","true");
        props.setProperty("socksProxyHost","100.100.101.58");
        props.setProperty("socksProxyPort","3128");

        props.setProperty("http.proxyHost","100.100.101.58");
        props.setProperty("http.proxyPort","3128");

        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        SmtpAuthenticator authentication = new SmtpAuthenticator();

        try { // Get a session
            Session session = Session.getDefaultInstance(props, authentication);
// Create a new
            MimeMessage message = new MimeMessage(session); // Populate
            message.setText("Hello from first JavaMail message");
            message.setSubject("Hi!");
            message.setFrom(new InternetAddress("cemordu@hotmail.com"));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress("dd@hotmail.com"));
// Send the message
            Transport transport = session.getTransport("smtp");
            Transport.send(message);
        } catch (MessagingException me) {
            System.err.println(me.getMessage());
        }
        System.out.println("Message Sent");
    }
}