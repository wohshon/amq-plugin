package com.demo.amqplugins.util;

import java.util.Map;
import java.util.Set;

import org.apache.activemq.jaas.GroupPrincipal;

public interface AuthorizationService {
	
	public Map<String, AuthorizationObject>  checkAuthorization();
}
