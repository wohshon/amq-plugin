package com.demo.amqplugins;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

public class CustomAuthenticationPlugin implements BrokerPlugin {

	public Broker installPlugin(Broker broker) throws Exception {
		return new CustomAuthenticationBroker(broker);
	}

}
