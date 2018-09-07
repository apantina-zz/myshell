package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Pops a directory from the shell's directory stack, and then sets the current
 * directory of the shell from the popped directory.
 * 
 * @author 0036502252
 *
 */
public class PopdShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Creates a new {@link PopdShellCommand}.
	 */
	public PopdShellCommand() {
		this.name = "popd";
		this.description = new ArrayList<>();
		description.add("Pops a directory from the shell's directory stack.");
		description.add(
				"Then it sets the current directory to the popped directory.");
	}

	/**
	 * Executes the given command.
	 * 
	 * @param env
	 *            the shell's environment
	 * @param arguments
	 *            unused
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData(MyShell.STACK_KEY);

		if (stack == null || stack.isEmpty()) {
			env.writeln("Error: Stack is empty.");
			return ShellStatus.CONTINUE;
		}

		Path path = stack.pop();

		if (path.toFile().isDirectory()) {
			env.setCurrentDirectory(path);
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
