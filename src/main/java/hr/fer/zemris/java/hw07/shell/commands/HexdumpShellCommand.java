package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints the hex representation of the file's bytes. The values are presented
 * in a table.
 * 
 * @author 0036502252
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * The name of the command.
	 */
	private String name;
	/**
	 * The description of the command.
	 */
	private List<String> description;
	/**
	 * Eight hex values are printed per table section
	 */
	private final int COUNTER_LENGTH = 8;
	/**
	 * The number 10 in hexadecimal. Used multiple times throughout this class
	 */
	private final int HEX_10 = 16;
	/**
	 * Values with a byte value below this bound are printed as dots on the
	 * right side of the table.
	 */
	private static final int LOWER_BOUND = 32;
	/**
	 * Values with a byte value above this bound are printed as dots on the
	 * right side of the table.
	 */
	private static final int UPPER_BOUND = 127;

	/**
	 * Constructs a new {@link HexdumpShellCommand}.
	 */
	public HexdumpShellCommand() {
		name = "copy";
		ArrayList<String> list = new ArrayList<>();
		list.add(
				"Expects a single argument: file name, and produces hex-output.");
		list.add(
				"On the right side of the image only a standard subset of characters is shown.");
		list.add("For all other characters a '.' is printed instead.");

		description = Collections.unmodifiableList(list);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String path = null;
		try {
			path = new ShellCommandParser(arguments).parsePath()[0].trim();

		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}
		
		Path p = env.getCurrentDirectory().resolve(path);
		
		if (!p.toFile().isFile()) {
			env.writeln("Expected a file path");
		}
		try (InputStream input = Files.newInputStream(p)) {
			int counter = 0;

			StringBuilder hexes = new StringBuilder();
			StringBuilder symbols = new StringBuilder();

			while (true) {
				int r = input.read();
				if (r < 1) {
					break;
				}

				// print the left side of the table which shows the counter
				String hex = Integer.toHexString(r).toUpperCase();
				hexes.append(hex.length() == 2 ? hex : "0" + hex).append(" ");

				// on the right side of the table: print the hex value if it's
				// within bounds, or print a dot
				symbols.append((r < LOWER_BOUND) || (r > UPPER_BOUND) ? "."
						: String.format("%c", r));
				counter++;

				// separate the table row; 8 by 8 hex values
				if (counter % 8 == 0) {
					hexes.append("| ");
				}

				// print a table row every 16 values
				if (counter % 16 == 0) {
					String hexedCounter = Integer
							.toHexString(counter / HEX_10 * HEX_10 - HEX_10)
							.toUpperCase();
					StringBuilder counterString = new StringBuilder();
					appendMultipleTimes(counterString,
							COUNTER_LENGTH - hexedCounter.length(), "0");
					counterString.append(hexedCounter).append(": ");

					env.writeln(counterString.toString() + hexes.toString()
							+ symbols.toString());

					hexes.setLength(0);
					symbols.setLength(0);
				}
			}

			// check if there are any remaining bytes to print after reading the
			// file, and align the table row accordingly..

			if (counter % HEX_10 != 0) {

				String hexedCounter = Integer
						.toHexString(counter / HEX_10 * HEX_10).toUpperCase();
				StringBuilder counterString = new StringBuilder();
				appendMultipleTimes(counterString,
						COUNTER_LENGTH - hexedCounter.length(), "0");
				counterString.append(hexedCounter).append(": ");

				int faliJos = HEX_10 - counter % HEX_10;

				if (faliJos >= 8) {
					appendMultipleTimes(hexes, 8 - counter % HEX_10, "   ");
					hexes.append("| ");
					appendMultipleTimes(hexes, 8, "   ");
					hexes.append("| ");

				} else {
					appendMultipleTimes(hexes, HEX_10 - counter % HEX_10,
							"   ");
					hexes.append("| ");

				}

				env.writeln(counterString.toString() + hexes.toString()
						+ symbols.toString());

			}

		} catch (IOException e) {
			env.writeln("Error while reading file: " + e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		return description;
	}

	/**
	 * Utility method, used for printing. Appends a StringBuilder with the
	 * desired number of desired strings.
	 * 
	 * @param sb
	 *            the StringBuilder to which the strings are appended
	 * @param n
	 *            the number of times to append <code>str</code>
	 * @param str
	 *            the string to be appended <code>n</code> times
	 * @return the new StringBuilder, with the appended strings
	 * @see StringBuilder
	 */
	private static StringBuilder appendMultipleTimes(StringBuilder sb, int n,
			String str) {
		for (int i = 0; i < n; i++) {
			sb.append(str);
		}
		return sb;
	}

}
