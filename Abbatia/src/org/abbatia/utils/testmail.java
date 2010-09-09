package org.abbatia.utils;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import java.util.Properties;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: benjamin
 * Date: 24-ene-2008
 * Time: 22:54:52
 * To change this template use File | Settings | File Templates.
 */
public class testmail
{
    public static void main(String[] argv)
    {
        String to = "benjamin.rodriguez.perez@gmail.com";     // to address
        String from = "webmaster@abbatia.net"; // fromaddress
        String subject = "Titulo del correo";
        String message = "<b>Mensaje de prueba</b><br><h1>Hola></h1>";
        String mailhost = "smtp.gmail.com"; // servidor smtp de gmail(smtps), pero en esta linea no lleva la "s" de "smtps"
        String user = "benjamin@abbatia.net"; // Aqui va tu nombre de usuario, observa que va tambien "@gmail.com" no solamente el nombre de usuario
        String password = "cyborg";              // Aqui va tu password

        // in the case of an exception, print a message to the output log
        boolean auth = true;
        boolean ssl = true;
        Properties props = System.getProperties();

        if (mailhost != null){
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtps.host", mailhost);
        }
        if (auth){
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtps.auth", "true");
        }

        props.put("mail.smtp.port", "25");
        props.put("mail.smtps.port", "465");

        // Get a Session object
        javax.mail.Session session = javax.mail.Session.getInstance(props);
        session.setDebug(true);

        // construct the message
        javax.mail.Message msg = new MimeMessage(session);

        try {
            //  Set message details
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(message, "text/html");
            //msg.setText(message);

            // send the thing off
            SMTPTransport t =
                    (SMTPTransport)session.getTransport("smtp"); //si ssl=true, smtps si no smtp
            try {
                if (auth)
                    t.connect(mailhost, user, password);
                else
                    t.connect();
                t.sendMessage(msg, msg.getAllRecipients());
            } finally {
                t.close();
            }
            //log("Mail was sent successfully.");

        } catch (Exception e) {
            if (e instanceof SendFailedException) {
                MessagingException sfe = (MessagingException)e;
                Exception ne;
                while ((ne = sfe.getNextException()) != null &&
                    ne instanceof MessagingException)     {
            sfe = (MessagingException)ne;
            }
          }
        }
    }
}
