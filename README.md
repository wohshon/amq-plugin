## AMQ plugin


#### Deploy this into a Standalone activemq

1. build the jar file

  `mvn install`

2. edit `<activemq_home>/conf/activemq.xml`

```
<plugins>
    ...
    <bean id="dbAuthenticationPlugin" class="com.demo.amqplugins.CustomAuthenticationPlugin" xmlns="http://www.springframework.org/schema/beans">
    </bean>
</plugins>
```


3. deploy jar file 

  `cp ./target/amq-0.0.1-SNAPSHOT.jar <activemq_home>/lib`

4. start amq

  `<activemq_home>/bin/activemq start`


#### Deploy this into a AMQ instance on karaf

1. build the jar file

  `mvn install`

2. edit `<amq_home>/etc/activemq.xml`

```
<plugins>
    ...
    <bean id="dbAuthenticationPlugin" class="com.demo.amqplugins.CustomAuthenticationPlugin" xmlns="http://www.springframework.org/schema/beans">
    </bean>
</plugins>
```


3. deploy jar file 

  `install -s wrap:mvn:com.demo/amq/0.0.1-SNAPSHOT`

4. start amq

  `<amq_home>/bin/amq`
  
