package com.demo.amqplugins;

import java.security.Principal;
import java.util.Iterator;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.region.Subscription;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBrokerFilter extends BrokerFilter {

	Logger logger=LoggerFactory.getLogger(getClass());
	public CustomBrokerFilter(Broker next) {
		super(next);
		logger.info("******Constructor invoked in broker!!!: "+getBrokerName());
		
	}

	@Override
	public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
		logger.info("Inside addConnection , info: "+info);
		logger.info("Inside addConnection , Context: "+context);
		super.addConnection(context, info);
	}
	
	@Override
	public Subscription addConsumer(ConnectionContext context, ConsumerInfo info) throws Exception {
		logger.info("Inside addConsumer , info: "+info);
		logger.info("TOPIC "+info.getDestination().toString());
		logger.info("User  "+context.getUserName());
		logger.info("Context  "+context.getSecurityContext());
		logger.info("Principals  "+context.getSecurityContext().getPrincipals());
		logger.info("Write Destinations  "+context.getSecurityContext().getAuthorizedWriteDests());
		Iterator<Principal> itr=context.getSecurityContext().getPrincipals().iterator();
		while (itr.hasNext()) {
			String name=itr.next().getName();
			if (!name.equals(context.getUserName())) {
				logger.info("ROLES: "+name);
			}
		}
		return super.addConsumer(context, info);
	}
	
	private boolean authenticate(String username, String password, String topic) {
		return true;
	}
	
}
