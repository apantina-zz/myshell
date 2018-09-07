package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Support for getting and replacing symbols used in the shell. Implemented
 * as a {@link ShellCommand}.
 * @author 0036502252
 *
 */
public class SymbolShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	String name;
	/**
	 * The description of this command.
	 */
	List<String> description;

	/**
	 * Constructs a new {@link SymbolShellCommand}.
	 */
	public SymbolShellCommand() {
		name = "symbol";
		List<String> list = new ArrayList<>();
		list.add("Can be run with one argument, which is symbol name.");
		list.add("In that case, the symbol for the given name is returned.");
		list.add("When the command is run with 2 more arguments, the symbol"
				+ " is replaced with the symbol from the 2nd argument.");
		description = Collections.unmodifiableList(list);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		//lots of redundancy, I know.
		
		String[] symArguments = arguments.split(" ");
		if (symArguments.length == 2) {
			switch (symArguments[1]) {
			case "PROMPT":
				env.writeln("Symbol for PROMPT is " + "'"
						+ env.getPromptSymbol() + "'");
				break;
			case "MORELINES":
				env.writeln("Symbol for MORELINES is " + "'"
						+ env.getMorelinesSymbol() + "'");
				break;
			case "MULTILINE":
				env.writeln("Symbol for MULTILINE is " + "'"
						+ env.getMultilineSymbol() + "'");
				break;
			default:
				env.writeln("The symbol for '" + symArguments[1]
						+ "' is not defined.");
				break;
			}
		} else if (symArguments.length == 3) {
			if (symArguments[2].length() != 1) {
				env.writeln(symArguments[2] + " is not a valid symbol!");
			}

			Character ch = symArguments[2].toCharArray()[0];

			switch (symArguments[1]) {
			case "PROMPT":
				env.writeln("Symbol for PROMPT changed from '"
						+ env.getPromptSymbol() + "' to '" + ch + "'");
				env.setPromptSymbol(ch);

				break;
			case "MORELINES":
				env.writeln("Symbol for MORELINES changed from '"
						+ env.getMorelinesSymbol() + "' to '" + ch + "'");
				env.setMorelinesSymbol(ch);

				break;
			case "MULTILINE":
				env.writeln("Symbol for MULTILINE changed from '"
						+ env.getMultilineSymbol() + "' to '" + ch + "'");
				env.setMultilineSymbol(ch);
				break;
			default:
				env.writeln("The symbol " + ch + " is not defined.");
				break;
			}

		} else {
			env.writeln("Invalid command! The command \"symbol\" expects "
					+ "1 or 2 more arguments, separated by spaces!");
		}

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
