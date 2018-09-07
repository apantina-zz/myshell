package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * A shell command which recursively deletes a directory and all its contents.
 * 
 * @author 0036502252
 *
 */
public class RmtreeShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link RmtreeShellCommand};
	 */
	public RmtreeShellCommand() {
		name = "rmtree";
		List<String> list = new ArrayList<>();
		list.add(
				"Takes a single argument – directory - and deletes all of its contents.");
		description = Collections.unmodifiableList(list);
	}

	/**
	 * Takes a single argument, which is the path to a directory, and 
	 * deletes it.
	 * @param env the shell environment
	 * @param arguments the arguments which the user sends to the shell
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String path = null;
		try {
			path = new ShellCommandParser(arguments).parsePath()[0].trim();

		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		File f = env.getCurrentDirectory().resolve(path).toFile();

		if (!f.isDirectory()) {
			env.writeln("The path " + f.toPath()
					+ " does not represent a directory.");
			return ShellStatus.CONTINUE;
		}

		try {
			traverse(f);
		} catch (IOException ex) {
			env.writeln("Could not generate tree: " + ex.getMessage());
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

	/**
	 * Recursively deletes a folder and all of its contents.
	 * 
	 * @param dir
	 *            the root of the directory
	 * @throws IOException
	 */
	public static void traverse(File dir) throws IOException {
		if (dir.listFiles() == null)
			return;

		File[] children = dir.listFiles();

		if (children != null) {
			for (File f : dir.listFiles()) {

				if (f.isFile()) {
					f.delete();

				} else if (f.isDirectory()) {
					traverse(f);
				}
			}
		}
		dir.delete(); // delete the root folder when done
	}
}
