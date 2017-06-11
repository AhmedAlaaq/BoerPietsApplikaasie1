package dao;

import java.math.BigDecimal;
import java.sql.*;
import dbmanager.ConnectionManager;
import exception.updateException;
import model.*;
import model.KlantAdres.KlantAdresBuilder;

import java.math.*;
import java.util.*;

public class Server {
	private ConnectionManager mn;
	private Bestelling bestelling = null;
	private Artikel artikel = null;
	private BestelRegel bestelregel = null;
	private Klant klant;
	private Scanner input = new Scanner(System.in);

	public Server(Connection connection) {
		mn = new ConnectionManager();
		connection = mn.getConnection();

	}


		public BestelRegel insertBestelRegel(BestelRegel bestelregel) {
			Artikel artikel = bestelregel.getArtikel();
			System.out.println(bestelregel);
			Bestelling bestelling = bestelregel.getBestel();

			String str = "INSERT INTO bestel_regel (antaal, artikelId, bestelId, regelPrijs) VALUES (?, ?, ?, ?);";

			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str,
							Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setObject(1, bestelregel.getAntaal());
				preparedStatement.setObject(2, artikel.getId());
				preparedStatement.setObject(3, bestelling.getId());
				preparedStatement.setObject(4, bestelregel.bestelregelprijs(artikel, bestelregel.getAntaal()));
				preparedStatement.executeUpdate();
				ResultSet res = preparedStatement.getGeneratedKeys();
				if (res.isBeforeFirst()) {
					res.next();
					int id = res.getInt(1);
					bestelregel.setId(id);
					System.out.println("Het teovoeging van de Bestelregel is geslaagd");
					bestelregel = new BestelRegel(id, artikel, bestelling, bestelregel.getAntaal(),
							bestelregel.bestelregelprijs(artikel, bestelregel.getAntaal()));
				}

			} catch (Exception ex) {
				throw new updateException(" Er is een probleem met bestel_regel tabel !");
			}
			return bestelregel;
		}

		public BigDecimal updateBestelregel(int regelId, int aantal) {
			bestelregel = getBestelRegel(regelId);
			artikel = bestelregel.getArtikel();

			String str = "UPDATE bestel_regel SET antaal = ?, regelPrijs = ? WHERE id = ?;";

			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setInt(1, aantal);
				preparedStatement.setObject(2, bestelregel.bestelregelprijs(artikel, aantal));
				preparedStatement.setInt(3, regelId);
				preparedStatement.executeUpdate();
				System.out.println(" Het aanpassen van niew antaal is geslagd ");
			} catch (Exception ex) {
				throw new updateException(
						"Het aanpassen van niew antaal is gezakt" + ", er is een probleem in database !");

			}
			return bestelregel.bestelregelprijs(artikel, aantal);
		}

		public void deleteBestelregel(int regelId) {
			String str = "delete from bestel_regel where id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setInt(1, regelId);
				preparedStatement.executeUpdate();
				System.out.println("Het wissen van een regel in bestelling is geslagd");
			} catch (Exception ex) {
				throw new updateException(
						"Het wissen van een regel in bestelling is gezakt" + ", er is een probleem in database !");
			}

		}

		public void deleteAllRegelenVanBestelling(int bestelId) {
			String str = "delete from bestel_regel where bestelId = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setInt(1, bestelId);
				preparedStatement.executeUpdate();
				System.out.println("Het wissen van alle regelen in bestelling is geslagd");
			} catch (Exception ex) {
				throw new updateException("Het wissen van een regel in bestelling is gezakt");
			}
		}

		public boolean wezigRegel(int id) {
			boolean isWezig = false;
			String str = "select * from bestel_regel where id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, id);
				ResultSet res = statement.executeQuery();
				if (res.next())
					isWezig = true;
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}
			return isWezig;
		}

		public BestelRegel getBestelRegel(int regelId) {
			bestelling = null;
			String str = "select id, antaal, artikelId, bestelId, regelPrijs from bestel_regel where id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, regelId);
				ResultSet res = statement.executeQuery();
				if (res.next()) {
					bestelregel = new BestelRegel();
					bestelregel.setId(res.getInt(1));
					bestelregel.setAntaal(res.getInt(2));
					bestelregel.setArtikel(getArtikel(res.getInt(3)));
					bestelling = getBestelling(res.getInt(4));
					bestelregel.setBestel(bestelling);
					bestelregel.setRegelPrijs(res.getBigDecimal(5));
				}
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem met database !");
			}
			return bestelregel;

		}

		public Set<BestelRegel> getRegelen(int bestelId) {
			Set<BestelRegel> regelenVanBestelling = new LinkedHashSet<>();
			String str = "SELECT * FROM bestel_regel where bestelId = ? order by id;";

			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setInt(1, bestelId);
				ResultSet res = preparedStatement.executeQuery();
				while (res.next()) {
					BestelRegel regel = new BestelRegel();
					regel.setId(res.getInt(1));
					regel.setAntaal(res.getInt(2));
					regel.setArtikel(getArtikel(res.getInt(3)));
					regel.setBestel(getBestelling(res.getInt(4)));
					regel.setRegelPrijs(res.getBigDecimal(5));
					regelenVanBestelling.add(regel);
				}
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem met database !");
			}
			return regelenVanBestelling;

		}

		public Artikel getArtikel(int artikelId) {
			Artikel artikel = null;
			String str = " select * from artikelen where id = ? ;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, artikelId);
				ResultSet res = statement.executeQuery();
				if (res.next()) {
					artikel = new Artikel();
					artikel.setId(res.getInt(1));
					artikel.setNaam(res.getString(2));
					artikel.setPrijs(res.getBigDecimal(3));
					artikel.setVoorraad(res.getInt(4));

				}
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem met database !");
			}
			return artikel;
		}

		public int getArtikelId(int bestelId) {
			int artikelId = 0;
			String str = "SELECT artikelId FROM bestel_regel where bestelId = ?;";

			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, bestelId);
				ResultSet res = statement.executeQuery();
				if (res.next()) {
					artikelId = res.getInt(1);

				}
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem met database !");
			}
			return artikelId;

		}
	
	
	/* -------------------------------------------------------*/
	
		

		public Bestelling insertBestelling(Bestelling bestel) {
			String str = "insert into bestelling (totaalPrijs, klantId) " + "values (?, ?);";
			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str,
							Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setBigDecimal(1, bestel.getTotaalPrijs());
				preparedStatement.setInt(2, bestel.getKlant().getId());
				preparedStatement.executeUpdate();
				ResultSet res = preparedStatement.getGeneratedKeys();
				if (res.isBeforeFirst()) {
					res.next();
					int id = res.getInt(1);
					bestel.setId(id);
					System.out.println("Het teovoeging van een Bestelling is geslaagd");
					bestel = new Bestelling(id, bestel.getTotaalPrijs(), bestel.getKlant());
				}

			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}
			return bestel;
		}

		public void addBestelregel(Bestelling bestel) {
			bestel.setTotaalPrijs(bestel.Totaalprijs(getRegelen(bestel.getId())));
			String str = "UPDATE bestelling SET totaalPrijs = ? WHERE id = ? and klantId = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setBigDecimal(1, bestel.getTotaalPrijs());
				preparedStatement.setInt(2, bestel.getId());
				preparedStatement.setInt(3, bestel.getKlant().getId());
				preparedStatement.executeUpdate();
				System.out.println(" Het totaal prijs is : " + bestel.getTotaalPrijs() + " geworden na het toevoeging");

			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}

		}

	

		public void deleteBestelling(int id) {

			for (BestelRegel bestelregel : getRegelen(id))
				deleteAllRegelenVanBestelling(id);
			;

			bestelling = getBestelling(id);

			String str = "DELETE FROM bestelling WHERE bestelling.id = ?;";

			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();
				System.out.println("Het wissen van een bestelling is geslagd");
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}

		}

		public void deleteBestelregel(BestelRegel regel, Bestelling bestel) {
			deleteBestelregel(regel.getId());
			String str = "UPDATE bestelling SET totaalPrijs = ? WHERE id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setBigDecimal(1, bestel.Totaalprijs(getRegelen(bestel.getId())));
				preparedStatement.setInt(2, bestel.getId());
				preparedStatement.executeUpdate();
				System.out.println("Het wissen van een bestelregel in bestelling is geslagd");

			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}

		}

		public void updateBestelling(int bestelId, BigDecimal nieuwPrijs) {
			String str = "update bestelling set totaalPrijs = ? where id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				preparedStatement.setBigDecimal(1, nieuwPrijs);
				preparedStatement.setInt(2, bestelId);
				preparedStatement.executeUpdate();
				System.out.println(" Het aanpassen van het aantal van kazen is geslaagd worden ");

			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}

		}

		public boolean wezigBestelling(int id) {
			boolean isWezig;
			String str = " select * from bestelling where bestelling.id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, id);
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

		public Set<Bestelling> getBestellingen() {

			Set<Bestelling> bestellingen = new LinkedHashSet<>();

			String str = "select * FROM bestelling order by id;";

			try (Connection connection = mn.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(str)) {
				ResultSet res = preparedStatement.executeQuery();
				while (res.next()) {
					Bestelling bestelling = new Bestelling();
					bestelling.setId(res.getInt(1));
					bestelling.setTotaalPrijs(res.getBigDecimal(2));
					bestelling.setKlant(getKlant(res.getInt(3)));
					bestellingen.add(bestelling);
				}
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem in database !");
			}
			return bestellingen;
		}

		public Bestelling getBestelling(int id) {
			klant = null;
			bestelling = null;
			String str = "SELECT totaalPrijs, klantId FROM bestelling where id = ?;";

			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, id);
				ResultSet res = statement.executeQuery();
				if (res.next()) {
					BigDecimal totaalPrijs = res.getBigDecimal(1);
					klant = getKlant(res.getInt(2));
					bestelling = new Bestelling(id, totaalPrijs, klant);

				}
			} catch (Exception ex) {
				throw new updateException(" Er is een probleem met bestelling tabel !");
			}
			return bestelling;
		}

		public Klant getKlant(int id) {
			klant = null;
			String str = "select klant.id, klant.voornaam, klant.achternaam, klant.tussenvoegsel" + " from klant where "
					+ "klant.id = ?;";

			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, id);
				ResultSet res = statement.executeQuery();
				if (res.next()) {
					String naam = res.getString(2);
					String achterNaam = res.getString(3);
					String tussenvoegsel = res.getString(4);
					klant = new Klant(id, naam, achterNaam, tussenvoegsel);
				}
			} catch (Exception ex) {
				throw new updateException("Er is een probleem met klant tabel ");
			}
			return klant;		
		}
	
