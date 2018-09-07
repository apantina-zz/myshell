package hr.fer.zemris.java.hw07.crypto;

/**
 * Utility class, which has two methods used in the 1st part of the 7th Java
 * Course homework.
 * 
 * @author 0036502252
 *
 */
public class Util {
	/**
	 * The digits used in hexadecimal number representation.
	 */
	private static String digits = "0123456789abcdef";

	/**
	 * Generates a byte array using a string representation of concatenated
	 * hexadecimal numbers.
	 * 
	 * @param str
	 *            the string to be converted
	 * @return the array of bytes according to the string's hex representation,
	 * or an zero-length byte array if <code>str</code> is an empty string.
	 * @throws IllegalArgumentException if the string is invalid. The string 
	 * must be a non-null value, and have an even number of characters in 
	 * order to be considered valid. 
	 */
	public static byte[] hexToByte(String str) {
		if (str == null || str.length() % 2 != 0) {
			throw new IllegalArgumentException(
					"Hex encoded string must have an even number of characters!");
		}
		if (str.isEmpty()) return new byte[0];
		byte[] result = new byte[str.length() / 2];
		for (int i = str.length(); i > 0;) {
			result[i / 2 - 1] = (byte) (getDigit(str.charAt(--i))
					| (getDigit(str.charAt(--i)) << 4));
		}
		return result;
	}

	/**
	 * Utility method for the <code><hexToByte</code> method. Gets the numeric 
	 * value of inputted character.
	 * @param c the inputted character
	 * @return the digit representing the character <code>c</code>.
	 * @throws NumberFormatException if the character is invalid
	 */
	private static int getDigit(char c) {
		int digit = Character.digit(c, 16);
		if (digit < 0) {
			throw new NumberFormatException("Invalid character: " + c);
		}
		return digit;
	}

	/**
	 * Generates a string representation of concatenated
	 * hexadecimal numbers, using a byte array of hexadecimal values.
	 * @param bytes the byte array used for conversion
	 * @return the string representation of concatenated hexadecimal numbers,
	 * or an empty string if the byte array is zero-length array.
	 */
	public static String byteToHex(byte[] bytes) {
		if (bytes.length == 0) return "";
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0, n = bytes.length; i < n; i++) {
			int masked = bytes[i] & 0xff;
			sb.append(digits.charAt(masked >> 4));
			sb.append(digits.charAt(masked & 0xf));
		}
		
		return sb.toString();
	}
}
