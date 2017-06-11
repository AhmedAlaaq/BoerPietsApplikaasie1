package model;

import java.math.BigDecimal;

public class BestelRegel {
	private int id = 0;
	private Artikel artikel = new Artikel();
	private Bestelling bestel = new Bestelling();
	private int antaal = 0;
	private BigDecimal regelPrijs;

	public BestelRegel() {

	}

	public BestelRegel(Artikel artikel, int antaal) {
		setArtikel(artikel);
		setBestel(bestel);
	}

	public BestelRegel(Artikel artikel, Bestelling bestel, int antaal) {
		setArtikel(artikel);
		setBestel(bestel);
		setAntaal(antaal);
	}

	public BestelRegel(int id, Artikel artikel, Bestelling bestel, int antaal, BigDecimal regelPrijs) {
		setId(id);
		setArtikel(artikel);
		setBestel(bestel);
		setAntaal(antaal);
		setRegelPrijs(regelPrijs);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	public Bestelling getBestel() {
		return bestel;
	}

	public void setBestel(Bestelling bestel) {
		this.bestel = bestel;
	}

	public int getAntaal() {
		return antaal;

	}

	public void setAntaal(int antaal) {
		this.antaal = antaal;
	}

	public BigDecimal getRegelPrijs() {
		return regelPrijs;
	}

	public void setRegelPrijs(BigDecimal regelPrijs) {
		this.regelPrijs = regelPrijs;
	}

	public BigDecimal bestelregelprijs(Artikel artikel, int antaal) {
		BigDecimal regelPrijs = null;
		regelPrijs = artikel.getPrijs().multiply(BigDecimal.valueOf(antaal));
		return regelPrijs;
	}

	@Override
	public String toString() {
		return "BestelRegel [id=" + id + ", artikel=" + artikel + ", bestel=" + bestel + ", antaal=" + antaal + "]";
	}

}