/* --------------------------------------------*/
		
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

		
		public boolean wezigKlant(String achternaam) {
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

		
		public List<Klant> getKlanten() {
			List<Klant> klanten = new ArrayList<>();
			String str = "SELECT * FROM klant;";

			try {
				Connection connection = mn.getConnection();
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
		
		
		public void insertArtikel(Artikel artikel) {

			String str = " insert into artikelen (naam, prijs, voorraad) " + "values (?, ?, ?);";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);) {
				statement.setString(1, artikel.getNaam());
				statement.setBigDecimal(2, artikel.getPrijs());
				statement.setInt(3, artikel.getVoorraad());
				statement.executeUpdate();
				ResultSet res = statement.getGeneratedKeys();
				if (res.isBeforeFirst()) {
					res.next();
					artikel.setId(res.getInt(1));
					System.out.println("Het teovoeging van de artikel is geslaagd");
				}
			} catch (Exception ex) {
				throw new updateException("Het teovoeging van de artikel is gezakt");

			}

		}

		public void deleteArtikel(int id) {
			String str = "delete from artikelen where id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, id);
				statement.executeUpdate();
				System.out.println(" Het wissen van deze artikel is geslagd ");
			} catch (Exception ex) {
				throw new updateException("Het wissen van de artikel is gezakt");
			}
		}

		public boolean wezigArtikel(int id) {
			boolean isWezig;
			String str = " select * from artikelen where artikelen.id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, id);
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

		public void updatenaam(int id, String naam) {
			String str = "update artikelen set naam = ? " + "where artikelen.id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setString(1, naam);
				statement.setInt(2, id);
				statement.executeUpdate();

			} catch (Exception ex) {
				throw new updateException(" Het aanpassen van niew naam is gezakt ");
			}
		}

		public void updatePrijs(int id, BigDecimal prijs) {

			String str = "update artikelen set prijs = ? " + "where artikelen.id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setBigDecimal(1, prijs);
				statement.setInt(2, id);
				statement.executeUpdate();

			} catch (Exception ex) {
				throw new updateException(" Het aanpassen van niewe prijs is gezakt  ");
			}
		}

		public void updateVoorraad(int id, int voorraad) {
			String str = "update artikelen set voorraad = ? " + "where artikelen.id = ?;";
			try (Connection connection = mn.getConnection();
					PreparedStatement statement = connection.prepareStatement(str)) {
				statement.setInt(1, voorraad);
				statement.setInt(2, id);
				statement.executeUpdate();

			} catch (Exception ex) {
				throw new updateException(" Het aanpassen van niewe voorraad is gezakt  ");
			}
		}

		public List<Artikel> getArtikelen() {
			List<Artikel> Artikelen = new ArrayList<>();
			String str = "SELECT * FROM artikelen;";

			try {
				Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str);
				ResultSet res = statement.executeQuery();
				while (res.next()) {
					Artikel artikel = new Artikel();
					artikel.setId(res.getInt(1));
					artikel.setNaam(res.getString(2));
					artikel.setPrijs(res.getBigDecimal(3));
					artikel.setVoorraad(res.getInt(4));
					Artikelen.add(artikel);
				}
			} catch (Exception ex) {
				throw new updateException("Er is een probleem met artikelen tabel ");
			}
			return Artikelen;
		}



}





