package view;

import java.util.Scanner;
import java.util.Scanner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controller.KlantController;

public class KlantMenu {
	private Menu menu = new Menu();
	private Validator validator = new Validator();
	private Scanner input = new Scanner(System.in);
	private KlantController klantcontroller;
	final Logger logger = LoggerFactory.getLogger(KlantMenu.class);
	boolean goedkues = false;

	public KlantMenu() {
		KlantBody();
	}

	public void KlantBody() {
		System.out.println("\n\n| Klant gegevens informatie |");
		System.out.println("| --------------------------|");
		System.out.println("1. Met Mongo-Database");
		System.out.println("2. Met Mysql-Database");
		System.out.println("3. terug naar hoofdmenu");
		System.out.print("Kies u alstublieft uw database voor klant gegevens 1:MongoDB, 2:MysqlDB :");
		String keus = input.next();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				KlantDAOMenu(Integer.parseInt(keus));
			case "2":
				KlantDAOMenu(Integer.parseInt(keus));
			case "3": {
				logger.warn("\nWilt u alstubliefd naar hoofdmenu terug ?");
				int answer = validator.uitLoggen(input.next());
				switch (answer) {
				case 1:
					menu.runMenu();
					;
					break;
				case 2:
					System.out.println();
					KlantBody();
					break;
				default:
					logger.info(" U moet een passende keus geven ! Probeer nog een keer ");
					System.out.println();
					KlantBody();
					break;
				}
			}
			break;
			default:
				logger.info(" U moet een passende nummer geven ! Probeer nog een keer  ");
				KlantBody();

			}
		else
			KlantBody();

	}

	public void KlantDAOMenu(int keus) {
		klantcontroller = new KlantController(keus);
		System.out.println("\n &&& Klant Menu &&&");
		System.out.println("1. Het toevoeren van een klant ");
		System.out.println("2. Het Bijwerken van een klant ");
		System.out.println("3. Het wissen van een klant");
		System.out.println("4. Aanboden");
		System.out.println("5. Exit");
		System.out.print(" Vul u alstublieft uw keus in :");
		String kies = input.next();
		if (validator.correcteKeus(kies))
			switch (kies) {
			case "1":
				klantcontroller.insert();
				KlantBody();
				break;
			case "2":
				do {
					System.out.print("Vul u de achternaam van de klant in :");
					String str = input.nextLine();
					if (validator.correcteString(str)) {
						AanpasMenu(str);
						goedkues = true;
					}
				} while (!(goedkues));
				KlantDAOMenu(keus);

				break;
			case "3":
				klantcontroller.delete();
				KlantBody();
				break;
			case "4":
				klantcontroller.printklanten();
				KlantBody();
				break;
			case "5":
				menu = new Menu();
				menu.runMenu();

			default:
				logger.warn(" U moet een passende nummer geven ! Probeer nog een keer  ");
				KlantBody();
			}
		else
			KlantBody();

	}

	public void AanpasMenu(String achternaam) {
		System.out.println("1: Naam aanpassen");
		System.out.println("2: Adres aanpassen");
		System.out.println("3: Exit");
		logger.info("\n wat wilt u alstublieft aanpassen ?");
		String keus = input.next();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				klantcontroller.update(achternaam, Integer.parseInt(keus));
				new KlantMenu();
				break;
			case "2":
				klantcontroller.update(achternaam, Integer.parseInt(keus));
				new KlantMenu();
				break;
			case "3":
				new KlantMenu();
				break;
			default:
				logger.warn(" U moet een passende nummer geven ! Probeer nog een keer  ");
				AanpasMenu(achternaam);
			}

		else {
			AanpasMenu(achternaam);
		}
	}

}
