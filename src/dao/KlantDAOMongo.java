package dao;

import java.util.List;
import java.util.ArrayList;
import dbmanager.MongoConnection;
import exception.updateException;
import model.Klant;
import model.KlantAdres;
import model.KlantAdres.KlantAdresBuilder;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.combine;

public class KlantDAOMongo implements KlantInterface {
	private Klant klant;
	private MongoConnection mc = new MongoConnection();
	
	public MongoCollection<Document> createCollection(){
		
		return mc.getConnection().getCollection("klant");
	}
    


	@Override
	public void insertKlant(Klant klant) {
		
		try{
			createCollection().insertOne(createDocument(klant));
			System.out.println("Het teovoeging van de klant is geslaagd");
		}catch (Exception ex) {
			throw new updateException("Het teovoeging van de klant is gezakt");

		}
			

		
	}

	@Override
	public void deleteKlant(String achternaam) {
		try {
			DeleteResult del = createCollection().deleteOne(eq("achternaam", achternaam));
			System.out.println(" Het wissen van deze klant is geslagd ");
		} catch (Exception ex) {
			throw new updateException("Het wissen van de klant is gezakt");
		}
	}

	@Override
	public boolean wezig(String achternaam) {
		boolean isWezig;
		try {
			if (createCollection().find(eq("achternaam", achternaam)) != null)
				isWezig = true;
			else
				isWezig = true;
			System.out.println(" Het wissen van deze klant is geslagd ");
		} catch (Exception ex) {
			throw new updateException(" Er is een probleem met database ");
		}

		return isWezig;
	}

	@Override
	public void updateKlantnaam(String achternaam, Klant klant) {
		try {
			Document dUpdate = createCollection().findOneAndUpdate(eq("achternaam", achternaam),
					combine(set("voornaam", klant.getVoornaam()), set("achternaam", klant.getAchternaam()),
							set("tussenvoegsel", klant.getTussenvoegsel())));
		} catch (Exception ex) {
			throw new updateException(" Het aanpassen van niew klant is gezakt ");
		}
	}

	@Override
	public void updateKlantAdres(String achternaam, Klant klant) {
		try {
			Document dUpdate = createCollection().findOneAndUpdate(eq("achternaam", achternaam),
					combine(set("straatnaam", klant.getKlantAdres().getStraatnaam()),
							set("huisnummer", klant.getKlantAdres().getHuisnummer()),
							set("toevoeging", klant.getKlantAdres().getToevoeging()),
							set("postcode", klant.getKlantAdres().getPostocde()),
							set("woonplaats", klant.getKlantAdres().getWoonplaats()),
							set("adrestype", klant.getKlantAdres().getAdrestype())));
		} catch (Exception ex) {
			throw new updateException(" Het aanpassen van niew klant is gezakt ");
		}
	}

	@Override
	public List<Klant> getKlanten() {
		List<Klant> klanten = new ArrayList<>();
		try {
			createCollection().find().forEach((Block<Document>) document -> klanten.add(createKlant(document)));
		}

		catch (Exception ex) {
			throw new updateException("Er is een probleem met klant tabel ");
		}
		return klanten;
	}

	@Override
	public String KlantString(Klant klant) {
		String show;
		try {
			Document document = (Document) createCollection().find(eq("id", klant.getId()));
			if (document != null) {
				klant = createKlant(document);
				int id = klant.getId();
				String naam = klant.getVoornaam();
				String achternaam = klant.getAchternaam();
				String straat = klant.getKlantAdres().getStraatnaam();
				String huis = klant.getKlantAdres().getHuisnummer();
				String postcode = klant.getKlantAdres().getPostocde();
				String plaats = klant.getKlantAdres().getWoonplaats();
				String type;
				if (klant.getKlantAdres().getAdrestype() == 1)
					type = "Huis";
				else if (klant.getKlantAdres().getAdrestype() == 2)
					type = "werk";
				else
					type = "other";
				show = id + "\t" + naam + "\t\t" + achternaam + "\t" + straat + "\t\t" + huis + "\t" + postcode + "\t\t"
						+ plaats + "\t\t" + type;
			} else
				show = "De naam is afwijzig";
		}

		catch (Exception ex) {
			throw new updateException(" Er is een probleem in de database ");
		}

		return show;
	}

	public Document createDocument(Klant klant) {
		Document document = new Document();
		if (klant.getId() == 0)
			klant.setId(1);
		else
			document.append("id", klant.getId());

		document.append("voornaam", klant.getVoornaam());
		document.append("achternaam", klant.getAchternaam());
		document.append("tussenvoegsel", klant.getTussenvoegsel());
		document.append("straatnaam", klant.getKlantAdres().getStraatnaam());
		document.append("huisnummer", klant.getKlantAdres().getHuisnummer());
		document.append("toevoeging", klant.getKlantAdres().getToevoeging());
		document.append("postcode", klant.getKlantAdres().getPostocde());
		document.append("woonplaats", klant.getKlantAdres().getWoonplaats());
		document.append("adrestype", klant.getKlantAdres().getAdrestype());

		return document;
	}

	public Klant createKlant(Document document) {
		KlantAdresBuilder klantbuilder = new KlantAdresBuilder();
		klant.setId(document.getInteger("_id"));
		klant.setVoornaam(document.getString("voornaam"));
		klant.setAchternaam(document.getString("achternaam"));
		klant.setTussenvoegsel(document.getString("tussenvoegsel"));
		klantbuilder.straatNaam(document.getString("straatnaam"));
		klantbuilder.huisNummer(document.getString("huisnummer"));
		klantbuilder.toevoeging(document.getString("toevoeging"));
		klantbuilder.postCode(document.getString("postcode"));
		klantbuilder.woonplaats(document.getString("woonplaats"));
		klantbuilder.adresType(document.getInteger("adrestype"));
		klant.setKlantAdres(new KlantAdres(klantbuilder));
		return klant;
	}
	

}
