package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {
	private Connection connection;
	 private String url;
     private String driverNaam;
     private String userNaam;
     private String wachtWoord;
     
	public ConnectionManager() {
		 Parser parser = new Parser();
		 driverNaam = parser.getDriverName(0);
         url = parser.getUrl(1);
         userNaam = parser.getUsername(1);
         wachtWoord = parser.getPassword(1);
	}

	public Connection getConnection() {
		
		try { // Database downloading
			Class.forName(driverNaam);
			// Connect to a Database
			connection = DriverManager.getConnection(url, userNaam, wachtWoord);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return connection;
	}

}
