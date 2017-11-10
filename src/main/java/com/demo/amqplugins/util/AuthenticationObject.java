package com.demo.amqplugins.util;

import java.util.Set;

import org.apache.activemq.jaas.GroupPrincipal;
import org.apache.activemq.jaas.UserPrincipal;

public class AuthenticationObject {
	private UserPrincipal principle;
	private Set<GroupPrincipal> groups;

	public Set<GroupPrincipal> getGroups() {
		return groups;
	}
	public void setGroups(Set<GroupPrincipal> groups) {
		this.groups = groups;
	}

	public UserPrincipal getPrinciple() {
		return principle;
	}
	public void setPrinciple(UserPrincipal principle) {
		this.principle = principle;
	}
	
	
}
