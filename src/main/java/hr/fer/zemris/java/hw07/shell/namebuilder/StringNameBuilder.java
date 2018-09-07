package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * A primitive {@link NameBuilder} implementation which gets a string, and 
 * appends it to a {@link NameBuilderInfo} object's {@link StringBuilder}.
 * @author 0036502252
 *
 */
public class StringNameBuilder implements NameBuilder {
	/**
	 * The string to be appended to a {@link NameBuilderInfo} object.
	 */
	private String str;

	/**
	 * Constructs a new {@link StringBuilder}.
	 * 
	 * @param str
	 *            the string to be appended to a {@link NameBuilderInfo} object
	 */
	public StringNameBuilder(String str) {
		this.str = str;
	}

	/**
	 * Appends its string to a {@link NameBuilderInfo} object.
	 * @param info the {@link NameBuilderInfo} object used for appending
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		info.getStringBuilder().append(str);
	}
}
