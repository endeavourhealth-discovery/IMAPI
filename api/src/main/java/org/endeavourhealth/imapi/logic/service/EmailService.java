package org.endeavourhealth.imapi.logic.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class EmailService {
  private final Properties prop;
  private String username;
  private String password;

  public EmailService(String host, int port, String username, String password) {
    prop = new Properties();
    prop.put("mail.smtp.auth", true);
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.host", host);
    prop.put("mail.smtp.post", port);
    prop.put("mail.smtp.ssl.trust", host);

    this.username = username;
    this.password = password;
  }

  public EmailService(String host, int port) {
    prop = new Properties();
    prop.put("mail.smtp.host", host);
    prop.put("mail.smtp.port", port);
  }

  public void sendMail(String subject, String content) throws MessagingException {
    Session session = setupSession();
    Message message = setupMessage(session, subject);
    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent(content, "text/html; charset=utf-8");
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(mimeBodyPart);
    message.setContent(multipart);
    if ("production".equals(System.getenv("MODE"))) Transport.send(message);
    else log.info("Email sent");
  }

  public void sendMailWithAttachment(String subject, String content, File attachment) throws MessagingException, IOException {
    Session session = setupSession();
    Message message = setupMessage(session, subject);
    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent(content, "text/html; charset=utf-8");
    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
    attachmentBodyPart.attachFile(attachment);
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(mimeBodyPart);
    multipart.addBodyPart(attachmentBodyPart);
    message.setContent(multipart);
    if ("production".equals(System.getenv("MODE"))) Transport.send(message);
    else log.info("Email with attachment sent");
  }

  private Session setupSession() {
    return Session.getInstance(prop, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
  }

  private Message setupMessage(Session session, String subject) throws MessagingException {
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress("imapi@endeavourhealth.org"));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("support@endeavourhealth.org"));
    message.setSubject(subject);
    return message;
  }

}
