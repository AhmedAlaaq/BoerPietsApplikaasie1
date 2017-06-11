package dao;

import model.*;
import java.sql.*;
import java.math.*;
import dbmanager.*;
import java.util.*;
import exception.updateException;

public class BestellingDAO {
	private Klant klant;
	private ArtikelDAO adao;
	private Bestelling bestel;
	private ConnectionManager mn;
	private Connection connection;
	private Scanner input = new Scanner(System.in);
	private BestelRegelDAO brdao = new BestelRegelDAO(connection);

	public BestellingDAO(Connection connection) {
		mn = new ConnectionManager();
		connection = mn.getConnection();
	}

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
		bestel.setTotaalPrijs(bestel.Totaalprijs(brdao.getRegelen(bestel.getId())));
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

		for (BestelRegel bestelregel : brdao.getRegelen(id))
			brdao.deleteAllRegelenVanBestelling(id);
		;

		bestel = getBestelling(id);

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
		brdao.deleteBestelregel(regel.getId());
		String str = "UPDATE bestelling SET totaalPrijs = ? WHERE id = ?;";
		try (Connection connection = mn.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(str)) {
			preparedStatement.setBigDecimal(1, bestel.Totaalprijs(brdao.getRegelen(bestel.getId())));
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

	public boolean wezig(int id) {
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
		bestel = null;
		String str = "SELECT totaalPrijs, klantId FROM bestelling where id = ?;";

		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setInt(1, id);
			ResultSet res = statement.executeQuery();
			if (res.next()) {
				BigDecimal totaalPrijs = res.getBigDecimal(1);
				klant = getKlant(res.getInt(2));
				bestel = new Bestelling(id, totaalPrijs, klant);

			}
		} catch (Exception ex) {
			throw new updateException(" Er is een probleem met bestelling tabel !");
		}
		return bestel;
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
}
