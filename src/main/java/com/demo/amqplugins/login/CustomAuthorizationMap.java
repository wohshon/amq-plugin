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

		String n=null;
		if (dest.getDestinationTypeAsString().equals("Topic")) {
			n=this.mqttCleanup(dest);
		}
		else {
			n=dest.getPhysicalName();
		}
		AuthorizationObject auth=results.get(n);
		Set<GroupPrincipal> gpset= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=null;
		for (String name : auth.getReadACL()) {
			log.info("--"+name);
			gp=new GroupPrincipal(name);
			gpset.add(gp);
		}
		this.addWildcard(gpset, ADMIN_TYPE);

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
		String n=null;
		if (dest.getDestinationTypeAsString().equals("Topic")) {
			n=this.mqttCleanup(dest);
		}
		else {
			n=dest.getPhysicalName();
		}
		AuthorizationObject auth=results.get(n);
		Set<GroupPrincipal> gpset= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=null;
		for (String name : auth.getReadACL()) {
			log.info("--"+name);
			gp=new GroupPrincipal(name);
			gpset.add(gp);
		}
		this.addWildcard(gpset, READ_TYPE);

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
		String n=null;
		if (dest.getDestinationTypeAsString().equals("Topic")) {
			n=this.mqttCleanup(dest);
		}
		else {
			n=dest.getPhysicalName();
		}
		AuthorizationObject auth=results.get(n);
		Set<GroupPrincipal> gpset= new HashSet<GroupPrincipal>();
		GroupPrincipal gp=null;
		for (String name : auth.getReadACL()) {
			log.info("--"+name);
			gp=new GroupPrincipal(name);
			gpset.add(gp);
		}
		this.addWildcard(gpset, WRITE_TYPE);
		return gpset;
/*	Set<GroupPrincipal> testSet= new HashSet<GroupPrincipal>();
	GroupPrincipal gp=new GroupPrincipal("testrole");
	GroupPrincipal gp1=new GroupPrincipal("admin");
	testSet.add(gp1);
	testSet.add(gp);
	return testSet;		*/
	}

	private String mqttCleanup(ActiveMQDestination dest) {
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
		return n;
	}
	
	private static final int  ADMIN_TYPE=0;
	private static final int  READ_TYPE=1;
	private static final int  WRITE_TYPE=2;
	private void addWildcard(Set<GroupPrincipal> gpset, int type) {
		String[] wc={"*",">"};
		for (int i=0;i<wc.length;i++) {
			AuthorizationObject ao=this.results.get(wc[i]);
			if (ao!=null) {
           		GroupPrincipal gp=null;
           		switch (type) {
		                   case ADMIN_TYPE : 

		            		for (String name : ao.getReadACL()) {
		            			log.info("--"+name);
		            			gp=new GroupPrincipal(name);
		            			gpset.add(gp);
		            		};
		                   case WRITE_TYPE : 

		                	   for (String name : ao.getWriteACL()) {
		            			log.info("--"+name);
		            			gp=new GroupPrincipal(name);
		            			gpset.add(gp);
		            		};		                        
		            		
		                   case READ_TYPE : 

		                	   for (String name : ao.getReadACL()) {
		            			log.info("--"+name);
		            			gp=new GroupPrincipal(name);
		            			gpset.add(gp);
		            		}		                        
		        		             
		        }//switch				
				
			}

			
		}
	}
}
