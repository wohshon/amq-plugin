package com.demo.amqplugins.login;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.activemq.jaas.GroupPrincipal;
import org.apache.activemq.jaas.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.amqplugins.util.AuthenticateService;
import com.demo.amqplugins.util.AuthenticationObject;

public class CustomLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
    private String user;
    private Set<Principal> principals;
    private AuthenticationObject auth;
    
	Logger log=LoggerFactory.getLogger(this.getClass());
	
	private AuthenticateService authenticateService=null;
	
	
	public AuthenticateService getAuthenticateService() {
		return authenticateService;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		this.subject=subject;
		this.callbackHandler=callbackHandler;
		log.info("**********init "+options.get("authenticationService"));
		try {
			authenticateService=(AuthenticateService)Class.forName((String)options.get("authenticationService")).newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("**********Authentication Service "+authenticateService);

	}

	public boolean login() throws LoginException {
		log.info("**********Login");
        Callback callbacks[] = new Callback[2];
        callbacks[0] = new NameCallback("Username: ");
        callbacks[1] = new PasswordCallback("Password: ", false);
        try {
			callbackHandler.handle(callbacks);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        user=((NameCallback)callbacks[0]).getName();
        log.info("User: "+user);
        char[] password = ((PasswordCallback) callbacks[1]).getPassword();
        if (password == null)
            password = new char[0];
        auth=authenticate(user, password) ;       
        if (auth!=null)	
		return true;
        else return false;
	}
	
	private AuthenticationObject authenticate(String user, char[] password) {
		log.info("******authenticate******"+new String(password));
		log.info("******calling db");

		return this.authenticateService.authenticate(user, password);
	}

	public boolean commit() throws LoginException {
		log.info("************commit");
		 Set<Principal> principals = subject.getPrincipals();
		principals.add(auth.getPrinciple());
		
		Set<GroupPrincipal> authGroups=auth.getGroups();
		for (GroupPrincipal groupPrincipal : authGroups) {
			principals.add(groupPrincipal);
		}
        		
		return true;
	}

	public boolean abort() throws LoginException {
		log.info("abort");
		return true;
	}

	public boolean logout() throws LoginException {
		log.info("logout");
		return true;
	}

}
