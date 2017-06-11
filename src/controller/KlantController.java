package controller;

import java.util.Scanner;
import dao.*;
import model.*;
import model.KlantAdres.KlantAdresBuilder;
import view.Menu;
import view.Validator;
import java.util.List;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KlantController {
	final Logger logger = LoggerFactory.getLogger(Menu.class);
	private KlantInterface klantinterface;
	private Connection connection;
	private Scanner input = new Scanner(System.in);
	private Validator validator = new Validator();
	private boolean goedKeus = false;

	public KlantController(int keus) {
		klantinterface = KlantFactory.Kies(keus);

	}

	public void insert() {

		Klant klant = new Klant();
		KlantAdresBuilder klantbuilder = new KlantAdresBuilder();
		do {
			System.out.print("Vul de naam van het Klant in :");
			String voornaam = input.nextLine();
			if (validator.correcteString(voornaam)) {
				klant.setVoornaam(voornaam);
				goedKeus = true;

			}
		} while (!goedKeus);

		System.out.print("Vul de achternaam van het klant in :");
		klant.setAchternaam(input.nextLine());
		System.out.print("Vul de tussenvoegsel van het klant in :");

		klant.setTussenvoegsel(input.nextLine());
		System.out.print(" Straatnaam :");
		klantbuilder.straatNaam(input.nextLine());
		System.out.print(" Huisnummer :");
		klantbuilder.huisNummer(input.nextLine());
		System.out.print(" Toevoeging :");
		klantbuilder.toevoeging(input.nextLine());
		correctPostcode(klant);
		System.out.print(" Woonplaats :");
		klantbuilder.woonplaats(input.nextLine());
		correctInputStatus(klant);
		klantinterface.insertKlant(klant);
	}

	public void delete() {

		Klant klant = new Klant();

		System.out.print("Vul u de achternaam van de klant in :");
		String achternaam = input.next();
		if (!klantinterface.wezig(achternaam))
			System.out.println(" De klant is afwijzig ! ");
		else

			klantinterface.deleteKlant(achternaam);

	}

	public void update(String achternaam, int keus) {
		Klant klant = new Klant();
		KlantAdresBuilder klantadresbuilder = new KlantAdresBuilder();
		if (!klantinterface.wezig(achternaam))
			System.out.println(" De klant is afwijzig ! ");
		else {
			boolean isJuist;
			switch (keus) {
			case 1:
				do {
					System.out.print("Vul de naam van het Klant in :");
					String voornaam = input.nextLine();
					if (validator.correcteString(voornaam)) {
						klant.setVoornaam(voornaam);
						goedKeus = true;

					}
				} while (!goedKeus);
				System.out.print(" Vul de nieuwe achternaam in :");
				klant.setAchternaam(input.nextLine());
				klantinterface.updateKlantnaam(achternaam, klant);
				System.out.println(" Het aanpassen is geslaagd worden ");
				break;
			case 2:

				System.out.print(" Vul de niewe straatnaam in :");
				klantadresbuilder.straatNaam(input.nextLine());
				System.out.print(" Huisnummer :");
				klantadresbuilder.huisNummer(input.nextLine());
				System.out.print(" Toevoeging :");
				klantadresbuilder.toevoeging(input.nextLine());
				System.out.print(" postcode :");
				klantadresbuilder.postCode(input.nextLine());
				System.out.print(" Woonplaats :");
				klantadresbuilder.woonplaats(input.nextLine());
				System.out.print(" Adrestype :");
				klantadresbuilder.adresType(input.nextInt());
				klant.setKlantAdres(new KlantAdres(klantadresbuilder));

				klantinterface.updateKlantAdres(achternaam, klant);

				break;

			}
		}
	}

	public void printklanten() {

		List<Klant> klanten = klantinterface.getKlanten();
		System.out.println(" &&&& Klanten informatie &&&& ");
		System.out.println("--------------------------------");
		System.out.println("Id\t Naam\t\tAchternaam\tStraatnaam\tHuisnummer\t" + "Postcode\tWoonplaats\tType ");

		for (int i = klanten.size() - 1; i >= 0; i--)
			System.out.println(klantinterface.KlantString(klanten.get(i)));

	}

	public Klant correctInputStatus(Klant klant) {
		boolean goedKeus = false;
		KlantAdresBuilder klantbuilder = new KlantAdresBuilder();
		do {
			System.out.print(" Vul de klant adres status in 1:Huis 2:werk 3:anders ::");
			String keus = input.nextLine();

			if (validator.inputStatus(keus)) {
				klantbuilder.adresType(Integer.parseInt(keus));
				klant.setKlantAdres(new KlantAdres(klantbuilder));
				goedKeus = true;
			}
			else
				logger.warn(" U moet een passende input geven ! Probeer nog een keer ");
		} while (!(goedKeus));
		return klant;
	}
	
	public Klant correctPostcode(Klant klant) {
		boolean goedKeus = false;
		KlantAdresBuilder klantbuilder = new KlantAdresBuilder();
		do {
			System.out.print(" Vul de postcode in :");
			String postcode = input.nextLine();

			if (validator.postCode(postcode)) {
				klantbuilder.postCode(postcode);
				klant.setKlantAdres(new KlantAdres(klantbuilder));
				goedKeus = true;
			} else
				logger.warn(" U moet een passende postcode geven ! Probeer nog een keer ");
		} while (!(goedKeus));
		return klant;
	}
}
