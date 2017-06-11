package dao;
import dbmanager.*;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KlantFactory {
	private Connection connection;
	private MongoConnection mc;
	final Logger logger = LoggerFactory.getLogger(KlantFactory.class);
	private static KlantFactory klantfactory = new KlantFactory();

	public KlantFactory() {
	}

	public static KlantInterface Kies(int keus) {
		return klantfactory.KiesDao(keus);
	}

	public KlantInterface KiesDao(int keus) {
		switch (keus) {
		case 1:
			return new KlantDAOMongo();
			
		case 2:
			return new KlantDAOMysql(connection);
			
		default:
			logger.info(" U heeft een onpassende keus ! Daarom ga ik met mysql verbinden ");
			System.out.println();
			return new KlantDAOMysql(connection);
		}
	}

}
