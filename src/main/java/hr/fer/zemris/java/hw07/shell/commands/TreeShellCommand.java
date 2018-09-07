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
 * Recursively prints a tree, with the given argument as the root.
 * @author 0036502252
 *
 */
public class TreeShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link TreeShellCommand};
	 */
	public TreeShellCommand() {
		name = "tree";
		List<String> list = new ArrayList<>();
		list.add(
				"Takes a single argument – directory – prints a tree (each directory level shifts output two characters to the right).");
		description = Collections.unmodifiableList(list);
	}

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
			generateTree(f, 0, env);
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
	 * Recursively prints prints a tree.
	 * @param dir the root of the directory
	 * @param depth the current depth of the recursion(used for indentation)
	 * @param env the environment used for I/O
	 * @throws IOException 
	 */
	public static void generateTree(File dir, int depth, Environment env) throws IOException {
		if (depth == 0) {
			System.out.println(dir);
		} else {
			String spaces = generateWhitespaces(depth);
			System.out.println(spaces + dir.getName());
		}

		if (dir.listFiles() == null)
			return;

		depth += 2;
		File[] children = dir.listFiles();

		if (children != null) {
			String spaces = generateWhitespaces(depth);
			for (File f : dir.listFiles()) {
				if (f.isFile()) {
					env.writeln(spaces + f.getName());
				} else if (f.isDirectory()) {
					generateTree(f, depth, env);
				}
			}
		}
	}

	/**
	 * Utility method. Generates <code>n</code> whitespaces.
	 * @param n the number of whitespaces to generate
	 * @return a string consisting of <code>n</code> spaces
	 */
	private static String generateWhitespaces(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

}
