package com.demo.amqplugins.login;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.jaas.GroupPrincipal;
import org.apache.activemq.security.AuthorizationMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.amqplugins.util.AuthorizationObject;
import com.demo.amqplugins.util.AuthorizationService;
import com.demo.amqplugins.util.DBAuthorizationService;

public class CustomAuthorizationMap implements AuthorizationMap {

	Logger log=LoggerFactory.getLogger(getClass());
	Map<String, AuthorizationObject> results;
	private AuthorizationService authorizationService=new DBAuthorizationService();
	
	public AuthorizationService getAuthorizationService() {
		return authorizationService;
	}
	
	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	public CustomAuthorizationMap() {
		log.info("*******loaded default custom authorizationmap");
	
		results = this.getAuthorizationService().checkAuthorization();
		Iterable<String> keys=results.keySet();
		for (String key : keys) {
			log.info(results.get(key).toString());			
		}
	}

	public Set<GroupPrincipal> getAdminACLs(ActiveMQDestination dest) {
		log.info("*auth map******admin "+dest);
		results = this.getAuthorizationService().checkAuthorization();

		String n=dest.getPhysicalName();
		if (!dest.getPhysicalName().startsWith("ActiveMQ"))
		{
			if (dest.getPhysicalName().contains(".")) {
				n = dest.getPhysicalName().replaceAll(".", "/");
			} else if (dest.getPhysicalName().contains("/")) {
				n = dest.getPhysicalName().replaceAll("/", ".");
			}
		}else {
			n="ActiveMQ.Advisory.>";
		}
		AuthorizationObject auth=results.get(n);
		Set<GroupPrincipal> gpset= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=null;
		for (String name : auth.getReadACL()) {
			log.info("--"+name);
			gp=new GroupPrincipal(name);
			gpset.add(gp);
		}
		return gpset;

/*		Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=new GroupPrincipal("testrole");
		GroupPrincipal gp1=new GroupPrincipal("admin");
		testSet.add(gp1);
		testSet.add(gp);
		return testSet;		
*/	}

	public Set<GroupPrincipal> getReadACLs(ActiveMQDestination dest) {
		log.info("*******read "+dest.getPhysicalName());
		results = this.getAuthorizationService().checkAuthorization();
		String n=dest.getPhysicalName();
		if (!dest.getPhysicalName().startsWith("ActiveMQ"))
		{
			if (dest.getPhysicalName().contains(".")) {
				n = dest.getPhysicalName().replaceAll(".", "/");
			} else if (dest.getPhysicalName().contains("/")) {
				n = dest.getPhysicalName().replaceAll("/", ".");
			}
		}else {
			n="ActiveMQ.Advisory.>";
		}
		AuthorizationObject auth=results.get(n);
		Set<GroupPrincipal> gpset= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=null;
		for (String name : auth.getReadACL()) {
			log.info("--"+name);
			gp=new GroupPrincipal(name);
			gpset.add(gp);
		}

		return gpset;
/*		Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=new GroupPrincipal("testrole");
		GroupPrincipal gp1=new GroupPrincipal("admin");
		testSet.add(gp1);
		testSet.add(gp);
		return testSet;	*/	
	}

	public Set<GroupPrincipal> getTempDestinationAdminACLs() {
		log.info("*******temp admin");
		Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=new GroupPrincipal("testrole");
		GroupPrincipal gp1=new GroupPrincipal("admin");
		testSet.add(gp1);
		testSet.add(gp);
		return testSet;
	}

	public Set<GroupPrincipal> getTempDestinationReadACLs() {
		log.info("*******temp read");
		Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=new GroupPrincipal("testrole");
		GroupPrincipal gp1=new GroupPrincipal("admin");
		testSet.add(gp1);
		testSet.add(gp);
		return testSet;
	}

	public Set<GroupPrincipal> getTempDestinationWriteACLs() {
		log.info("*******temp write");
		Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=new GroupPrincipal("testrole");
		GroupPrincipal gp1=new GroupPrincipal("admin");
		testSet.add(gp1);
		testSet.add(gp);
		return testSet;
	}

	public Set<GroupPrincipal> getWriteACLs(ActiveMQDestination dest) {
		log.info("*******write "+dest);
		results = this.getAuthorizationService().checkAuthorization();
		String n=dest.getPhysicalName();
		if (!dest.getPhysicalName().startsWith("ActiveMQ"))
		{
			if (dest.getPhysicalName().contains(".")) {
				n = dest.getPhysicalName().replaceAll(".", "/");
			} else if (dest.getPhysicalName().contains("/")) {
				n = dest.getPhysicalName().replaceAll("/", ".");
			}
		} else {
			n="ActiveMQ.Advisory.>";
		}
		AuthorizationObject auth=results.get(n);
		Set<GroupPrincipal> gpset= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=null;
		for (String name : auth.getReadACL()) {
			log.info("--"+name);
			gp=new GroupPrincipal(name);
			gpset.add(gp);
		}
		return gpset;
/*	Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
	GroupPrincipal gp=new GroupPrincipal("testrole");
	GroupPrincipal gp1=new GroupPrincipal("admin");
	testSet.add(gp1);
	testSet.add(gp);
	return testSet;		*/
	}

}
