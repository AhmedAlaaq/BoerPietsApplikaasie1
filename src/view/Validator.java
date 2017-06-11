package view;

import java.util.Scanner;
import java.math.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.updateException;

import org.apache.commons.validator.*;
import model.Klant;
import model.KlantAdres;
import model.KlantAdres.KlantAdresBuilder;

public class Validator {

	final Logger logger = LoggerFactory.getLogger(Menu.class);
	Scanner input = new Scanner(System.in);

	public boolean correcteKeus(String keus) {
		boolean isnumber;

		if (keus.matches("[1-5]+"))
			isnumber = true;
		else {
			/*
			 * Als ik een Junit wil gebruiken dan kan ik gewoon de logger.warn
			 * of println verwijderen
			 */
			logger.error(" U moet een passende nummer geven ! Probeer nog een keer ");
			isnumber = false;
		}

		return isnumber;

	}

	public int uitLoggen(String str) {
		int answer = 3;

		if ("Ja".equalsIgnoreCase(str))
			answer = 1;
		else if ("Nee".equalsIgnoreCase(str))
			answer = 2;
		else if (GenericValidator.isBlankOrNull(str))
			answer = 3;
		return answer;

	}

	public boolean inputBigDecimal(String prijs) {
		boolean isbigdecimal;
		if (prijs.matches("[00-99][.][00-99]+"))
			isbigdecimal = true;
		else {
			/*
			 * Als ik een Junit wil gebruiken dan kan ik gewoon de logger.warn
			 * of println verwijderen
			 */
			logger.error(" U moet juiste prijs geven ! Probeer nog een keer ");
			isbigdecimal = false;
		}

		return isbigdecimal;

	}

	public boolean inputStatus(String keus) {
		boolean isnumber;

		if (keus.matches("[1-3]"))

			isnumber = true;
		else {
			/*
			 * Als ik een Junit wil gebruiken dan kan ik gewoon de logger.warn
			 * of println verwijderen
			 */
			logger.error(" U moet een passende keus geven ! Probeer nog een keer ");
			isnumber = false;
		}

		return isnumber;

	}

	public boolean correcteString(String keus) {
		boolean isString;

		if (keus == null || !(keus.matches("[a-zA-Z]+"))) {
			isString = false;
			/*
			 * Als ik een Junit wil gebruiken dan kan ik gewoon de logger.warn
			 * of println verwijderen
			 */
			logger.error(" U moet een passende naam geven ! Probeer nog een keer ");
		} else

			isString = true;

		return isString;

	}
	public boolean correcteInteger(int number) {
		
		boolean isnumber = GenericValidator.minValue(number, 1);

		if (!isnumber) {
			/*
			 * Als ik een Junit wil gebruiken dan kan ik gewoon de logger.warn
			 * of println verwijderen
			 */
			logger.error(" U moet een passende ID geven ! Probeer nog een keer ");
		} 

		return isnumber;

	}


	

	public boolean postCode(String postcode) {
		boolean iscorrect = false;
		try {
			if (postcode.length() <= 6) {
				String digit = postcode.substring(0, 4);
				String letter = postcode.substring(4, 6);
				if ((digit.matches("[1000-9999]+"))
						&& (Character.isLetter(letter.charAt(0)) && Character.isLetter(letter.charAt(1))))
					iscorrect = true;
				else
					iscorrect = false;
			}
		} catch (Exception ex) {

			throw new updateException("U moet een passende postcode geven ! Probeer nog een keer");
		}
		return iscorrect;
	}

	
}
