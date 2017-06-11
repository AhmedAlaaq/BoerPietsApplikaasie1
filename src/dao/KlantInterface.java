package dao;
import model.Klant;
import java.util.List;

public interface KlantInterface {
	void insertKlant (Klant klant );
	void deleteKlant (String achternaam);
	boolean wezig(String achternaam);
	void updateKlantnaam(String achternaam, Klant klant);
	void updateKlantAdres(String achtrenaam, Klant klant);
	List<Klant> getKlanten();
	String KlantString(Klant klant);

}
