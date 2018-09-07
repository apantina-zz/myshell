package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.ShellCommand;

/**
 * Sets the current directory to the given argument.
 * 
 * @author 0036502252
 *
 */
public class CDShellCommand implements ShellCommand {
	/**
	 * The name of the command.
	 */
	private String name;
	/**
	 * The description of the command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link CDShellCommand}.
	 */
	public CDShellCommand() {
		this.name = "cd";
		this.description = new ArrayList<>();
		description.add("Sets the current directory to the given argument.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String str = "";
		try {
			str = new ShellCommandParser(arguments).parsePath()[0];

		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		Path path = env.getCurrentDirectory().resolve(str.trim());

		env.setCurrentDirectory(path);

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
