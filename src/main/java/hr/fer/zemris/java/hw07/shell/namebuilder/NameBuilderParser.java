package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.ParserException;

/**
 * Parses grouping tags in regular expressions, and generates
 * {@link NameBuilder} objects accordingly. Groupings tags start with '${', and
 * end with '}'. The parser works similarly to a lazy lexer.
 * 
 * @author 0036502252
 */
public class NameBuilderParser {
	/**
	 * The input string converted to a char array.
	 */
	private char[] data;
	/**
	 * The current index of the parser.
	 */
	private int currentIndex;
	/**
	 * The end of the parsed string.
	 */
	private int end;
	/**
	 * The list of all generated {@link NameBuilder} objects.
	 */
	private List<NameBuilder> builders;

	/**
	 * Constructs a new {@link NameBuilderParser} which parses the given string.
	 * 
	 * @param expression
	 *            the regular expression to be parsed
	 */
	public NameBuilderParser(String expression) {
		this.data = expression.toCharArray();
		this.currentIndex = 0;
		this.end = data.length;
		this.builders = new ArrayList<>();
	}

	/**
	 * Gets the NameBuilder representing the regex's groupings. This parser
	 * namely returns an {@link AggregatingNameBuilder} object, which holds all
	 * other NameBuilders.
	 * 
	 * @return the terminating NameBuilder object
	 */
	public NameBuilder getNameBuilder() {
		parse();

		return new AggregatingNameBuilder(builders);
	}

	// =========================================================================
	// PRIVATE IMPLEMENTATION DETAILS
	// =========================================================================

	/**
	 * Skips whitespaces, and delegates functionality to different methods
	 * according to current index of the parser.
	 */
	private void parse() {
		while (currentIndex < end) {
			if (Character.isWhitespace(data[currentIndex]))
				currentIndex++;

			if (isSubstitutionStart()) {
				groupMethod();
			} else {
				stringMethod();
			}

		}
	}

	/**
	 * If there is no grouping substitution, simply generate a
	 * {@link StringNameBuilder} object.
	 */
	private void stringMethod() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < end && !isSubstitutionStart()) {
			sb.append(data[currentIndex++]);
		}

		builders.add(new StringNameBuilder(sb.toString()));
	}

	/**
	 * When inside a grouping substitution, generate a {@link GroupNameBuilder}
	 * object according to the contents of the grouping substitution.
	 * 
	 * @throws NumberFormatException
	 * @throws ParserException
	 */
	private void groupMethod() throws NumberFormatException, ParserException {
		currentIndex += 2; // skip the ${ symbols
		StringBuilder sb = new StringBuilder();
		while (currentIndex < end && !isSubstitutionEnding()) {
			if(Character.isWhitespace(data[currentIndex])) {
				currentIndex++;
				continue;
			}
			sb.append(data[currentIndex++]);
		}
		String[] args = sb.toString().split(",");
		currentIndex++; // skip the } symbol
		
		
		
		if (args.length == 1) {
			int groupIndex = 0;
			try {
				groupIndex = Integer.parseInt(args[0].trim());

			} catch (NumberFormatException ex) {
				throw new ParserException(
						"Error while parsing ints: " + args[0], ex.getCause());
			}

			builders.add(new GroupNameBuilder(groupIndex, false, 0));

		} else if (args.length == 2) {
			int groupIndex;
			int minSymbols;

			try {
				groupIndex = Integer.parseInt(args[0].trim());
				minSymbols = Integer.parseInt(args[1].trim());
			} catch (NumberFormatException ex) {
				throw new ParserException(
						"Error while parsing ints: " + args[0] + args[1],
						ex.getCause());
			}
			builders.add(new GroupNameBuilder(groupIndex,
					args[1].startsWith("0"), minSymbols));
		} else {
			throw new ParserException("Invalid number of arguments!");
		}

	}

	/**
	 * @return true if the parser encounter the end of a group substitution
	 */
	private boolean isSubstitutionEnding() {
		String str = String.copyValueOf(data).substring(currentIndex);
		return str.startsWith("}");
	}

	/**
	 * @return true if the parser encounter the start of a group substitution
	 */
	private boolean isSubstitutionStart() {
		String str = String.copyValueOf(data).substring(currentIndex);
		return str.startsWith("${");
	}
}
