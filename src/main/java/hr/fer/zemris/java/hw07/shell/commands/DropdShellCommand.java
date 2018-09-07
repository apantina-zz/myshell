package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Drops a directory from the environment's internal stack. Unlike the 
 * {@link PopdShellCommand}, the dropped directory is then simply discarded.
 * @author 0036502252
 *
 */
public class DropdShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link DropdShellCommand}.
	 */
	public DropdShellCommand() {
		this.name = "dropd";
		this.description = new ArrayList<>();
		description.add("Drops a directory from the internal stack.");
		
	}


	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData(MyShell.STACK_KEY);
			
		if(stack == null || stack.isEmpty()) {
			env.writeln("Error: Stack is empty.");
			return ShellStatus.CONTINUE;
		
		}
		
		stack.pop();
		env.setSharedData(MyShell.STACK_KEY, stack);
		
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
