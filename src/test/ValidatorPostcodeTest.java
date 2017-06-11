package test;

import view.Validator;
import static org.junit.Assert.*;
import org.junit.Test;

public class ValidatorPostcodeTest {

	Validator validator = new Validator();

	@Test
	public void testCorrectePostcode() {
		String correctePost = "3765ca";
		assertTrue(validator.postCode(correctePost));
		String oncorrectePost = "549fgh54";
		assertFalse(validator.postCode(oncorrectePost));

	}

}