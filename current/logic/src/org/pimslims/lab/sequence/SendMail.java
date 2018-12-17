package org.pimslims.lab.sequence;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.pimslims.lab.Util;

import com.sun.mail.smtp.SMTPTransport;

/**
 * SendMail sends a very simple text/plain and text/html message.
 * 
 * @author Petr Troshin aka pvt43
 */
public class SendMail {

    public static final String smtp_server = "smtp.server";

    public static final String smtp_user = "smtp.user";

    public static final String smtp_password = "smtp.password";

    public static final String smtp_secure = "smtp.secure";

    public static final String smtp_debug = "smtp.debug";

    public static final String smtp_replyTo = "smtp.reply.to";

    final Session session;

    String user;

    String password;

    boolean isSecure;

    Properties props;

    public static final String replyTo = "reply.to.message";

    /**
     * Constructor for SendMail
     */
    public SendMail(final HashMap<String, String> properties) {
        // create some properties and get the default Session

        assert properties != null && !Util.isEmpty(properties.get(SendMail.smtp_server));
        this.props = new Properties();
        this.props.put("mail.smtp.host", properties.get(SendMail.smtp_server).trim());

        this.isSecure = false;
        final String secure = properties.get(SendMail.smtp_secure);
        if (secure != null && secure.trim().equalsIgnoreCase("true")) {
            this.props.put("mail.smtps.auth", "true");
            this.isSecure = true;
        }

        final String debug = properties.get(SendMail.smtp_debug);
        if (debug != null && debug.trim().equalsIgnoreCase("true")) {
            this.props.put("mail.debug", true);
        }

        final String username = properties.get(SendMail.smtp_user);
        if (!Util.isEmpty(username)) {
            this.user = username.trim();
            final String passwordVal = properties.get(SendMail.smtp_password);
            if (!Util.isEmpty(passwordVal)) {
                this.password = passwordVal.trim();
            }
        }

        this.session = Session.getInstance(this.props, null);
    }

    private boolean send(final Message msg, final String subject, final String message, final boolean ishtml)
        throws MessagingException, IOException {
        final boolean success = true;
        assert !Util.isEmpty(message);

        if (!Util.isEmpty(subject)) {
            msg.setSubject(subject);
        }
        msg.setSentDate(new Date());
        if (!ishtml) {
            msg.setText(message);
        } else {
            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(message, "text/html")));
        }
        //Transport.send(msg);

        SMTPTransport t = null;

        try {
            if (this.isSecure) {
                t = (SMTPTransport) this.session.getTransport("smtps");
            } else {
                t = (SMTPTransport) this.session.getTransport("smtp");
            }

            if (!Util.isEmpty(this.user)) {
                t.connect(this.props.getProperty("mail.smtp.host"), this.user, this.password);
                t.sendMessage(msg, msg.getAllRecipients());
            } else {
                t.connect();
                t.sendMessage(msg, msg.getAllRecipients());
            }
        } finally {
            System.out.println("Response: " + t.getLastServerResponse());
            t.close();
        }

        return success;
    }

    public boolean sendMessage(final String to, final String from, final String subject,
        final String message, final boolean ishtml) throws MessagingException, IOException {
        assert !Util.isEmpty(to) && !Util.isEmpty(from);
        return this.sendMessage(new String[] { to }, null, from, subject, message, ishtml);
    }

    public boolean sendMessage(final String to, final String from, final String subject, final String message)
        throws MessagingException, IOException {
        assert !Util.isEmpty(to) && !Util.isEmpty(from);
        return this.sendMessage(new String[] { to }, null, from, subject, message, false);
    }

    public boolean sendMessage(final String to, final String cc, final String from, final String subject,
        final String message, final boolean ishtml) throws MessagingException, IOException {
        assert !Util.isEmpty(to) && !Util.isEmpty(from);
        return this.sendMessage(new String[] { to }, new String[] { cc }, from, subject, message, ishtml);
    }

    private InternetAddress[] convert(final String[] addresses) throws AddressException {
        assert addresses != null;
        final InternetAddress[] iaddresses = new InternetAddress[addresses.length];

        for (int i = 0; i < addresses.length; i++) {
            iaddresses[i] = new InternetAddress(addresses[i]);
        }
        return iaddresses;
    }

    public boolean sendMessage(final String[] to, final String[] cc, final String from, final String subject,
        final String message, final boolean ishtml) throws MessagingException, IOException {

        assert to != null && from != null;
        final Message msg = new MimeMessage(this.session);

        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, this.convert(to));
        if (cc != null && cc.length > 0) {
            msg.addRecipients(javax.mail.Message.RecipientType.CC, this.convert(cc));
        }
        return this.send(msg, subject, message, ishtml);
    }
}
