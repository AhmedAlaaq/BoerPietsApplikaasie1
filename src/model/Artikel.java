package model;

import java.math.BigDecimal;

public class Artikel {
	private int id;
	private String naam;
	private BigDecimal prijs;
	private int voorraad;

	public Artikel() {
		this(0, null, null, 0);
	}

	public Artikel(String naam, BigDecimal prijs, int voorraad) {
		setNaam(naam);
		setPrijs(prijs);
		setVoorraad(voorraad);
	}

	public Artikel(int id, String naam, BigDecimal prijs, int voorraad) {
		setId(id);
		setNaam(naam);
		setPrijs(prijs);
		setVoorraad(voorraad);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public BigDecimal getPrijs() {
		return prijs;
	}

	public void setPrijs(BigDecimal prijs) {
		this.prijs = prijs;
	}

	public int getVoorraad() {
		return voorraad;
	}

	public void setVoorraad(int voorraad) {
		this.voorraad = voorraad;
	}

	public String toString() {
		String st ="This artikel with id : " + this.getId() + " naam : " + this.getNaam() + " with prijs : "
				+ this.getPrijs() + (this.getVoorraad() > 0 ? this.getVoorraad() + " ( "+ this.getVoorraad() + " ) invoorraad" : "Verkocht");
		return st;
	}

}

