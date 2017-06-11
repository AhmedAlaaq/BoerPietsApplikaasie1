package view;

import model.Artikel;
import org.slf4j.Logger;
import model.Bestelling;
import java.util.Scanner;
import org.slf4j.LoggerFactory;
import controller.BestellingControllerMetServer;


public class BestellingMenu {
	private Menu menu;
	private Bestelling bestelling;
	private boolean goedKeus = false;
	private BestellingControllerMetServer bestelController;
	private Validator validator = new Validator();
	private Scanner input = new Scanner(System.in);
	final Logger logger = LoggerFactory.getLogger(AccountMenu.class);

	public BestellingMenu() {
		bestelController = new BestellingControllerMetServer();
		System.out.println("\n &&& Bestelling Menu &&&");
		System.out.println("1. Het toevoeren van een bestelling ");
		System.out.println("2. Het Bijwerken van een bestelling ");
		System.out.println("3. Het wissen van een bestelling");
		System.out.println("4. Aanboden");
		System.out.println("5. Exit");
		System.out.print(" Vul u alstublieft uw keus in :");
		String keus = input.nextLine();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				bestelController.insert();
				new BestellingMenu();
				break;
			case "2":
				do {
					System.out.print("Vul u het id van de bestelling in :");
					int id = input.nextInt();
					if (validator.correcteInteger(id)) {
						AanpasMenu(id);
						goedKeus = true;
					}
				} while (!(goedKeus));

				new BestellingMenu();
				break;
			case "3":
				bestelController.delete();
				new BestellingMenu();
				break;
			case "4":
				bestelController.printBestellingen();
				new BestellingMenu();
				break;
			case "5":
				menu = new Menu();
				menu.runMenu();

			default:
				logger.warn(" U moet een passende nummer geven ! Probeer nog een keer  ");
				new BestellingMenu();
			}
		else
			new BestellingMenu();

	}

	public void AanpasMenu(int id) {
		System.out.println("1: Het Toevoeging van een bestelregel");
		System.out.println("2: Het wissen  van een bestelregel");
		System.out.println("3:  Het wisselen van het aantal van artikelen");
		System.out.println("4: Exit");
		logger.info("\n wat wilt u alstublieft aanpassen ?");
		String keus = input.next();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				bestelController.update(id, Integer.parseInt(keus));
				new BestellingMenu();
				break;
			case "2":
				bestelController.update(id, Integer.parseInt(keus));
				new BestellingMenu();
				break;
			case "3":
				bestelController.update(id, Integer.parseInt(keus));
				new BestellingMenu();
				break;
			case "4":
				new BestellingMenu();

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
