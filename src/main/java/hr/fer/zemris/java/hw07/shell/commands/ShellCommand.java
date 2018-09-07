package hr.fer.zemris.java.hw07.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Represents a single shell command used by the MyShell program. Each command
 * must offer these functionalities in order to be supported by the shell.
 * @author 0036502252
 *
 */
public interface ShellCommand {
	/**
	 * Executes the command.
	 * @param env the environment used by the shell running this command
	 * @param arguments the arguments used for execution
	 * @return the new status of the shell
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	/**
	 * @return the name of the command
	 */
	String getCommandName();
	/**
	 * @return the description of the command
	 */
	List<String> getCommandDescription();
}
