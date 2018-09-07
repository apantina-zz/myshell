package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Lists all of the directories that the user had previously put on the 
 * environment's internal path stack.
 * @author 0036502252
 *
 */
public class ListdShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link ListdShellCommand}.
	 */
	public ListdShellCommand() {
		this.name = "listd";
		this.description = new ArrayList<>();
		description.add("Lists the directories situated on the stack.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData(MyShell.STACK_KEY);
		if(stack.isEmpty()) {
			env.writeln("No directories have been saved.");
			return ShellStatus.CONTINUE;
		}
		stack.forEach(path->env.writeln(path.toAbsolutePath().toString()));
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
