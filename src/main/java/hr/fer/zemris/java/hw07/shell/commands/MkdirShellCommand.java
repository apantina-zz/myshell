package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Creates a new directory using the given path.
 * 
 * @author 0036502252
 *
 */
public class MkdirShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link MkdirShellCommand}.
	 */
	public MkdirShellCommand() {
		name = "mkdir";
		ArrayList<String> list = new ArrayList<>();
		list.add(
				"Takes a single argument - directory name, and creates the appropriate directory structure.");

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

		try {
			Files.createDirectories(env.getCurrentDirectory().resolve(path));
		} catch (IOException e) {
			env.writeln("Could not create directory:" + e.getMessage());
		} catch (InvalidPathException ex) {
			env.writeln("Invalid path!");
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
}
