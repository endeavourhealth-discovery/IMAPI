package org.endeavourhealth.imapi.logic.service

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import org.endeavourhealth.imapi.utility.EnvHelper
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.*

class EmailService {
  private val log = LoggerFactory.getLogger(javaClass)
  private val prop: Properties
  private var username: String? = null
  private var password: String? = null

  constructor(host: String, port: Int, username: String, password: String) {
    prop = Properties()
    prop["mail.smtp.auth"] = true
    prop["mail.smtp.starttls.enable"] = "true"
    prop["mail.smtp.host"] = host
    prop["mail.smtp.port"] = port
    prop["mail.smtp.ssl.trust"] = host

    this.username = username
    this.password = password
  }

  constructor(host: String, port: Int) {
    prop = Properties()
    prop["mail.smtp.host"] = host
    prop["mail.smtp.port"] = port
  }

  @Throws(MessagingException::class)
  fun sendMail(subject: String, content: String, to: String) {
    val session = setupSession()
    val message = setupMessage(session, subject, to)
    val mimeBodyPart = MimeBodyPart()
    mimeBodyPart.setContent(content, "text/html; charset=utf-8")
    val multipart: Multipart = MimeMultipart()
    multipart.addBodyPart(mimeBodyPart)
    message.setContent(multipart)
    if (!EnvHelper.isDevMode()) {
      Transport.send(message)
    } else log.info("Email sent")
  }

  @Throws(MessagingException::class, IOException::class)
  fun sendMailWithAttachment(subject: String, content: String, attachment: File, to: String) {
    val session = setupSession()
    val message = setupMessage(session, subject, to)
    val mimeBodyPart = MimeBodyPart()
    mimeBodyPart.setContent(content, "text/html; charset=utf-8")
    val attachmentBodyPart = MimeBodyPart()
    attachmentBodyPart.attachFile(attachment)
    val multipart: Multipart = MimeMultipart()
    multipart.addBodyPart(mimeBodyPart)
    multipart.addBodyPart(attachmentBodyPart)
    message.setContent(multipart)
    if (!EnvHelper.isDevMode()) {
      Transport.send(message)
    } else log.info("Email with attachment sent")
  }

  private fun setupSession(): Session {
    return Session.getInstance(prop, object : Authenticator() {
      override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(username, password)
      }
    })
  }

  @Throws(MessagingException::class)
  private fun setupMessage(session: Session, subject: String, toEmail: String): Message {
    val message: Message = MimeMessage(session)
    message.setFrom(InternetAddress(prop.getProperty("mail.smtp.host")))
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
    message.subject = subject
    return message
  }
}
