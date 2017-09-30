package com.demo.amqplugins;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.region.Subscription;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAuthenticationBroker extends BrokerFilter {

	Logger logger=LoggerFactory.getLogger(getClass());
	public CustomAuthenticationBroker(Broker next) {
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
		//logger.info("User  "+context.getSecurityContext().getAuthorizedWriteDests());
		logger.info("Contextr  "+context.getSecurityContext());
		return super.addConsumer(context, info);
	}
	
	private boolean authenticate(String username, String password, String topic) {
		return true;
	}
	
}
