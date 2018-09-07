package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * NameBuilder implementations are used in combination with
 * {@link NameBuilderInfo} objects in order to generate new filenames from old
 * filenames, using regular expression and their grouping mechanisms.
 * 
 * @author 0036502252
 *
 */
public interface NameBuilder {
	/**
	 * The main functionality of a NameBuilder lies in this method. It is the
	 * way each NameBuilder object communicates with a NameBuilderInfo object in
	 * order to generate a new filename.
	 * 
	 * @param info
	 *            the {@link NameBuilderInfo} object which talks to this
	 *            NameBuilder object
	 */
	void execute(NameBuilderInfo info);

}
