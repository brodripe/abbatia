package org.abbatia.poc1_gae_abbatia1.server;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.mysql.jdbc.Driver;

import java.sql.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.abbatia.poc1_gae_abbatia1.model.MessageSQL;

public class MessageRepositorySQL {
	
	public MessageRepositorySQL() {
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			//DriverManager.registerDriver(new Driver());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<MessageSQL> getAll() {
		Connection c = null;
		List<MessageSQL> messages = null;
		
		System.out.println(System.getProperty("java.class.path"));
		
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://abbatia.net:abbatia-test-project:poc1-abbatia/guestbook");
			
			messages = new ArrayList<MessageSQL>();
			
			ResultSet rs = c.createStatement().executeQuery("SELECT content, entryID FROM entries");
			
			while (rs.next()){
				MessageSQL message = new MessageSQL();
				
			    message.setText(rs.getString("content"));
			    message.setId((long) rs.getInt("entryID"));
		 
			    messages.add(message);
			}		    
			
		} catch (SQLException e) {
	        e.printStackTrace();
	      } finally {
	          if (c != null) 
	            try {
	              c.close();
	            } catch (SQLException ignore) {
	              }
	      }
	      
	      return messages;
	}

	public void create(MessageSQL message) {
		
		Connection c = null;
				
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://abbatia.net:abbatia-test-project:poc1-abbatia/guestbook");
			
			String statement ="INSERT INTO entries (guestName, content) VALUES( ? , ? )";
		      PreparedStatement stmt = c.prepareStatement(statement);
		      stmt.setString(1, "Jordi");
		      stmt.setString(2, message.getText());
		      stmt.executeUpdate();
					    
			
		} catch (SQLException e) {
	        e.printStackTrace();
	      } finally {
	          if (c != null) 
	            try {
	              c.close();
	            } catch (SQLException ignore) {
	              }
	      }
	}

	public void deleteById(Long id) {
		
		Connection c = null;
		
		try {
			c = DriverManager.getConnection("jdbc:google:rdbms://abbatia.net:abbatia-test-project:poc1-abbatia/guestbook");
			
			String statement ="DELETE FROM entries WHERE entryID = ?";
		      PreparedStatement stmt = c.prepareStatement(statement);
		      stmt.setInt(1, (int) id.longValue());
		      stmt.executeUpdate();					    
			
		} catch (SQLException e) {
	        e.printStackTrace();
	      } finally {
	          if (c != null) 
	            try {
	              c.close();
	            } catch (SQLException ignore) {
	              }
	      }
	}
	
}
