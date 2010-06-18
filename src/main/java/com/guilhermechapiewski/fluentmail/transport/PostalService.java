package com.guilhermechapiewski.fluentmail.transport;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.guilhermechapiewski.fluentmail.email.Email;
import com.sun.mail.smtp.SMTPTransport;

public class PostalService {

	private static EmailTransportConfiguration emailTransportConfig = new EmailTransportConfiguration();
	private static Session session;

	public void send(Email email) throws AddressException, MessagingException, UnsupportedEncodingException {
		Message message = createMessage(email);
		send(message);
	}

	protected Session getSession() {
		if (session == null) {
			Properties properties = System.getProperties();
			properties.put("mail.smtp.host", emailTransportConfig
					.getSmtpServer());
			properties.put("mail.smtp.auth", emailTransportConfig
					.isAuthenticationRequired());

			session = Session.getInstance(properties);
		}

		return session;
	}

	protected MimeMessage createMessage(Email email) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress(email.getFromAddress()));

		for (String to : email.getToAddresses()) {
			message.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(to));
		}

		for (String cc : email.getCcAddresses()) {
			message.setRecipients(Message.RecipientType.CC, InternetAddress
					.parse(cc));
		}

		for (String bcc : email.getBccAddresses()) {
			message.setRecipients(Message.RecipientType.BCC, InternetAddress
					.parse(bcc));
		}
		
		String charset = email.getCharset();
		message.setSubject(email.getSubject(), charset);
		message.setText(email.getBody(), charset);
		message.setSentDate(Calendar.getInstance().getTime());
		
//		message.addHeader("Content-class", "urn:content-classes:calendarmessage");
//		message.setHeader("Content-type", "text/calendar; method=REQUEST; charset=UTF-8");
//		message.addHeader("Content-transfer-encoding", "8BIT");
		
		return message;
	}

	protected void send(Message message) throws NoSuchProviderException,
			MessagingException {
		SMTPTransport smtpTransport = (SMTPTransport) getSession()
				.getTransport(getProtocol());
		if (emailTransportConfig.isAuthenticationRequired()) {
			smtpTransport.connect(emailTransportConfig.getSmtpServer(),
					emailTransportConfig.getUsername(), emailTransportConfig
							.getPassword());
		} else {
			smtpTransport.connect();
		}
		smtpTransport.sendMessage(message, message.getAllRecipients());
		smtpTransport.close();
	}

	protected String getProtocol() {
		String protocol = "smtp";
		if (emailTransportConfig.useSecureSmtp()) {
			protocol = "smtps";
		}
		return protocol;
	}
}
