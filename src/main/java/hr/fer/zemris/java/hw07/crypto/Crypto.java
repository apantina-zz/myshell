package hr.fer.zemris.java.hw07.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Offers decryption, encryption and SHA-256 file digest checking of various
 * file types. Parameters are input via the command line in order to indicate
 * the desired operation, followed by arguments specified by the program
 * interface.
 * <p>
 * The parameters are as follows:
 * <p>
 * <code>checksha</code> - needs to be followed by a path to a file. The
 * specified file will then be able to have its SHA-256 digest checked. The
 * digest used for checking is input via the command line.
 * <p>
 * <code>encrypt</code> - needs to be followed by two arguments - an input file,
 * which is the file to be encrypted, and the output file, which will be the
 * resulting encrypted file. The file is encrypted using a hex-encoded password,
 * and a hex-encoded intialization vector, also provided by the user via the
 * command line.
 * <p>
 * <code>decrypt</code> - needs to be followed by two arguments - an input file,
 * which is the file to be decrypted, and the output file, which will be the
 * resulting decrypted file. The file is decrypted using a hex-encoded password,
 * and a hex-encoded intialization vector, also provided by the user via the
 * command line.
 * <p>
 * 
 * @author 0036502252
 *
 */
public class Crypto {
	/**
	 * One kilobyte. Used when initializing a byte buffer.
	 */
	private static final int ONE_KB = 1024;
	/**
	 * The prompt symbol used in the program's UI.
	 */
	private static final String PROMPT_SYMBOL = "> ";

	/**
	 * Main method of the program. Performs a desired operation based on the command
	 * line parameters.
	 * 
	 * @param args
	 *            the command line arguments, supported ones are
	 *            <code>decrypt, encrypt</code> and <code>checksha</code>.
	 */
	public static void main(String[] args) {
		if (args.length == 2) {

			if (!args[0].equals("checksha")) {
				System.out.println("Invalid arguments. Arguments were: " + args[0] + " " + args[1] + "\nExiting..");
				return;
			}
			checkSHA(args[1]);

		} else if (args.length == 3) {

			if (args[0].equals("encrypt")) {
				encryptDecrypt(args[1], args[2], true);
			} else if (args[0].equals("decrypt")) {
				encryptDecrypt(args[1], args[2], false);
			} else {
				System.out.println("Invalid arguments. Arguments were: " + args[0] + " " + args[1] + " " + args[2]
						+ "\nExiting..");

			}

		} else {
			System.out.println("Invalid number of command line arguments!");
		}
	}

	// =========================================================================
	// ================== PRIVATE IMPLEMENTATION METHODS ======================
	// =========================================================================

	/**
	 * Checks the SHA-256 file digest of a given file.
	 * 
	 * @param filename
	 *            the file to be checked
	 */
	private static void checkSHA(String filename) {
		System.out.println("Please provide expected sha-256 digest for " + filename + " :");
		System.out.print(PROMPT_SYMBOL);

		Scanner sc = new Scanner(System.in);
		String expectedDigest = sc.next();
		sc.close();

		String actualDigest = getDigest(filename);
		if (actualDigest == null) {
			System.out.println("Could not generate digest.");

		} else if (actualDigest.equals(expectedDigest)) {
			System.out.println("Digesting completed. Digest of " + filename + " matches expected digest.");

		} else {
			System.out.println("Digesting completed. Digest of " + filename
					+ " does not match the expected digest.\nDigest was:" + actualDigest);
		}

	}

	/**
	 * Gets the digest of a file, by reading it and using the {@link MessageDigest}
	 * class to generate the byte array representing the hash.
	 * 
	 * @param filename
	 *            the name of the file used for hashing
	 * @return the string hex representation of the byte array digest
	 */
	private static String getDigest(String filename) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");

			try (InputStream is = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
				if (Paths.get(filename).toFile().isFile()) {
					byte[] buff = new byte[ONE_KB];
					while (true) {
						int r = is.read(buff);
						if (r < 1) break;
						sha.update(buff, 0, r);
					}

					byte[] hash = sha.digest();

					String hashedString = Util.byteToHex(hash);

					return hashedString;
				} else {
					System.out.println("Given path is not a file! ");
					return null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Encrypts or decrypts a file based on the operation of choice.
	 * 
	 * @param inputPath
	 *            the path of the file to be encrypted/decrypted
	 * @param outputPath
	 *            the path of the resulting file
	 * @param encrypt
	 *            true if the user desires encryption, false if the user desires
	 *            decryption of the file <code>inputPath</code>
	 */
	private static void encryptDecrypt(String inputPath, String outputPath, boolean encrypt) {
		System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
		System.out.print(PROMPT_SYMBOL);
		Scanner sc = new Scanner(System.in);
		String password = sc.next();

		System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
		System.out.print(PROMPT_SYMBOL);
		String initVector = sc.next();
		sc.close();

		SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(password), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(initVector));
		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		try {
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (InvalidKeyException e) {
			System.out.println("Invalid key!");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Invalid parameter specification!");
		}

		processFile(inputPath, outputPath, cipher);

		System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. Generated file " + outputPath
				+ " based on file " + inputPath + ".");
	}

	/**
	 * Processes a given file. This method is called by the
	 * <code>encryptDecrypt</code> method. It generates an encrypted/decrypted file
	 * based on the cipher's settings.
	 * 
	 * @param input
	 *            the path of the file to be encrypted/decrypted
	 * @param output
	 *            the path of the resulting file
	 * @param cipher
	 *            the cipher used for file encryption/decryption
	 */
	private static void processFile(String input, String output, Cipher cipher) {
		try (InputStream is = Files.newInputStream(Paths.get(input), StandardOpenOption.READ);
				OutputStream os = Files.newOutputStream(Paths.get(output))) {

			byte[] buffer = new byte[ONE_KB];
			while (true) {
				int r = is.read(buffer);
				if (r < 1) break;
				os.write(cipher.update(buffer, 0, r));
			}

			byte[] result = cipher.doFinal();

			os.write(result);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	}
	
}