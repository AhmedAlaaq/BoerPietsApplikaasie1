package dao;

import model.*;
import java.sql.*;
import java.math.*;
import java.util.*;
import exception.updateException;
import dbmanager.ConnectionManager;

public class BestelRegelDAO {

	private BestellingDAO bdao;
	private ConnectionManager mn;
	private Connection connection;
	private Bestelling bestelling = null;
	private Artikel artikel = new Artikel();
	private BestelRegel bestelregel = null;

	public BestelRegelDAO(Connection connection) {
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
			throw new updateException("Het aanpassen van niew antaal is gezakt" + ", er is een probleem in database !");

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

	public boolean wezig(int id) {
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
				bestelling = bdao.getBestelling(res.getInt(4));
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
		bdao = new BestellingDAO(connection);

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
				regel.setBestel(bdao.getBestelling(res.getInt(4)));
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
}
