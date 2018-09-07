package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Exits from the shell.
 * @author 0036502252
 *
 */
public class ExitShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	String name;
	/**
	 * The description of this command.
	 */
	List<String> description;
	/**
	 * Creates a new {@link ExitShellCommand}.
	 */
	public ExitShellCommand() {
		name = "exit";
		List<String> list = new ArrayList<>();
		list.add("Exits from the shell.");
		description = Collections.unmodifiableList(list);
	}
	
	/**
	 * Exits from the shell by setting its status to TERMINATE.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
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
