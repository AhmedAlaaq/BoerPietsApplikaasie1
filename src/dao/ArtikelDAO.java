package dao;

import exception.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dbmanager.*;
import model.Artikel;
import java.math.*;

public class ArtikelDAO {
	private ConnectionManager mn;
	private Connection connection;
	private Artikel artikel;

	public ArtikelDAO(Connection connection) {
		mn = new ConnectionManager();
		connection = mn.getConnection();

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

	public boolean wezig(int id) {
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
			connection = mn.getConnection();
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
