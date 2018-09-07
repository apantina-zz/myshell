package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Prints the content of a textual file.
 * 
 * @author 0036502252
 *
 */
public class CatShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	String name;
	/**
	 * The description of this command.
	 */
	List<String> description;

	/**
	 * Constructs a new {@link CatShellCommand}.
	 */
	public CatShellCommand() {
		name = "cat";
		List<String> list = new ArrayList<>();
		list = new ArrayList<>();
		list.add("Opens given file and writes its content to console.");
		list.add("The first argument is path to some file and is mandatory.");
		list.add(
				"The second argument charset name that should be used to interpret chars from bytes.");
		description = Collections.unmodifiableList(list);
	}

	/**
	 * Takes one or two arguments. The first argument is path to some file and
	 * is mandatory. The second argument is charset name that should be used to
	 * interpret chars from bytes. If not provided, a default platform charset
	 * should be used.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		Path p = null;
		try {
			args = new ShellCommandParser(arguments).parsePath();
			p = env.getCurrentDirectory().resolve(args[0].trim());
		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		Charset charset = null;

		if (args.length == 1) {
			charset = Charset.defaultCharset();
		} else if (args.length == 2) {
			try {
				charset = Charset.forName(args[1].trim());
			} catch (IllegalCharsetNameException
					| UnsupportedCharsetException ex) {
				env.writeln("Illegal or unupported charset!");
				return ShellStatus.CONTINUE;
			}

		} else {
			env.writeln(
					"Error! \"cat\" command is defined for 1 or 2 arguments.");
			return ShellStatus.CONTINUE;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(p.toString())),
				charset))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				env.writeln(line);
			}

		} catch (IOException ex) {
			env.writeln("Cannot read from file!");
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
