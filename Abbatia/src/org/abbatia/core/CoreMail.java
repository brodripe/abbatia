package org.abbatia.core;
//import com.sun.corba.se.internal.iiop.messages.Message;

import org.abbatia.bean.Email;
import org.abbatia.exception.CorreoNoEnviadoException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Date;


public class CoreMail {
    private static Logger log = Logger.getLogger(CoreMail.class.getName());
    static String from = "altas@abbatia.net";
    // Autentificar por SMTP
    static String auth = "true";
    static String authLogin = "altas@abbatia.net";
    static String authPass = "2666W3";

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";


    public static void enviarNotificacionAlta(ArrayList listaCorreo, String asunto) throws AbadiaException {
        //boolean error = true;
        Iterator lista = listaCorreo.iterator();
        Email mail = null;
        while (lista.hasNext()) {
            mail = (Email) lista.next();
            mail.setAsunto(asunto);
            mail.setMsg("");
            enviarEmail(mail);
        }
        //return error;

    }

    public static void enviarCorreo(ArrayList listaCorreo) throws AbadiaException {
        for (Object aListaCorreo : listaCorreo) {
            enviarEmail((Email) aListaCorreo);
        }
    }

    public static void enviarEmail(Email email) throws AbadiaException {
        boolean debug = true;//, error = true;
        String destinatario;
        // create some properties and get the default Session
        Properties prop;
        Session session;

        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            prop = new Properties();
            prop.put("mail.smtp.host", SMTP_HOST_NAME);
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.debug", "true");
            prop.put("mail.smtp.port", SMTP_PORT);
            prop.put("mail.smtp.socketFactory.port", SMTP_PORT);
            prop.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            prop.put("mail.smtp.socketFactory.fallback", "false");

            session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(authLogin, authPass);
                        }
                    });

            session.setDebug(debug);

            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            InternetAddress[] replyTo = new InternetAddress[1];
            replyTo[0] = addressFrom;
            msg.setReplyTo(replyTo);

            InternetAddress[] addressTo = {new InternetAddress(email.getTo())};
            msg.setRecipients(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type

            msg.setSubject(email.getAsunto());
            //incluimos las partes del contenido
            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(email.getMsg());

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);


            //si el correo tiene un fichero adjunto...
            if (email.getFilename() != null)
            {
                // attach the file to the message
                FileDataSource fds = new FileDataSource(email.getFilename());
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
                mp.addBodyPart(mbp2);
            }

            msg.setContent(mp);
            msg.setSentDate(new Date());

            //msg.setContent(email.getMsg(), "text/plain");
            //msg.
            Transport.send(msg);

        } catch (Exception mex) {
            throw new CorreoNoEnviadoException("CoreMail.enviarMail", mex, log);
        }
    }

    // Autenticador ;-)
    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        protected PasswordAuthentication getPasswordAuthentication() {
            String username = authLogin;
            String password = authPass;
            return new PasswordAuthentication(username, password);
        }
    }

}
