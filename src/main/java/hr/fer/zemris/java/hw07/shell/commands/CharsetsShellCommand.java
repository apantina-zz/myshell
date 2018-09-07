package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints all available charsets for the platform running this shell.
 * @author 0036502252
 *
 */
public class CharsetsShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	String name;
	/**
	 * The description of this command.
	 */
	List<String> description;

	/**
	 * Constructs a new {@link CharsetsShellCommand}.
	 */
	public CharsetsShellCommand() {
		name = "charsets";
		List<String> list = new ArrayList<>();
		list.add(
				"Takes no arguments, and lists names of supported charsets for your Java platform.");
		list.add("A single charset name is written per line.");
		description = Collections.unmodifiableList(list);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		System.out.println("Available charsets:");
		Charset.availableCharsets()
				.forEach((name, charset) -> System.out.println(name));
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
}
