package controller;

import java.util.Scanner;
import dao.ArtikelDAO;
import model.Artikel;
import view.Validator;
import java.util.List;
import java.sql.Connection;
import java.math.*;

public class ArtikelController {
	private ArtikelDAO artikeldao;
	private Connection connection;
	private Scanner input = new Scanner(System.in);
	private Validator validator = new Validator();
	private boolean goedKeus = false;

	public ArtikelController() {
		artikeldao = new ArtikelDAO(connection);
	}
	

	public void insert() {
		Artikel artikel = new Artikel();
		System.out.print("Vul de naam van het artikel in :");
		artikel.setNaam(input.nextLine());
		do {
			System.out.print("Vul de prijs van het artikel in :");
			String prijs = input.next();
			if (validator.inputBigDecimal(prijs)) {
				artikel.setPrijs(new BigDecimal(prijs));
				goedKeus = true;
			}
		} while (!(goedKeus));

		do {
			System.out.print("Hoeveel artikelen zijn er in voorraad?");
			int number = input.nextInt();
			if (validator.correcteInteger(number)) {
				artikel.setVoorraad(number);
				artikeldao.insertArtikel(artikel);
				goedKeus = true;
			}
		} while (!(goedKeus));
	}

	public void delete() {
		Artikel artikel = new Artikel();
		do {
			System.out.print("Vul u het id van de artikel in :");
			int id = input.nextInt();
			if (validator.correcteInteger(id)) {
				if (!artikeldao.wezig(id))
					System.out.println(" De artikel is afwijzig ! ");
				else

					artikeldao.deleteArtikel(id);
				goedKeus = true;
			}
		} while (!(goedKeus));

	}

	public void update(int id, int keus) {
		Artikel artikel = new Artikel();
		if (!artikeldao.wezig(id))
			System.out.println(" De artikel is afwijzig ! ");
		else {
			boolean isJuist;
			switch (keus) {
			case 1:
				System.out.print(" Vul het niew naam in :");
				artikeldao.updatenaam(id, input.next());
				System.out.println(" Het aanpassen is geslaagd worden ");
				break;
			case 2:
				do {
					System.out.print(" Vul het niewe prijs in :");
					String prijs = input.next();
					if (validator.inputBigDecimal(prijs)) {
						artikeldao.updatePrijs(id, new BigDecimal(prijs));
						System.out.println(" Het aanpassen is geslaagd worden ");
						goedKeus = true;
					}
				} while (!(goedKeus));

				break;
			case 3:

				do {

					System.out.print(" Vul het niewe voorraad in :");
					String vorraad = input.next();
					if (validator.correcteKeus(vorraad)) {
						artikeldao.updateVoorraad(id, Integer.parseInt(vorraad));
						System.out.println(" Het aanpassen is geslaagd worden ");
						isJuist = true;
					} else {
						System.out.println("\n De voorrad moet een nummer zijn ! Probeer nog een keer");
						isJuist = false;
					}
				} while (!(isJuist));
				break;

			}
		}
	}

	public void printArtikelen() {
		List<Artikel> artikelen = artikeldao.getArtikelen();
		System.out.println(" &&&& Artikelen informatie &&&& ");
		System.out.println("--------------------------------");
		for (int i = artikelen.size() - 1; i >= 0; i--)
			System.out.println(artikelen.get(i).toString());

	}
}
