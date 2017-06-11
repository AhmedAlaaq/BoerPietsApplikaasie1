package model;

public class Klant {
	private int id;
	private String voornaam;
	private String achternaam;
	private String tussenvoegsel;
	private KlantAdres klantadres;
	
	public Klant() {
		this(0, null, null, null, null);
	}

	public Klant(String voornaam, String achternaam, String tussenvoegsel, KlantAdres klantadres) {
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setTussenvoegsel(tussenvoegsel);
		setKlantAdres(klantadres);
	}

	public Klant(int id, String voornaam, String achternaam, String tussenvoegsel, KlantAdres klantadres) {
		setId(id);
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setTussenvoegsel(tussenvoegsel);
		setKlantAdres(klantadres);
	}
	public Klant(int id, String voornaam, String achternaam, String tussenvoegsel) {
		setId(id);
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setTussenvoegsel(tussenvoegsel);
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public String getTussenvoegsel() {
		return tussenvoegsel;
	}

	public void setTussenvoegsel(String tussenvoegsel) {
		this.tussenvoegsel = tussenvoegsel;
	}

	public KlantAdres getKlantAdres() {
		return klantadres;
	}

	public void setKlantAdres(KlantAdres klantadres) {
		this.klantadres = klantadres;
	}

	@Override
	public String toString() {
		return "Klant [id=" + id + ", voornaam=" + voornaam + ", achternaam=" + achternaam + ", tussenvoegsel="
				+ tussenvoegsel + "]";
	}
	
} 
