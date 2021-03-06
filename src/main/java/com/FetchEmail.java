package com;

import com.sun.net.ssl.internal.ssl.Provider;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

public class FetchEmail{

    public static void main(String argv[]) throws Exception {

        Security.addProvider(new Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("proxySet","true");
        props.setProperty("socksProxyHost","100.100.101.58");
        props.setProperty("socksProxyPort","3128");

        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");

        Session session = Session.getDefaultInstance(props,null);


        URLName urln = new URLName("pop3","pop.gmail.com",995,null,
                "dd@gmail.com", "---");
        Store store = session.getStore(urln);
        Folder inbox = null;
        try {
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            FetchProfile profile = new FetchProfile();
            profile.add(FetchProfile.Item.ENVELOPE);
            Message[] messages = inbox.getMessages();
            inbox.fetch(messages, profile);
            System.out.println("Inbox Number of Message" + messages.length);
            for (int i = 0; i < messages.length; i++) {

                String from = decodeText(messages.clone()[0].toString());

                InternetAddress ia = new InternetAddress(from);
                System.out.println("FROM:" + ia.getPersonal()+'('+ia.getAddress()+')');



                System.out.println("TITLE:" + messages[i].getSubject());



                System.out.println("DATE:" + messages[i].getSentDate());

            }

        }

        finally {

            try {

                inbox.close(false);

            }

            catch (Exception e) {

            }

            try {

                store.close();

            }

            catch (Exception e) {

            }

        }

    }



    protected static String decodeText(String text)

            throws UnsupportedEncodingException {

        if (text == null)

            return null;

        if (text.startsWith("=?GB") || text.startsWith("=?gb"))

            text = MimeUtility.decodeText(text);

        else

            text = new String(text.getBytes("ISO8859_1"));

        return text;

    }



}
