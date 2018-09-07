package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * @author 0036502252
 *
 */
public class GroupNameBuilder implements NameBuilder {
	/**
	 * The group index to be used.
	 */
	private int groupIndex;
	/**
	 *  If true, append zeros where needed. Otherwise append spaces
	 */
	private boolean appendZeros;
	/**
	 * The minimum number of symbols to append.
	 */
	private int minSymbols;

	/**
	 * Constructs a new {@link GroupNameBuilder}.
	 * @param groupIndex the group index to be used.
	 * @param appendZeros append zeros where needed, otherwise append spaces
	 * @param minSymbols the minimum number of symbols to append
	 */
	public GroupNameBuilder(int groupIndex, boolean appendZeros,
			int minSymbols) {
		this.groupIndex = groupIndex;
		this.appendZeros = appendZeros;
		this.minSymbols = minSymbols;
	}

	/**
	 * Appends text to the {@link NameBuilderInfo} object's stringbuilder
	 * according to the regular expression. If there are 2 arguments in the 
	 * regex, the second argument determines the minimum number of symbols. If 
	 * there is a zero before the minimum number, then zeros are appended where
	 * needed. Spaces are appended otherwise.
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		String str = info.getGroup(groupIndex);

		if (str.length() < minSymbols) {
			int n = minSymbols - str.length();
			info.getStringBuilder()
					.append(nSymbols(n, appendZeros ? '0' : ' '));
		}

		info.getStringBuilder().append(str);
	}

	/**
	 * Generates a string which contains of <code>n</code> consecutive
	 * characters.
	 * 
	 * @param n
	 *            the number of characters to be generated
	 * @param symbol
	 *            the characters to be generated <code>n</code> times
	 * @return the string with <code>n</code> chars
	 */
	private static String nSymbols(int n, char symbol) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(symbol);
		}
		return sb.toString();
	}

}
