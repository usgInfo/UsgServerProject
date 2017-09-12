/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.common.manager;

import static com.accure.usg.server.utils.Common.getConfig;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Deepak
 */
public class EmailManager {

    private Properties props = null;
    private String strEmailSender = null;
    private String strEmailSenderPassword = null;
    String host = "";
//    PropertiesConfiguration config = new PropertiesConfiguration("conf.properties");

    public EmailManager() throws Exception {
        host = (String) getConfig().getProperty("mail-smtp-host");
        props = new Properties();
        props.put("mail.smtp.auth", (String) getConfig().getProperty("mail-smtp-auth"));
        props.put("mail.smtp.starttls.enable", (String) getConfig().getProperty("mail-smtp-starttls-enable"));
        props.put("mail.smtp.host", (String) getConfig().getProperty("mail-smtp-host"));
        props.put("mail.smtp.port", (String) getConfig().getProperty("mail-smtp-port"));
        strEmailSender = (String) getConfig().getProperty("Email-Sender");
        strEmailSenderPassword = (String) getConfig().getProperty("EmailSender-Password");
        props.put("mail.smtp.user", strEmailSender);
        props.put("mail.smtp.password", strEmailSenderPassword);
    }

    public void sendEmail(String to, String subject, String msg) throws Exception {
        Session emailSession = Session.getDefaultInstance(props, null);
        Message message = new MimeMessage(emailSession);
        message.setFrom(new InternetAddress(strEmailSender));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(msg, "text/html");
        Transport transport = emailSession.getTransport("smtp");
        transport.connect(host, strEmailSender, strEmailSenderPassword);
        transport.sendMessage(message, message.getAllRecipients());
        //System.out.println("success " + to);
        transport.close();
    }

//    public static PropertiesConfiguration getConfig() {
//        PropertiesConfiguration config = null;
//        try {
//            config = new PropertiesConfiguration("conf.properties");
//        } catch (ConfigurationException ex) {
//            //System.out.println(ex);
//        }
//        return config;
//    }
    public static void main(String args[]) throws Exception {
        new EmailManager().sendEmail("deepak.pathak2310@gmail.com", "", "");
    }

}
