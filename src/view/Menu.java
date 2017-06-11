
package view;

////////////////
import org.slf4j.Logger;
import java.util.Scanner;
import org.slf4j.LoggerFactory;
////////////////
public class Menu {
	Validator validator = new Validator();
	Scanner input = new Scanner(System.in);
	final Logger logger = LoggerFactory.getLogger(Menu.class);

	public void runMenu() {
		prHeider();
		prMenu();
		System.out.print(" Vul u alstublieft uw keus in :");
		String keus = input.nextLine();
		if (validator.correcteKeus(keus))
			switch (keus) {
			case "1":
				AccountMenu Accountmenu = new AccountMenu();
				break;
			case "2":
				KlantMenu klantmenu = new KlantMenu();
				break;
			case "3":
				BestellingMenu Bestelmenu = new BestellingMenu();
				break;
			case "4":
				ArtikelMenu artikelmenu = new ArtikelMenu();
			case "5": {
				logger.warn(" Wilt u alstubliefd Afsluiten ?");
				int answer = validator.uitLoggen(input.nextLine());
				switch (answer) {
				case 1:
					logger.info(" Tot ziens");
					System.exit(1);
					break;
				case 2:
					logger.info("");
					runMenu();
					break;
				default: {
					logger.warn(" U moet een passende keus geven ! Probeer nog een keer");
					System.out.println();
					runMenu();
				}
				}
			}
			default:
				logger.warn(" U moet een passende nummer geven ! Probeer nog een keer  ");
				runMenu();
			}

		else
			runMenu();
	}

	/*
	 * Let goed op dat ik alle methode (public) gemaakt heb want ik ga hem in
	 * andere submenu gebruiken
	 */
	/* Gaan we het hoofd van het menu printen */
	public void prHeider() {
		System.out.println("| ------------------------------- |");
		System.out.println("|           WelKom bij            |");
		System.out.println("|   Boer Piets - Applikaasie Aap  |");
		System.out.println("| ------------------------------- |");
	}

	/* Gaan we het lichaam van het menu printen */
	public void prMenu() {
		System.out.println("1. Ga naar Accounten ");
		System.out.println("2. Ga naar klanengegevens ");
		System.out.println("3. Ga naar bestelling details");
		System.out.println("4. Ga naar artikel details");
		System.out.println("5. Exit");
	}

}
