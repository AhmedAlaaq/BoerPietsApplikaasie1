package controller;

import dao.*;
import model.*;
import view.*;
import java.util.*;
import java.math.*;
import controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;

public class BestellingController {

	private BestellingDAO bdao;
	private Validator validator = new Validator();
	private BestelRegelDAO brdao;
	private Artikel artikel;
	private Klant klant;
	private KlantDAOMysql klantdao;
	private Connection connection;
	private ArtikelDAO artikeldao;
	private boolean goedKeus = false;
	private Scanner input = new Scanner(System.in);
	final Logger logger = LoggerFactory.getLogger(Menu.class);
	private KlantController klantController = new KlantController(2);

	public BestellingController() {
		bdao = new BestellingDAO(connection);
		brdao = new BestelRegelDAO(connection);
		klantdao = new KlantDAOMysql(connection);
		artikeldao = new ArtikelDAO(connection);
	}

	public void insert() {
		Bestelling bestelling = new Bestelling();
		BestelRegel regel = new BestelRegel();

		do {
			System.out.print("Vul de achternaam van de klant in :");
			String achternaam = input.nextLine();
			if (klantdao.wezig(achternaam)) {
				bestelling.setKlant(klantdao.getKlant(achternaam));
				goedKeus = true;
			}
		} while (!(goedKeus));

		bestelling = bdao.insertBestelling(bestelling);
		regel.setBestel(bestelling);

		System.out.print("Hoeveel artiklen wilt u :");
		int num = input.nextInt();
		for (int i = 0; i < num; i++) {
			System.out.print(" Vul het antaal van kazen in :");
			regel.setAntaal(input.nextInt());
			do {
				System.out.print("Vul de ID van het artikel in :");
				int artikelId = input.nextInt();
				if (validator.correcteInteger(artikelId) && artikeldao.wezig(artikelId)) {
					regel.setArtikel(brdao.getArtikel(artikelId));
					regel = brdao.insertBestelRegel(regel);
					goedKeus = true;
				}

			} while (!(goedKeus));
			bdao.addBestelregel(bestelling);
			System.out.println(bestelling);
		}
	}

	public void delete() {
		Bestelling bestelling = new Bestelling();
		do {
			System.out.print("Vul u het id van de Bestelling in :");
			int id = input.nextInt();
			if (validator.correcteInteger(id)) {
				if (!bdao.wezig(id))
					System.out.println(" Deze bestelling is afwijzig ! ");
				else

					bdao.deleteBestelling(id);
				goedKeus = true;
			}
		} while (!(goedKeus));

	}

	public void update(int id, int keus) {

		BestelRegel regel = new BestelRegel();
		Bestelling bestelling = new Bestelling();

		if (!bdao.wezig(id))
			System.out.println(" De bestelling is afwijzig ! ");
		else {
			bestelling = bdao.getBestelling(id);
			boolean isJuist;
			switch (keus) {
			case 1:
				do {
					System.out.print("Vul de ID van het artikel in :");
					int artikelId = input.nextInt();
					if (validator.correcteInteger(artikelId) && artikeldao.wezig(artikelId)) {
						artikel = brdao.getArtikel(artikelId);
						regel.setArtikel(artikel);
						goedKeus = true;
					}
				} while (!(goedKeus));
				System.out.print("Vul het antaal van kazen voor het nieuwe bestelregel in :");
				regel.setAntaal(input.nextInt());
				regel.setBestel(bdao.getBestelling(id));
				regel = brdao.insertBestelRegel(regel);
				bestelling.setTotaalPrijs(bestelling.Totaalprijs(brdao.getRegelen(id)));
				bdao.addBestelregel(bestelling);
				break;
			case 2:
				do {
					printRegelenVanBestelling(id);
					System.out.print("Vul de ID van het Bestel regel in :");
					int bestelregelId = input.nextInt();
					if (validator.correcteInteger(bestelregelId) && brdao.wezig(bestelregelId)) {
						bdao.deleteBestelregel(brdao.getBestelRegel(bestelregelId), bdao.getBestelling(id));
						goedKeus = true;
					}
				} while (!(goedKeus));

				break;
			case 3:

				do {
					System.out.print("Vul de ID van het Bestel regel in :");
					int bestelregelId = input.nextInt();
					if (validator.correcteInteger(bestelregelId) && brdao.wezig(bestelregelId)) {
						System.out.print("Vul het nieuw aantal van de kazen in :");
						int aantal = input.nextInt();
						if (validator.correcteInteger(aantal))
							bdao.updateBestelling(id, brdao.updateBestelregel(bestelregelId, aantal));
						goedKeus = true;
					}
				} while (!(goedKeus));

				break;

			}
		}
	}

	
	public void printRegelenVanBestelling(int bestelId){
		Bestelling bestelling = bdao.getBestelling(bestelId);
		Set<BestelRegel> regelen = brdao.getRegelen(bestelling.getId());
		System.out.println(" Kies u Alstublieft welke bestel regel wilt u wissen ");
		for (BestelRegel regel : regelen) 
			printBestelRegel(regel);
	}
	
	public void printBestelRegel(BestelRegel regel) {
		System.out.println("\t" + regel.getId() + "\t " + regel.getArtikel().getNaam() + "\t\t"
				+ regel.getArtikel().getPrijs() + "\t\t" + regel.getAntaal());
	}

	public void printBestellingen() {
		Set<Bestelling> bestellingen = bdao.getBestellingen();
		System.out.println(" &&&& Bestellingen informatie &&&& ");
		System.out.println("------------------------------------");
		for (Bestelling bestelling : bestellingen) {
			Set<BestelRegel> regelen = brdao.getRegelen(bestelling.getId());
			System.out.println("Bestelling " + bestelling.getId() + " details :");
			System.out.println("Bestelling ID\t   KlantAchternaam\t    TotaalPrijs ");
			System.out.println(" --------------------------------------------------------");
			System.out.println("\t" + bestelling.getId() + "\t\t" + bestelling.getKlant().getAchternaam() + "\t\t"
					+ bestelling.getTotaalPrijs());
			System.out.println(" &&&& Bestelregel informatie &&&& ");
			System.out.println("----------------------------------");
			System.out.println(" Bestel ID\t ArtikelNaam\t\tArtikelPrijs\tAntaal");
			System.out.println(" -------------------------------------------------------------");
			for (BestelRegel regel : regelen) {
				printBestelRegel(regel);
			}
		}
	}

}
