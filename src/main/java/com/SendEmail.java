package com;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class SendEmail {

    private String userName;
    private String password;
    private Store store;
    private Folder box;

    /**
     * @param userName
     * @param password
     */
    public void login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param text
     * @param attachment
     * @return
     * @throws MessagingException
     */
    public Message createMessage(String[] to, String[] cc, String[] bcc, String subject, String text, String[] attachment)
            throws MessagingException {
        Properties properties = new Properties();
        // for example smtp host is hotmail
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.socks.host","100.100.101.58");
        properties.put("mail.smtp.socks.port","3128");

        Session session = this.getSession(properties);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName));
        message.setSubject(subject);
        for (int i = 0; i < to.length; i++)
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        for (int i = 0; i < cc.length; i++)
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
        for (int i = 0; i < bcc.length; i++)
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));

        Message msg = setContent(message, text, attachment);

        return msg;
    }

    /**
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param text
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException
     */
    public void send(String to, String cc, String bcc, String subject, String text) throws AddressException, MessagingException,
            IOException {
        this.send(new String[]{to}, new String[]{cc}, new String[]{bcc}, subject, text, new String[]{});
    }

    /**
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param text
     * @param attachment
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException
     */
    public void send(String[] to, String[] cc, String[] bcc, String subject, String text, String[] attachment)
            throws AddressException,
            MessagingException,
            IOException {

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
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
        Session session = this.getSession(props);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName));
        message.setSubject(subject);
        for (int i = 0; i < to.length; i++)
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        for (int i = 0; i < cc.length; i++)
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
        for (int i = 0; i < bcc.length; i++)
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));

        Message msg = setContent(message, text, attachment);
        Transport.send(msg);
        System.out.println("The mail was successfully sent");
    }

    /**
     * @param message
     * @param to
     * @param cc
     * @param bcc
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException
     */
    public void send(Message message, String[] to, String[] cc, String[] bcc) throws AddressException, MessagingException,
            IOException {
        for (int i = 0; i < to.length; i++)
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        for (int i = 0; i < cc.length; i++)
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
        for (int i = 0; i < bcc.length; i++)
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));

        Transport.send(message);
        System.out.println("The mail was successfully sent");
    }

    /**
     * @param message
     * @throws Exception
     */
    public void read(Message message) throws Exception {
        String from = InternetAddress.toString(message.getFrom());
        String replyTo = InternetAddress.toString(message.getReplyTo());
        String subject = message.getSubject();
        Date sentDate = message.getSentDate();
        Date receivedDate = message.getReceivedDate();

        System.out.println("Message Start");
        System.out.println();
        System.out.println("From: " + from);
        System.out.println("Reply To: " + replyTo);
        System.out.println("Subject : " + subject);
        System.out.println("Sent Date: " + sentDate);
        System.out.println("Received Date : " + receivedDate);
        System.out.println();

        Multipart mp = (Multipart) message.getContent();
        for (int i = 0; i < mp.getCount(); i++) {
            BodyPart body = mp.getBodyPart(i);
            System.out.println("Content " + (i + 1));
            this.printPart(body);
        }
        System.out.println("Message End");
    }

    /**
     * @param message
     * @throws MessagingException
     * @throws IOException
     */
    public void delete(Message message) throws MessagingException, IOException {
        message.setFlag(Flag.DELETED, true);
        this.box.close(true);
        this.store.close();
        System.out.println("[INFO]: Message deleted");
    }

    /**
     * @param message
     * @throws MessagingException
     * @throws IOException
     */
    public void markAsRead(Message message) throws MessagingException, IOException {
        message.setFlag(Flag.SEEN, true);
        this.box.close(true);
        this.store.close();
        System.out.println("[INFO]: Message marked as read");
    }

    /**
     * @param message
     * @param text
     * @param attachments
     * @param replyAll
     * @throws MessagingException
     * @throws IOException
     */
    public void reply(Message message, String text, String attachments[], boolean replyAll) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties);
        Message replyMessage = new MimeMessage(session);
        replyMessage = message.reply(replyAll);
        replyMessage.setFrom(new InternetAddress(userName));
        replyMessage.setReplyTo(message.getReplyTo());
        replyMessage = setContent(replyMessage, text, attachments);

        Transport t = session.getTransport("smtp");
        try {
            t.connect(userName, password);
            t.sendMessage(replyMessage, replyMessage.getAllRecipients());
        } finally {
            t.close();
        }
        System.out.println("[INFO]: Message Replied");
        box.close(true);
        store.close();
    }

    /**
     * @param message
     * @param to
     * @throws MessagingException
     * @throws IOException
     */
    public void forward(Message message, String to) throws MessagingException, IOException {
        this.forward(message, new String[]{to});
    }

    /**
     * @param message
     * @param to
     * @throws MessagingException
     * @throws IOException
     */
    public void forward(Message message, String[] to) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "587");


        String from = InternetAddress.toString(message.getFrom());
        String subject = message.getSubject();

        Session session = this.getSession(properties);
        Message forward = new MimeMessage(session);
        forward.setSubject("Fwd: " + subject);
        forward.setFrom(new InternetAddress(from));
        for (int i = 0; i < to.length; i++)
            forward.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));

        MimeBodyPart part1 = new MimeBodyPart();
        part1.setContent(message, "message/rfc822");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(part1);
        // set content
        forward.setContent(multipart);
        forward.saveChanges();

        // Send message
        Transport.send(forward);
        System.out.println("[INFO]: Message forwarded");
    }

    /**
     * @param folder
     * @param subject
     * @param from
     * @param openType
     * @return
     * @throws MessagingException
     */
    public Message[] getPopMessage(String folder, String subject, String from, int openType) throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.pop3s.port", "995");

        Session session = Session.getDefaultInstance(properties);
        this.store = session.getStore("pop3s");

        this.store.connect("pop3.live.com", userName, password);

        this.box = store.getFolder(folder);
        this.box.open(openType);

        SubjectTerm subjectCriteria = new SubjectTerm(subject);
        FromStringTerm fromCriteria = new FromStringTerm(from);
        SearchTerm searchCriteria = new AndTerm(subjectCriteria, fromCriteria);
        return this.box.search(searchCriteria);
    }

    /**
     * @param folder
     * @param subject
     * @param from
     * @param openType
     * @return
     * @throws MessagingException
     */
    public Message[] getImapMessage(String folder, String subject, String from, int openType) throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "outlook.office365.com");
        properties.setProperty("mail.port", "993");
        properties.setProperty("mail.transport.protocol", "imaps");

        Session session = this.getSession(properties);
        this.store = session.getStore("imaps");
        this.store.connect("imap-mail.outlook.com", userName, password);

        this.box = this.store.getFolder(folder);
        this.box.open(openType);

        SubjectTerm subjectCriteria = new SubjectTerm(subject);
        FromStringTerm fromCriteria = new FromStringTerm(from);
        SearchTerm searchCriteria = new AndTerm(subjectCriteria, fromCriteria);
        return this.box.search(searchCriteria);
    }

    /**
     * @param part
     * @throws Exception
     */
    private void printPart(Part part) throws Exception {
        if (part.isMimeType("text/plain")) {
            System.out.println((String) part.getContent());
        }
        // check if the content has attachment
        else if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++)
                printPart(mp.getBodyPart(i));
        }
        // check if the content is a nested message
        else if (part.isMimeType("message/rfc822")) {
            printPart((Part) part.getContent());
        } else {
            System.out.println(part.getContentType());
        }
    }

    /**
     * @param props
     * @return
     */
    private Session getSession(Properties props) {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        return session;
    }

    /**
     * @param message
     * @param text
     * @param attachments
     * @return
     * @throws MessagingException
     */
    private Message setContent(Message message, String text, String[] attachments) throws MessagingException {
        Multipart multiPart = new MimeMultipart();

        if (attachments != null) {
            // external source part n
            for (String attachment : attachments) {
                DataSource source = new FileDataSource(attachment);
                BodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(attachment);
                multiPart.addBodyPart(attachmentPart);
            }
            // text part
            BodyPart textPart = new MimeBodyPart();
            textPart.setText(text);
            multiPart.addBodyPart(textPart);
            // set all contents
            message.setContent(multiPart);
        } else {
            message.setContent(text, "text/plain");
        }
        return message;
    }

    public static void main(String[] args) throws Exception {
        final String bobUserName = "454@hotmail.com";
        final String bobPassword = "545";
        final String aliceUserName = "454";
        final String alicePassword = "4545-";
//        final String johnUserName = "john@web.com";
//        final String johnPassword = "1234";
//        final String dennisUserName = "dennis@web.com";
//        final String dennisPassword = "1234";

        final String to[] = {aliceUserName};
        final String cc[] = {};
        final String bcc[] = {};
        final String attachments1[] = {"/home/kabakcic/Bilder/bob.jpg"};
        final String attachments2[] = {"/home/kabakcic/Bilder/alice.png"};
        final String INBOX = "INBOX";


        SendEmail bob = new SendEmail();
        bob.login(bobUserName, bobPassword);
        bob.send(to, cc, bcc, "Hello", "Hello Alice !", attachments1);

        //SendEmail alice = new SendEmail();
       // alice.login(aliceUserName, alicePassword);
       // Message message = alice.getPopMessage(INBOX, "Hello", bobUserName, Folder.READ_WRITE)[0];
        //alice.read(message);
       // alice.reply(message, "Hello Bob !", attachments2, true);

       // message = bob.getImapMessage(INBOX, "Hello", aliceUserName, Folder.READ_WRITE)[0];
        //bob.read(message);
        //bob.markAsRead(message);

        //SendEmail john = new SendEmail();
       // john.login(johnUserName, johnPassword);
       // message = john.getPopMessage(INBOX, "Hello", aliceUserName, Folder.READ_WRITE)[0];
       // john.forward(message, dennisUserName);

       // SendEmail dennis = new SendEmail();
       // dennis.login(dennisUserName, dennisPassword);
      //  message = dennis.getPopMessage(INBOX, "Hello", johnUserName, Folder.READ_WRITE)[0];
       // dennis.read(message);
       // dennis.delete(message);
    }
}