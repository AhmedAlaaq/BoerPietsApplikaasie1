package dao;

import exception.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dbmanager.ConnectionManager;
import model.Account;
import model.Artikel;
import model.Klant;
import model.KlantAdres;
import model.KlantAdres.KlantAdresBuilder;

public class KlantDAOMysql implements KlantInterface {

	private ConnectionManager mn;
	private Connection connection;
	private Klant klant;

	public KlantDAOMysql(Connection connection) {
		mn = new ConnectionManager();
		connection = mn.getConnection();

	}

	@Override
	public void insertKlant(Klant klant) {

		String str = " insert into Klant (voornaam, achternaam, tussenvoegsel, "
				+ "straatnaam, huisnummer, toevoeging, postcode, " + "woonplaats, adrestype) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);) {
			statement.setString(1, klant.getVoornaam());
			statement.setString(2, klant.getAchternaam());
			statement.setString(3, klant.getTussenvoegsel());
			statement.setObject(4, klant.getKlantAdres().getStraatnaam());
			statement.setObject(5, klant.getKlantAdres().getHuisnummer());
			statement.setObject(6, klant.getKlantAdres().getToevoeging());
			statement.setObject(7, klant.getKlantAdres().getPostocde());
			statement.setObject(8, klant.getKlantAdres().getWoonplaats());
			statement.setInt(9, klant.getKlantAdres().getAdrestype());
			statement.executeUpdate();
			ResultSet res = statement.getGeneratedKeys();
			if (res.isBeforeFirst()) {
				res.next();
				klant.setId(res.getInt(1));
				System.out.println("Het teovoeging van de klant is geslaagd");
			}
		} catch (Exception ex) {
			throw new updateException("Het teovoeging van de klant is gezakt");

		}

	}

	@Override
	public void deleteKlant(String achternaam) {
		String str = "delete from klant where achternaam = ?";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setString(1, achternaam);
			statement.executeUpdate();
			System.out.println(" Het wissen van deze klant is geslagd ");
		} catch (Exception ex) {
			throw new updateException("Het wissen van de klant is gezakt");
		}
	}

	@Override
	public boolean wezig(String achternaam) {
		boolean isWezig;
		String str = " select * from klant where klant.achternaam = ?;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setString(1, achternaam);
			ResultSet res = statement.executeQuery();
			if (res.next())
				isWezig = true;
			else
				isWezig = false;
		} catch (Exception ex) {
			throw new updateException(" Er is een probleem met database ");
		}
		return isWezig;
	}

	@Override
	public void updateKlantnaam(String achternaam, Klant klant) {
		String str = "update klant set voornaam = ?, achternaam = ?, tussenvoegsel = ? " + "where klant.achternaam = ?;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setString(1, klant.getVoornaam());
			statement.setString(2, klant.getAchternaam());
			statement.setString(3, klant.getTussenvoegsel());
			statement.setString(4, achternaam);
			statement.executeUpdate();

		} catch (Exception ex) {
			throw new updateException(" Het aanpassen van niew klant is gezakt ");
		}
	}

	@Override
	public void updateKlantAdres(String achternaam, Klant klant) {
		String str = "update klant set straatnaam = ?, huisnummer = ?, "
				+ "toevoeging = ?, postcode = ?, woonplaats = ?, " + "adrestype = ? " + "where klant.achternaam = ?;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setObject(1, klant.getKlantAdres().getStraatnaam());
			statement.setObject(2, klant.getKlantAdres().getHuisnummer());
			statement.setObject(3, klant.getKlantAdres().getToevoeging());
			statement.setObject(4, klant.getKlantAdres().getPostocde());
			statement.setObject(5, klant.getKlantAdres().getWoonplaats());
			statement.setObject(6, klant.getKlantAdres().getAdrestype());
			statement.setString(7, achternaam);
			statement.executeUpdate();

		} catch (Exception ex) {
			throw new updateException(" Het aanpassen van niew klant is gezakt ");
		}
	}

	@Override
	public List<Klant> getKlanten() {
		List<Klant> klanten = new ArrayList<>();
		String str = "SELECT * FROM klant;";

		try {
			connection = mn.getConnection();
			PreparedStatement statement = connection.prepareStatement(str);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				KlantAdresBuilder klantbuilder = new KlantAdresBuilder();
				Klant klant = new Klant();
				klant.setId(res.getInt(1));
				klant.setVoornaam(res.getString(2));
				klant.setAchternaam(res.getString(3));
				klant.setTussenvoegsel(res.getString(4));
				klantbuilder.straatNaam(res.getString(5));
				klantbuilder.huisNummer(res.getString(6));
				klantbuilder.toevoeging(res.getString(7));
				klantbuilder.postCode(res.getString(8));
				klantbuilder.woonplaats(res.getString(9));
				klantbuilder.adresType(res.getInt(10));
				klant.setKlantAdres(new KlantAdres(klantbuilder));
				klanten.add(klant);
			}
		} catch (Exception ex) {
			throw new updateException("Er is een probleem met klant tabel ");
		}
		return klanten;
	}
	@Override
	public String KlantString(Klant klant) {
		String show;
		String str = "select klant.id, klant.voornaam, klant.achternaam, "
				+ " klant.straatnaam, klant.huisnummer, klant.postcode, "
				+ "klant.woonplaats, Adres_Type.type "
				+ "from klant, Adres_type where " + "klant.id = ? and "
				+ "klant.adrestype = Adres_Type.id;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {

			statement.setInt(1, klant.getId());
			ResultSet res = statement.executeQuery();
			if (res.next()) {

				int id = res.getInt(1);
				String naam = res.getString(2);
				String achternaam = res.getString(3);
				String straat = res.getString(4);
				String huis = res.getString(5);
				String postcode = res.getString(6);
				String plaats = res.getString(7);
				String type = res.getString(8);
				show = id + "\t" + naam + "\t\t" + achternaam + "\t" + 
						straat + "\t\t" + huis + "\t" + postcode + "\t\t" + plaats + "\t\t" + type;
			}

			else
				show = "De naam is afwijzig";
		}

		catch (Exception ex) {
			throw new updateException(" Er is een probleem in de database ");
		}

		return show;
	}
	
	public Klant getKlant(String achternaam){
		String str = "select klant.id, klant.voornaam, klant.achternaam, klant.tussenvoegsel"
					+ " from klant where " + "klant.achternaam = ?;";

		try (Connection	connection = mn.getConnection();
			PreparedStatement statement = connection.prepareStatement(str)){
			statement.setString(1, achternaam);
			ResultSet res = statement.executeQuery();
			if (res.next()) {
				int id = res.getInt(1);
				String naam = res.getString(2);
				String tussenvoegsel = res.getString(4);
				klant = new Klant(id, naam, achternaam, tussenvoegsel);
			}
		} catch (Exception ex) {
			throw new updateException("Er is een probleem met klant tabel ");
		}
		return klant;
	}
	
	
	
	
}
	


