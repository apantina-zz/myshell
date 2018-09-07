package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pushes the current working directory to the environment's internal stack, and
 * sets the current directory to the first argument of the command, which should
 * be the path to a directory.
 * 
 * @author 0036502252
 *
 */
public class PushdShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link PushdShellCommand}.
	 */
	public PushdShellCommand() {
		this.name = "pushd";
		this.description = new ArrayList<>();
		description.add("Pushes the current directory on the stack.");
		description.add(
				"Then it sets the current directory to the directory in the 1st argument.");
	}

	/**
	 * Executes the given command.
	 * 
	 * @param env
	 *            the environment of the shell
	 * @param arguments
	 *            the directory to be pushed on the shell's internal stack
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String str;
		try {
			str = new ShellCommandParser(arguments).parsePath()[0].trim();

		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		Path path = env.getCurrentDirectory().resolve(str);
		if (!path.toFile().isDirectory()) {
			env.writeln("Path must be an existing directory!");
			return ShellStatus.CONTINUE;
		}

		// create a new stack in the environment's shared data map
		if (env.getSharedData(MyShell.STACK_KEY) == null) {
			Stack<Path> stack = new Stack<>();
			env.setSharedData(MyShell.STACK_KEY, stack);
		}

		// access the stack, push the current directory on the stack
		@SuppressWarnings("unchecked")
		Stack<Path> modified = (Stack<Path>) env
				.getSharedData(MyShell.STACK_KEY);
		modified.push(env.getCurrentDirectory());
		env.setSharedData(MyShell.STACK_KEY, modified);

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
