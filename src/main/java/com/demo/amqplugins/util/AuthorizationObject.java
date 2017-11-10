package com.demo.amqplugins.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationObject {

	
	Logger log=LoggerFactory.getLogger(getClass());
	private String destname;
	private String destType;
	private Set<String> readACL=new HashSet<String>();
	private Set<String> writeACL=new HashSet<String>();;
	private Set<String> adminACL=new HashSet<String>();;
	
	public String getDestType() {
		return destType;
	}
	public void setDestType(String destType) {
		this.destType = destType;
	}
	public String getDestname() {
		return destname;
	}
	public void setDestname(String destname) {
		this.destname = destname;
	}
	public Set<String> getReadACL() {
		return readACL;
	}
	public void setReadACL(Set<String> readACL) {
		this.readACL = readACL;
	}
	public Set<String> getWriteACL() {
		return writeACL;
	}
	public void setWriteACL(Set<String> writeACL) {
		this.writeACL = writeACL;
	}
	public Set<String> getAdminACL() {
		return adminACL;
	}
	public void setAdminACL(Set<String> adminACL) {
		this.adminACL = adminACL;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(getDestname())
		.append(" - admin "+getAdminACL().size())
		.append(" - read "+getReadACL().size())
		.append(" - write "+getWriteACL().size());
		return sb.toString();
	}
	
}
