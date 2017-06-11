package test;
import view.Validator;
import static org.junit.Assert.*;

import org.junit.Test;

public class ValidatorTest {
 Validator validator = new Validator();
	@Test
	public void testCorrecteKeus() {
		String correcteInt = "5";
		assertTrue(validator.correcteKeus(correcteInt));
		String oncorrecteInt = "k1fn5dk4kd";
		assertFalse(validator.correcteKeus(oncorrecteInt));
		
	}

	@Test
	public void testUitLoggen() {
		
		String jaStr = "ja";
		assertEquals(1, validator.uitLoggen(jaStr));
		String neeStr = "nee";
		assertEquals(2, validator.uitLoggen(neeStr));
		String anders = "Alstublieft";
		assertEquals(3, validator.uitLoggen(anders));
	}
	
	@Test
	public void testInputBigDecimal() {
		String correcteBigDecimal ="2.99";
		assertTrue(validator.inputBigDecimal(correcteBigDecimal));
		String oncorrecteBigDecimal = "2,99";
		assertFalse(validator.inputBigDecimal(oncorrecteBigDecimal));
		
	}
	
	@Test
	public void testInputStatus() {
		String correcteStatus = "2";
		assertTrue(validator.inputStatus(correcteStatus));
		String oncorrecteStatus = "34";
		assertFalse(validator.inputStatus(oncorrecteStatus));
		
	}

}

