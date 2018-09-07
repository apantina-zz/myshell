package hr.fer.zemris.java.hw07.shell;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Parses a command from the shell. Implemented as a 'lazy' lexer which reads a
 * string character by character.
 * 
 * @author 0036502252
 *
 */
public class ShellCommandParser {
	/**
	 * The input string converted to a char array.
	 */
	private char[] data;
	/**
	 * The current index of the parser.
	 */
	private int currentIndex;
	/**
	 * The length of the data array. Used solely for convenience.
	 */
	private int end;
	/**
	 * The arguments which will be returned as an array.
	 */
	private ArrayList<String> arguments;

	/**
	 * Initializes the parser with the string to be parsed.
	 * 
	 * @param str
	 *            the string to be parsed
	 */
	public ShellCommandParser(String str) {
		Objects.requireNonNull(str);
		data = str.trim().toCharArray();
		end = data.length;
		currentIndex = 0;
		arguments = new ArrayList<>();
	}

	/**
	 * Parses the path according to the shell's regulations.
	 * 
	 * @return
	 */
	public String[] parsePath() {
		while (currentIndex < end) {
			if(Character.isWhitespace(data[currentIndex])) currentIndex++;
			
			if (data[currentIndex] == '\"') {
				quotationMethod();
			} else {
				normalMethod();
			}
		}
		if (data.length == 0) {
			return null;
		}
		return arguments.stream().map(s->s.trim()).toArray(String[]::new);
		
	}

	// =========================================================================
	// PRIVATE IMPLEMENTATION DETAILS
	// =========================================================================

	/**
	 * The parser is in this mode when no quotation mark is present. No spaces
	 * are allowed.
	 */
	private void normalMethod() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < end
				&& !Character.isWhitespace(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}

		arguments.add(sb.append(" ").toString());
		sb.setLength(0);
		currentIndex++; // skip the whitespace separating multiple paths

	}

	/**
	 * The parser is in this mode when a quotation mark is present. Spaces
	 * and escaping are allowed.
	 */
	private void quotationMethod() {
		StringBuilder sb = new StringBuilder();
		currentIndex++; // skip the first " character
		while (currentIndex < end && data[currentIndex] != '\"') {
			if (data[currentIndex] == '\\') {
				if (currentIndex < end && (data[currentIndex + 1] == '\"'
						|| data[currentIndex + 1] == '\\')) {
					currentIndex++; // skip the first '\' symbol
				}
			}
			sb.append(data[currentIndex++]);
		}

		currentIndex++; // skip the terminating " mark

		arguments.add(sb.toString()); //add to the list of arguments
		sb.setLength(0);

		/*
		 * "After the ending double-quote, either no more characters must be
		 * present or at least one space character must be present: strings like
		 * "C:\fi le".txt are invalid and argument parser should report an
		 * error."
		 */
		if (currentIndex < end
				&& !Character.isWhitespace(data[currentIndex])) {
			throw new ParserException("Illegal sequence!");
		}
	}
}
