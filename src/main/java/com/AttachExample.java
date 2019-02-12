package com;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
public class AttachExample {
    public static void main (String args[]) throws Exception
    {
        System.getProperties().put("proxySet","true");
//        System.getProperties().put("socksProxyPort","1080");
//        System.getProperties().put("socksProxyHost","192.168.155.1");
        Properties props = System.getProperties();
        props.setProperty("socksProxyHost","100.100.101.58");
        props.setProperty("socksProxyPort","3128");
        String from = "cemilkaba@m";
        String to = "d@hotmail.com";
        String filename = "AttachExample.java";


        // Get system properties
        final String username = "*****";
        final String password = "*****";

        props.put("mail.user", username);
        props.put("mail.host", "mail.spymac.com");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");


        Session session = Session.getDefaultInstance(props,
                new Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }});

        // Define message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Hello JavaMail Attachment");
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();
        // Fill the message
        messageBodyPart.setText("Here's the file");
        // Create a Multipart
        Multipart multipart = new MimeMultipart();
        // Add part one
        multipart.addBodyPart(messageBodyPart);
        // // Part two is attachment // // Create second body part
        messageBodyPart = new MimeBodyPart();
        // Get the attachment
        DataSource source = new FileDataSource(filename);
        // Set the data handler to the attachment
        messageBodyPart.setDataHandler(new DataHandler(source));
        // Set the filename
        messageBodyPart.setFileName(filename);
        // Add part two
        multipart.addBodyPart(messageBodyPart);
        // Put parts in message
        message.setContent(multipart);
        // Send the message
        Transport.send(message);
    }
}