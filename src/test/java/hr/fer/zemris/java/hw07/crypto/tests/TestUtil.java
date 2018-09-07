package hr.fer.zemris.java.hw07.crypto.tests;

import static hr.fer.zemris.java.hw07.crypto.Util.byteToHex;
import static hr.fer.zemris.java.hw07.crypto.Util.hexToByte;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.java.hw07.crypto.Util;

/**
 * Testing class for the methods in the class {@link Util}.
 * @author 0036502252
 *
 */
@SuppressWarnings("javadoc")
public class TestUtil {

	@Test(expected = IllegalArgumentException.class)
	public void invalidHexToByte() {
		hexToByte(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidHexToByte2() {
		hexToByte("0284598rfji0s");
	}

	@Test
	public void emptyStringHexToByte() {
		byte[] b = hexToByte("");
		Assert.assertTrue(b.length == 0);
	}

	@Test
	public void hexToByteTest() {
		byte[] actuals = hexToByte("01aE22");
		byte[] expecteds = new byte[] { 1, -82, 34 };
		Assert.assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void byteToHexTest() {
		String actual = byteToHex(new byte[] { 1, -82, 34 });
		String expected = "01ae22";
		Assert.assertEquals(expected, actual);
	}

	// in the following tests, we expect to get the same thing after
	// converting from a string to a hex, and then back to a string (and vice
	// versa)
	
	@Test
	public void combinedTest() {
		String str = "1883abffa4348234";
		Assert.assertEquals(str, byteToHex(hexToByte(str)));
	}

	@Test
	public void combinedTest2() {
		byte[] arr = new byte[] { 1, 2, 2, 3, 3, 3, 4, 4, -22, 3, 2 };
		Assert.assertArrayEquals(arr, hexToByte(byteToHex(arr)));
	}

}
