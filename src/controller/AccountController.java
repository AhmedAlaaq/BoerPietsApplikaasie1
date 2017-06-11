package controller;

//////////
import view.*;
import java.util.Scanner;
import java.sql.*;
import dao.AccountDAO;
import model.Account;

//////////
public class AccountController {
	AccountDAO accountdao;
	Connection connection;
	Account account;
	Validator validator = new Validator();
	Scanner input = new Scanner(System.in);
	boolean goedKeus = false;

	public AccountController() {
		accountdao = new AccountDAO(connection);
	}

	/* Controller CLASS */
	public void InsertAccount() {

		account = new Account();
		System.out.println(" ***** Vul het account informatie in *****");
		System.out.print(" Vul naam in :");
		account.setNaam(input.nextLine());
		System.out.print(" Vul wachtWoord in :");
		account.setWachtWoord(input.nextLine());

		do {
			System.out.print(" Vul de record status in :");
			String keus = input.nextLine();

			if (validator.inputStatus(keus)) {
				account.setAccountStatus(Integer.parseInt(keus));
				accountdao.InsertAccount(account);
				goedKeus = true;
			}
		} while (!(goedKeus));
	}

	public void UpdateAccount() {
		account = new Account();
		System.out.println(" ***** Informatie Bijwerken *****");
		System.out.print(" Vul de naam in :");
		account.setNaam(input.nextLine());
		System.out.print("Kies  het nieuw accountStatus als 1: Admin" + " 2: Medewerker 3: Klant:");
		do {
			System.out.print(" Vul de record status in :");
			String keus = input.nextLine();

			if (validator.inputStatus(keus)) {
				account.setAccountStatus(Integer.parseInt(keus));
				accountdao.InsertAccount(account);
				goedKeus = true;
			}
		} while (!(goedKeus));
	}

	public void DeleteAccount() {
		account = new Account();
		System.out.println(" ***** Informatie Wissen *****");
		System.out.print(" Vul de naam van de gebruiker in :");
		account.setNaam(input.nextLine());
		accountdao.DeletAccount(account);

	}

	public void ShowAccount() {
		account = new Account();
		System.out.print(" Vul de naam van de gebruiker in :");
		account.setNaam(input.nextLine());
		System.out.println(accountdao.ShowAccount(account));
	}
}
