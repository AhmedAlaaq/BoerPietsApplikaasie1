package controller;

import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.Server;
import model.*;
import view.Menu;
import view.Validator;

public class BestellingControllerMetServer {
	
	private Server server;
	private Validator validator = new Validator();
	private Artikel artikel;
	private Klant klant;
	private Connection connection;
	private boolean goedKeus = false;
	private Scanner input = new Scanner(System.in);
	final Logger logger = LoggerFactory.getLogger(Menu.class);

	public BestellingControllerMetServer() {
		server = new Server(connection);
	}

	public void insert() {
		Bestelling bestelling = new Bestelling();
		BestelRegel regel = new BestelRegel();

		do {
			System.out.print("Vul de achternaam van de klant in :");
			String achternaam = input.nextLine();
			if (server.wezigKlant(achternaam)) {
				bestelling.setKlant(server.getKlant(achternaam));
				goedKeus = true;
			}
		} while (!(goedKeus));

		bestelling = server.insertBestelling(bestelling);
		regel.setBestel(bestelling);

		System.out.print("Hoeveel artiklen wilt u :");
		int num = input.nextInt();
		for (int i = 0; i < num; i++) {
			System.out.print(" Vul het antaal van kazen in :");
			regel.setAntaal(input.nextInt());
			do {
				System.out.print("Vul de ID van het artikel in :");
				int artikelId = input.nextInt();
				if (validator.correcteInteger(artikelId) && server.wezigArtikel(artikelId)) {
					regel.setArtikel(server.getArtikel(artikelId));
					regel = server.insertBestelRegel(regel);
					goedKeus = true;
				}

			} while (!(goedKeus));
			server.addBestelregel(bestelling);
			System.out.println(bestelling);
		}
	}

	public void delete() {
		Bestelling bestelling = new Bestelling();
		do {
			System.out.print("Vul u het id van de Bestelling in :");
			int id = input.nextInt();
			if (validator.correcteInteger(id)) {
				if (!server.wezigBestelling(id))
					System.out.println(" Deze bestelling is afwijzig ! ");
				else

					server.deleteBestelling(id);
				goedKeus = true;
			}
		} while (!(goedKeus));

	}

	public void update(int id, int keus) {

		BestelRegel regel = new BestelRegel();
		Bestelling bestelling = new Bestelling();

		if (!server.wezigBestelling(id))
			System.out.println(" De bestelling is afwijzig ! ");
		else {
			bestelling = server.getBestelling(id);
			boolean isJuist;
			switch (keus) {
			case 1:
				do {
					System.out.print("Vul de ID van het artikel in :");
					int artikelId = input.nextInt();
					if (validator.correcteInteger(artikelId) && server.wezigArtikel(artikelId)) {
						artikel = server.getArtikel(artikelId);
						regel.setArtikel(artikel);
						goedKeus = true;
					}
				} while (!(goedKeus));
				System.out.print("Vul het antaal van kazen voor het nieuwe bestelregel in :");
				regel.setAntaal(input.nextInt());
				regel.setBestel(server.getBestelling(id));
				regel = server.insertBestelRegel(regel);
				bestelling.setTotaalPrijs(bestelling.Totaalprijs(server.getRegelen(id)));
				server.addBestelregel(bestelling);
				break;
			case 2:
				do {
					printRegelenVanBestelling(id);
					System.out.print("Vul de ID van het Bestel regel in :");
					int bestelregelId = input.nextInt();
					if (validator.correcteInteger(bestelregelId) && server.wezigRegel(bestelregelId)) {
						server.deleteBestelregel(server.getBestelRegel(bestelregelId), server.getBestelling(id));
						goedKeus = true;
					}
				} while (!(goedKeus));

				break;
			case 3:

				do {
					System.out.print("Vul de ID van het Bestel regel in :");
					int bestelregelId = input.nextInt();
					if (validator.correcteInteger(bestelregelId) && server.wezigRegel(bestelregelId)) {
						System.out.print("Vul het nieuw aantal van de kazen in :");
						int aantal = input.nextInt();
						if (validator.correcteInteger(aantal))
							server.updateBestelling(id, server.updateBestelregel(bestelregelId, aantal));
						goedKeus = true;
					}
				} while (!(goedKeus));

				break;

			}
		}
	}

	public void printRegelenVanBestelling(int bestelId){
		Bestelling bestelling = server.getBestelling(bestelId);
		Set<BestelRegel> regelen = server.getRegelen(bestelling.getId());
		System.out.println(" Kies u Alstublieft welke bestel regel wilt u wissen ");
		for (BestelRegel regel : regelen) 
			printBestelRegel(regel);
	}
	
	
	public void printBestelRegel(BestelRegel regel) {
		System.out.println("\t" + regel.getId() + "\t " + regel.getArtikel().getNaam() + "\t\t"
				+ regel.getArtikel().getPrijs() + "\t\t" + regel.getAntaal());
	}

	public void printBestellingen() {
		Set<Bestelling> bestellingen = server.getBestellingen();
		System.out.println(" &&&& Bestellingen informatie &&&& ");
		System.out.println("------------------------------------");
		for (Bestelling bestelling : bestellingen) {
			Set<BestelRegel> regelen = server.getRegelen(bestelling.getId());
			System.out.println("Bestelling " + bestelling.getId() + " details :");
			System.out.println("Bestelling ID\t   KlantAchternaam\t    TotaalPrijs ");
			System.out.println(" --------------------------------------------------------");
			System.out.println("\t" + bestelling.getId() + "\t\t" + bestelling.getKlant().getAchternaam() + "\t\t"
					+ bestelling.getTotaalPrijs());
			System.out.println(" &&&& Bestelregel informatie &&&& ");
			System.out.println("----------------------------------");
			System.out.println(" Bestel ID\t ArtikelNaam\t\tArtikelPrijs\tAntaal");
			System.out.println(" -------------------------------------------------------------");
			for (BestelRegel regel : regelen) 
				printBestelRegel(regel);
			
		}
	}
}
