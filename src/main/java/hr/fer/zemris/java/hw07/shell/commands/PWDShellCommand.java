package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.ShellCommand;

/**
 * PWD - Print Working Directory. Prints the current directory used by the
 * shell.
 * 
 * @author 0036502252
 *
 */
public class PWDShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link PWDShellCommand}.
	 */
	public PWDShellCommand() {
		this.name = "pwd";
		this.description = new ArrayList<>();
		description.add("Prints the working directory to the shell.");
	}

	/**
	 * Prints the current working directory to the shell's output stream.
	 * 
	 * @param env
	 *            the shell's environment
	 * @param arguments
	 *            unused
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		env.writeln(env.getCurrentDirectory().toString());

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
