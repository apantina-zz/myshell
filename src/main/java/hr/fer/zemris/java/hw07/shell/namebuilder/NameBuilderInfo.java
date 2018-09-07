package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.regex.Matcher;

/**
 * Creates a new filename using a grouping mechanism (usually from the 
 * {@link Matcher} class) and a {@link StringBuilder}.
 * @author 0036502252
 *
 */
public interface NameBuilderInfo {
	/**
	 * @return the class's {@link StringBuilder}
	 */
	StringBuilder getStringBuilder();

	/**
	 * @param index the index of the group 
	 * @return the substring in the regex's group
	 */
	String getGroup(int index);

}
