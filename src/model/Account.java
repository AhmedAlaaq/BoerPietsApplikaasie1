package model;

public class Account {
	private int id;
	private String naam;
	private String wachtWoord;
	private int accountStatus;

	public Account() {
		this(0, null, null, 0);
	};

	public Account(String naam, String wachtWoord, int accountStatus) {
		setNaam(naam);
		setWachtWoord(wachtWoord);
		setAccountStatus(accountStatus);
	}

	public Account(int id, String naam, String wachtWoord, int accountStatus) {
		setId(id);
		setNaam(naam);
		setWachtWoord(wachtWoord);
		setAccountStatus(accountStatus);
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

	public String getWachtWoord() {
		return wachtWoord;
	}

	public void setWachtWoord(String wachtWoord) {
		this.wachtWoord = wachtWoord;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

}

