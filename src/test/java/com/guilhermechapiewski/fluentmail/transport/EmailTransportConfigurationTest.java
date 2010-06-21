package com.guilhermechapiewski.fluentmail.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests to {@link EmailTransportConfiguration}.
 */
public class EmailTransportConfigurationTest {
	
	private EmailTransportConfiguration config = new EmailTransportConfiguration();

	@Test
	public void should_manage_configuration_correctly() {
		String smtpServer = "smtp.server.com";
		String username = "john";
		String password = "doe";

		assertEquals("Should configure smtp server correctly", smtpServer, config.getSmtpServer());
		assertEquals("Should configure username correctly", username, config.getUsername());
		assertEquals("Should configure password correctly", password, config.getPassword());
		
		assertTrue("Should configure authentication correctly", config.isAuthenticationRequired());
		assertFalse("Should configure secure smtp correctly", config.useSecureSmtp());
	}
}