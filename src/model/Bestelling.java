package model;

import java.math.*;
import java.util.HashSet;
import java.util.Set;

public class Bestelling {
	private int id = 0;
	private Klant klant = null;
	private BigDecimal totaalPrijs;
	private Set<BestelRegel> regelSet = new HashSet<>();

	public Bestelling() {

	}

	public Bestelling(Klant klant, BigDecimal totalPrijs) {
		setKlant(klant);
		setTotaalPrijs(totalPrijs);
	}

	public Bestelling(int id, BigDecimal totalPrijs, Klant klant) {
		setId(id);
		setKlant(klant);
		setTotaalPrijs(totalPrijs);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Klant getKlant() {
		return klant;
	}

	public void setKlant(Klant klant) {
		this.klant = klant;
	}

	public BigDecimal getTotaalPrijs() {
		return totaalPrijs;
	}

	public void setTotaalPrijs(BigDecimal totaalprijs) {
		this.totaalPrijs = totaalprijs;
	}

	public BigDecimal Totaalprijs(Set<BestelRegel> regelSet) {

		BigDecimal totaalPrijs = BigDecimal.ZERO;

		for (BestelRegel bestelregel : regelSet) {
			totaalPrijs = totaalPrijs.add(bestelregel.getRegelPrijs());
		}
		this.totaalPrijs = totaalPrijs;
		return totaalPrijs;
	}

	@Override
	public String toString() {
		return "Bestelling details is [id=" + id + ", klant=" + klant + ", totaalPrijs=" + totaalPrijs + "]";
	}

}
