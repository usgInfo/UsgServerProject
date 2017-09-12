/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.payroll.dto.SalarySlipRegisterReport;
import static com.accure.payroll.manager.SalarySlipRportPDFGeneration.getMonthString;
import java.util.Properties;

import static com.accure.usg.server.utils.Common.getConfig;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class SendEmail {

    Properties props = null;
    String emailSenderUsername = null;
    String emailSenderPassword = null;
    String host = null;

    public boolean send(ByteArrayOutputStream bos, List<SalarySlipRegisterReport> list) throws Exception {
        boolean result = false;
        try {

            props = new Properties();
            emailSenderUsername = (String) getConfig().getProperty("Email-Sender");
            emailSenderPassword = (String) getConfig().getProperty("EmailSender-Password");
            //System.out.println("emailSenderUsername" + emailSenderUsername);

            //System.out.println("emailSenderPassword" + emailSenderPassword);

            host = (String) getConfig().getProperty("mail-smtp-host");
            props.put("mail.smtp.user", emailSenderUsername);
            props.put("mail.smtp.password", emailSenderPassword);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.starttls.enable", (String) getConfig().getProperty("mail-smtp-starttls-enable"));
            props.put("mail.smtp.auth", (String) getConfig().getProperty("mail-smtp-auth"));
            props.put("mail.smtp.port", (String) getConfig().getProperty("mail-smtp-port"));
            //System.out.println("props" + props);

            for (SalarySlipRegisterReport salarylist : list) {
                //System.out.println("salarylist.getEmail()" + salarylist.getEmail());
                Session emailSession = Session.getDefaultInstance(props);
                Message message = new MimeMessage(emailSession);
                message.setFrom(new InternetAddress(emailSenderUsername));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(salarylist.getEmail()));
                String monthString = getMonthString(salarylist.getMonth());
                message.setSubject("Salary Slip Month of " + monthString + "-" + salarylist.getYear());
                BodyPart messageBodyPart1 = new MimeBodyPart();
                messageBodyPart1.setText("Hi   " + salarylist.getEmployeeName() + ",\n      \n" + "Please find herewith the enclosed pay slip for the month of  " + monthString + "-" + salarylist.getYear());
                MimeBodyPart messageBodyPart2 = new MimeBodyPart();

                byte[] bytes = bos.toByteArray();

                DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
                messageBodyPart2.setDataHandler(new DataHandler(dataSource));
                messageBodyPart2.setFileName(salarylist.getEmployeeName() + ".pdf");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart1);
                multipart.addBodyPart(messageBodyPart2);

                message.setContent(multipart);
                Transport transport = emailSession.getTransport("smtp");
                transport.connect(host, emailSenderUsername, emailSenderPassword);
                transport.sendMessage(message, message.getAllRecipients());
                 result = true;
                 //System.out.println("sent");
                transport.close();
               
            }

        } catch (Exception e) {
            result = false;

        }
        return result;
    }
}
