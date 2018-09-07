package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

import hr.fer.zemris.java.hw07.shell.commands.ShellCommand;

/**
 * Represents an environment which the shell uses for input and output.
 * 
 * @author 0036502252
 *
 */
public interface Environment {
	/**
	 * Reads a line from the input stream.
	 * 
	 * @return the read line
	 * @throws ShellIOException
	 *             in case reading goes awry
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes a string on the output stream.
	 * 
	 * @param text
	 *            the string to be written
	 * @throws ShellIOException
	 *             in case writing goes awry
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes a line on the output stream.
	 * 
	 * @param text
	 *            the string to be written
	 * @throws ShellIOException
	 *             in case writing goes awry
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * @return the list of the shell's available commands
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * @return the multiline symbol of the shell
	 */
	Character getMultilineSymbol();

	/**
	 * @param symbol
	 *            the multiline symbol to be set
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * @return the prompt symbol of the shell
	 */
	Character getPromptSymbol();

	/**
	 * @param symbol
	 *            the prompt symbol to be set
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * @return the moreLines symbol of the shell
	 */
	Character getMorelinesSymbol();

	/**
	 * @param symbol
	 *            the moreLines symbol to be set
	 */
	void setMorelinesSymbol(Character symbol);

	/**
	 * @return the environment's current working directory.
	 */
	Path getCurrentDirectory();

	/**
	 * @param path
	 *            the environment's new working directory to be set
	 */
	void setCurrentDirectory(Path path);

	/**
	 * Gets data from the environment's shared data map using the given key.
	 * 
	 * @param key
	 *            the key to be used for data acquisition
	 * @return the object mapped to the given key, or null if no object could be
	 *         found for the corresponding key
	 */
	Object getSharedData(String key);

	/**
	 * Sets the shared data at the given key to the desired object.
	 * 
	 * @param key
	 *            the key at which the new object will be mapped
	 * @param value
	 *            the value to be set
	 */
	void setSharedData(String key, Object value);
}
