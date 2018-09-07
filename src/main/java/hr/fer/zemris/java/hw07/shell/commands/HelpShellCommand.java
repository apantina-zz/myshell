package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints information about each supported command.
 * @author 0036502252
 *
 */
public class HelpShellCommand implements ShellCommand {
	/**
	 * The description of this command.
	 */
	List<String> description;
	/**
	 * The name of this command.
	 */
	String name;
	
	/**
	 * Constructs a new {@link HelpShellCommand}.
	 */
	public HelpShellCommand() {
		name = "help";
		List<String> list = new ArrayList<>();
		list.add("If started with no arguments, lists names of all supported commands.");
		list.add("If started with a single argument, prints the name and the description of selected command.");
		description = Collections.unmodifiableList(list);
	}

	/**
	 * If only 'help' is typed in, print all commands. Otherwise, print 
	 * the description of the specified command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments.isEmpty()) {
			System.out.println("Available commands: ");
			env.commands().forEach((name, command)-> System.out.println(name));
		} else {
			ShellCommand c = env.commands().get(arguments);
			if(c == null) {
				System.out.println("Command not found.");
			} else {
				c.getCommandDescription().forEach(System.out::println);
			}
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
