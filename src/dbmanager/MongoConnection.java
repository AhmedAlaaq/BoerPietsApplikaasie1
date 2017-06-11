package dbmanager;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import view.AccountMenu;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoConnection {
	 
	     private MongoDatabase database;
	     private String url;
	     private String port;
	private String databaseNaam;
	     private String username;
	     private String wachtwoord;
	     final Logger logger = LoggerFactory.getLogger(MongoConnection.class);
	 	     public MongoConnection() {
	         Parser parser = new Parser();
	         databaseNaam = parser.getDatabase(0);
	         url = parser.getUrl(0);
	         port = parser.getPort(0);
	         username = parser.getUsername(0);
	         wachtwoord = parser.getPassword(0);
	     }
	 
	     public void createConnection() {
	         MongoCredential credential = MongoCredential.createCredential(username, databaseNaam, wachtwoord.toCharArray());
	         MongoClient mongoClient = new MongoClient(new ServerAddress(url, Integer.parseInt(port)), Arrays.asList(credential));
	         database = mongoClient.getDatabase(databaseNaam);
	         logger.info(" Database is connected successfully");
	     }
	 
	     public MongoDatabase getConnection() {
	         if(database == null){
	             createConnection();
	         }
	         return database;
	     }
	 }

