package com;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;


public class GmailSender {

    public static void main(String[] args) throws AddressException, MessagingException {


        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("proxySet","true");
        props.setProperty("socksProxyHost","100.100.101.58");
        props.setProperty("socksProxyPort","3128");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = "cemilkabakci@gmail.com";
        final String password = "Ugurlu#52";
        Session session = Session.getDefaultInstance(props,
                new Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }});

        // -- Create a new message --
        Message msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress("cemilkabakci@gmail.com"));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse("cemilkabakci@gmail.com",false));
        msg.setSubject("Hello");
        msg.setText("How are you");
        msg.setSentDate(new Date());
        Transport.send(msg);
        System.out.println("Message sent.");
    }
}



