package com.demo.amqplugins.util;

public interface AuthenticateService {
	public AuthenticationObject authenticate(String username, char[] password);
}
