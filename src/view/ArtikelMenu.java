package view;

import model.Artikel;
import org.slf4j.Logger;
import java.util.Scanner;
import org.slf4j.LoggerFactory;
import controller.ArtikelController;

public class ArtikelMenu {
	private Menu menu;
	private Artikel artikel;
	private boolean goedKeus = false;
	Validator validator = new Validator();
	private ArtikelController artikelController;
	private Scanner input = new Scanner(System.in);
	final Logger logger = LoggerFactory.getLogger(AccountMenu.class);

	public ArtikelMenu() {
		artikelController = new ArtikelController();
		System.out.println("\n &&& Artikel Menu &&&");
		System.out.println("1. Het toevoeren van een artikel ");
		System.out.println("2. Het Bijwerken van een artikel ");
		System.out.println("3. Het wissen van een artikel");
		System.out.println("4. Aanboden");
		System.out.println("5. Exit");
		System.out.print(" Vul u alstublieft uw keus in :");
		String keus = input.nextLine();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				artikelController.insert();
				new ArtikelMenu();
				break;
			case "2":
				do {
					System.out.print("Vul u het id van de artikel in :");
					int id = input.nextInt();
					if (validator.correcteInteger(id)) {
						AanpasMenu(id);
						goedKeus = true;
					}
				} while (!(goedKeus));

				new ArtikelMenu();
				break;
			case "3":
				artikelController.delete();
				new ArtikelMenu();
				break;
			case "4":
				artikelController.printArtikelen();
				new ArtikelMenu();
				break;
			case "5":
				menu = new Menu();
				menu.runMenu();

			default:
				logger.warn(" U moet een passende nummer geven ! Probeer nog een keer  ");
				new ArtikelMenu();
			}
		else
			new ArtikelMenu();

	}

	public void AanpasMenu(int id) {
		System.out.println("1: Naam aanpassen");
		System.out.println("2: Prijs aanpassen");
		System.out.println("3: Voorraad aanpassen");
		System.out.println("4: Exit");
		logger.info("\n wat wilt u alstublieft aanpassen ?");
		String keus = input.next();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				artikelController.update(id, Integer.parseInt(keus));
				new ArtikelMenu();
				break;
			case "2":
				artikelController.update(id, Integer.parseInt(keus));
				new ArtikelMenu();
				break;
			case "3":
				artikelController.update(id, Integer.parseInt(keus));
				new ArtikelMenu();
				break;
			case "4":
				new ArtikelMenu();

				break;
			default:
				logger.warn(" U moet een passende nummer geven ! Probeer nog een keer  ");
				AanpasMenu(id);
			}

		else {
			AanpasMenu(id);
		}
	}

}
