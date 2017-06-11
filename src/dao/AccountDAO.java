package dao;

///////////
import java.sql.*;
import java.util.Scanner;
import dbmanager.*;
import exception.updateException;
import model.Account;

////////////
public class AccountDAO {
	private ConnectionManager mn;
	private Connection connection;
	private Account account;

	public AccountDAO(Connection connection) {
		mn = new ConnectionManager();
		connection = mn.getConnection();
	}

	/* INSERT DATA CLASS */
	public void InsertAccount(Account account) {

		String str = " insert into accounten (naam, wachtWoord, accountStatus) " + "values (?, ?, ?);";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, account.getNaam());
			statement.setString(2, account.getWachtWoord());
			statement.setInt(3, account.getAccountStatus());
			statement.executeUpdate();
			ResultSet res = statement.getGeneratedKeys();
			if (res.isBeforeFirst()) {
				res.next();
				account.setId(res.getInt(1));
				System.out.println(" Het invullen van het niew account is geslagd ");
			}
		} catch (Exception ex) {
			throw new updateException("Het teovoeging van het account is gezakt");

		}
	}

	/* DE BIJWEREN KLASSE */
	public void UpDateAccount(Account account) {

		String str = " update accounten set accountStatus = ? " + "where accounten.naam = ?;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {
			statement.setInt(1, account.getAccountStatus());
			statement.setString(2, account.getNaam());
			statement.executeUpdate();
			System.out.println(" Het bijweken van het account is geslagd ");
		} catch (Exception ex) {
			throw new updateException("Het aanpassen van het account is gezakt");
		}
	}

	/* DE KLASSE VAN WISSEN */
	public void DeletAccount(Account account) {

		String str = " delete from accounten " + "where accounten.naam = ?;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {

			statement.setString(1, account.getNaam());
			statement.executeUpdate();
			System.out.println(" Het wissen van het record is geslagd ");
		} catch (Exception ex) {
			throw new updateException("Het wissen van het account is gezakt");
		}
	}

	/* DE KLASSE VAN DE INFORMATIE AANBODEN */
	public String ShowAccount(Account account) {
		String show;
		String str = "select Accounten.id, Accounten.naam," + " Accounten.wachtWoord ,Account_Type.type "
				+ "from Accounten, Account_Type where " + "Accounten.naam = ? and "
				+ "Accounten.accountStatus = Account_Type.id;";
		try (Connection connection = mn.getConnection();
				PreparedStatement statement = connection.prepareStatement(str)) {

			statement.setString(1, account.getNaam());
			ResultSet res = statement.executeQuery();
			if (res.next()) {

				int id = res.getInt(1);
				String naam = res.getString(2);
				String wachtwoord = res.getString(3);
				String type = res.getString(4);
				System.out.println(" &&&& Record informatie &&&& ");
				System.out.println("Id\t Naam\tWachtWoord\tType ");
				System.out.println("------------------------------------------------------");
				show = id + "\t" + naam + "\t" + wachtwoord + "\t\t" + type;
			}

			else
				show = "De naam is afwijzig";
		}

		catch (Exception ex) {
			throw new updateException(" Er is een probleem in de database ");
		}

		return show;
	}
}
