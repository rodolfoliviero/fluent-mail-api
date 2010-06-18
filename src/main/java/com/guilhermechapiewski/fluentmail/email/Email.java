package com.guilhermechapiewski.fluentmail.email;

import java.util.Map;
import java.util.Set;

public interface Email {

	String getFromAddress();
	
	Set<String> getToAddresses();
	
	Set<String> getCcAddresses();
	
	Set<String> getBccAddresses();
	
	String getSubject();
	
	String getBody();
	
	String getCharset();
	
	Map<String, String> getHeaders();
}