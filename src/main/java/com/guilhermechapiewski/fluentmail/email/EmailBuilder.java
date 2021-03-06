package com.guilhermechapiewski.fluentmail.email;


public interface EmailBuilder {

	EmailBuilder from(String address);

	EmailBuilder to(String... addresses);

	EmailBuilder cc(String... addresses);

	EmailBuilder bcc(String... addresses);

	EmailBuilder withSubject(String subject);

	EmailBuilder withBody(String body);
	
	EmailBuilder withCharset(String charset);
	
	EmailBuilder addHeaders(String name, String value);

	void send();
}