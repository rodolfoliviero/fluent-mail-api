package com.guilhermechapiewski.fluentmail.email;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.guilhermechapiewski.fluentmail.transport.EmailTransportException;
import com.guilhermechapiewski.fluentmail.transport.PostalService;
import com.guilhermechapiewski.fluentmail.validation.EmailAddressValidator;
import com.guilhermechapiewski.fluentmail.validation.IncompleteEmailException;
import com.guilhermechapiewski.fluentmail.validation.InvalidEmailAddressException;

public class EmailMessage implements EmailBuilder, Email {

	private static EmailAddressValidator emailAddressValidator = new EmailAddressValidator();
	private static PostalService postalService = new PostalService();

	private String fromAddress;
	private Set<String> toAddresses = new HashSet<String>();
	private Set<String> ccAddresses = new HashSet<String>();
	private Set<String> bccAddresses = new HashSet<String>();
	private String subject;
	private String body;
	private String charset = Charset.defaultCharset().name();
	private Map<String, String> headers = new HashMap<String, String>();
	
	public void send() {
		validateRequiredInfo();
		validateAddresses();
		sendMessage();
	}

	protected void validateRequiredInfo() {
		if (fromAddress == null) {
			throw new IncompleteEmailException("From address cannot be null");
		}
		if (toAddresses.isEmpty()) {
			throw new IncompleteEmailException(
					"Email should have at least one to address");
		}
		if (subject == null) {
			throw new IncompleteEmailException("Subject cannot be null");
		}
		if (body == null) {
			throw new IncompleteEmailException("Body cannot be null");
		}
	}

	protected void sendMessage() {
		try {
			postalService.send(this);
		} catch (Exception e) {
			throw new EmailTransportException("Email could not be sent: "
					+ e.getMessage(), e);
		}
	}

	public EmailBuilder from(String address) {
		this.fromAddress = address;
		return this;
	}

	public EmailBuilder to(String... addresses) {
		for (int i = 0; i < addresses.length; i++) {
			this.toAddresses.add(addresses[i]);
		}
		return this;
	}

	public EmailBuilder cc(String... addresses) {
		for (int i = 0; i < addresses.length; i++) {
			this.ccAddresses.add(addresses[i]);
		}
		return this;
	}
	
	public EmailBuilder withCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public EmailBuilder bcc(String... addresses) {
		for (int i = 0; i < addresses.length; i++) {
			this.bccAddresses.add(addresses[i]);
		}
		return this;
	}

	public EmailBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public EmailBuilder withBody(String body) {
		this.body = body;
		return this;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public Set<String> getToAddresses() {
		return toAddresses;
	}

	public Set<String> getCcAddresses() {
		return ccAddresses;
	}

	public Set<String> getBccAddresses() {
		return bccAddresses;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	protected EmailBuilder validateAddresses() {
		if (!emailAddressValidator.validate(fromAddress)) {
			throw new InvalidEmailAddressException("From: " + fromAddress);
		}

		for (String email : toAddresses) {
			if (!emailAddressValidator.validate(email)) {
				throw new InvalidEmailAddressException("To: " + email);
			}
		}

		return this;
	}

	public static void setEmailAddressValidator(
			EmailAddressValidator emailAddressValidator) {
		EmailMessage.emailAddressValidator = emailAddressValidator;
	}

	public static void setPostalService(PostalService postalService) {
		EmailMessage.postalService = postalService;
	}

	public String getCharset() {
		return charset;
	}

	public EmailBuilder addHeaders(String name, String value) {
		headers.put(name, value);
		return this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
}