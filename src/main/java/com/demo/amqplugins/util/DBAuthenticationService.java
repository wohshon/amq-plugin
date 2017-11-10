package com.demo.amqplugins.util;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.activemq.jaas.GroupPrincipal;
import org.apache.activemq.jaas.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBAuthenticationService implements AuthenticateService {

	Logger log=LoggerFactory.getLogger(getClass());
	public AuthenticationObject authenticate(String username, char[] password) {

		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
	    AuthenticationObject auth=null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		        conn=DriverManager.getConnection("jdbc:mariadb://192.168.223.130:3306/demodb?user=demouser&password=password");
		     	log.info("calling db "+conn);
		        ps=conn.prepareStatement("select u.uname, g.gname from amq_users u, amq_usergroups g where u.uid=g.uid and u.uname=? && u.pword=?");
		        ps.setString(1, username);
		        ps.setString(2, new String(password));
		    rs=ps.executeQuery();
		    auth = new AuthenticationObject();
		    
		    Set<GroupPrincipal> groups=new HashSet<GroupPrincipal>();
		    auth.setGroups(groups);
	    	String group=null;
		    
		    while (rs.next()) {
		    	if (auth.getPrinciple()==null) {
		    		auth.setPrinciple(new UserPrincipal(rs.getString("uname")));
		    		log.info("User principal :"+ auth.getPrinciple().getName());
		    	}
		    	group=rs.getString("gname");
		    	auth.getGroups().add(new GroupPrincipal(group));
	    		log.info("Groups:"+ group);
		    	
		    	
		    }
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (rs!=null)
					rs.close();
					if (ps!=null)
						ps.close();
					if (conn!=null)
						conn.close();					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		
		//org.mariadb.jdbc.Driver
		return auth;
	}

}
