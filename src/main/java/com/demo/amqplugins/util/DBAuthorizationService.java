package com.demo.amqplugins.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.jaas.GroupPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBAuthorizationService implements AuthorizationService {

	Logger log=LoggerFactory.getLogger(getClass());
	public Map<String, AuthorizationObject> checkAuthorization() {
		AuthorizationObject auth=null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Map<String, AuthorizationObject> results = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		        conn=DriverManager.getConnection("jdbc:mariadb://192.168.223.130:3306/demodb?user=demouser&password=password");
		     	log.info("calling db "+conn);
		     	StringBuilder sb=new StringBuilder("(");
/*		     	int i=1;
		        for (GroupPrincipal groupPrincipal : principals) {
			        	sb.append("?");
			        	if (i++<principals.size()) {
			        		sb.append(",");
			        	}
				}
		        sb.append(")");
		        ps=conn.prepareStatement("select * from amq_authorization where read_grp IN "+sb.toString()+" || write_grp IN "+sb.toString() +" || admin_grp IN "+sb.toString());

*/		        
		     	
/*		        i=1;
		        while (i<principals.size()*3) {
			        for (GroupPrincipal groupPrincipal : principals) {
				        ps.setString(i++, groupPrincipal.getName());					
					}
		        }
*/		        
		        ps=conn.prepareStatement("select * from amq_authorization");
		     	rs=ps.executeQuery();
		    auth = null;
		    results =new HashMap<String,AuthorizationObject>();
		   String destname=null;
		   String desttype=null;
		   String write_grp =null;
		   String read_grp =null;
		   String admin_grp =null;
		    while (rs.next()) {
		    	destname=rs.getString("destname");
		    	desttype=rs.getString("desttype");
		        	log.info(destname);
		        if (results.get(destname)==null) {
		        	auth = new AuthorizationObject();
		        	auth.setDestType(desttype);
		        	auth.setDestname(destname);
		        	results.put(destname, auth);		        	
		        }
		        write_grp = rs.getString("write_grp");
		        read_grp = rs.getString("read_grp");
		        admin_grp = rs.getString("admin_grp");
		        ((AuthorizationObject)results.get(destname)).getAdminACL().add(admin_grp);
		        ((AuthorizationObject)results.get(destname)).getWriteACL().add(write_grp);
		        ((AuthorizationObject)results.get(destname)).getReadACL().add(read_grp);
		        	
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

		
		return results;
	}

}
