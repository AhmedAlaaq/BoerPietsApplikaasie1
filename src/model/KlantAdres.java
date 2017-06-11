package model;

public class KlantAdres {
	private final String straatnaam;
	private final String huisnummer;
	private final String toevoeging;
	private final String postcode;
	private final String woonplaats;
	private final int adrestype;

	public KlantAdres(KlantAdresBuilder builder) {
		this.straatnaam = builder.straatnaam;
		this.huisnummer = builder.huisnummer;
		this.toevoeging = builder.toevoeging;
		this.postcode = builder.postcode;
		this.woonplaats = builder.woonplaats;
		this.adrestype = builder.adrestype;
	}

	public static class KlantAdresBuilder {
		private String straatnaam;
		private String huisnummer;
		private String toevoeging;
		private String postcode;
		private String woonplaats;
		private int adrestype;

		public KlantAdresBuilder() {
		}

		public KlantAdresBuilder straatNaam(String straatnaam) {
			this.straatnaam = straatnaam;
			return this;
		}

		public KlantAdresBuilder huisNummer(String huisnummer) {
			this.huisnummer = huisnummer;
			return this;
		}

		public KlantAdresBuilder toevoeging(String toevoeging) {
			this.toevoeging = toevoeging;
			return this;
		}

		public KlantAdresBuilder postCode(String postcode) {
			this.postcode = postcode;
			return this;
		}

		public KlantAdresBuilder woonplaats(String woonplaats) {
			this.woonplaats = woonplaats;
			return this;
		}

		public KlantAdresBuilder adresType(int adrestype) {
			this.adrestype = adrestype;
			return this;
		}

		public KlantAdres getAdres() {
			return new KlantAdres(this);
		}
	}

	public String getStraatnaam() {
		return straatnaam;
	}

	public String getHuisnummer() {
		return huisnummer;
	}

	public String getToevoeging() {
		return toevoeging;
	}

	public String getPostocde() {
		return postcode;
	}

	public String getWoonplaats() {
		return woonplaats;
	}

	public int getAdrestype() {
		return adrestype;
	}
}
