package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Copies a file from from the first argument to the directory from the second
 * argument, or to the same directory as the original file with a different name
 * if the user specifies a file instead of the directory.
 * 
 * @author 0036502252
 *
 */
public class CopyShellCommand implements ShellCommand {
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constant used in a buffer.
	 */
	private final int FOUR_KB = 4096;

	/**
	 * Constructs a new {@link CopyShellCommand}.
	 */
	public CopyShellCommand() {
		name = "copy";
		ArrayList<String> list = new ArrayList<>();
		list.add("Expects two arguments: source file name and destination file "
				+ "name (i.e. paths and names).");
		list.add("If the destination file already exists, the user is prompted "
				+ "if they wish to overwrite.");
		list.add("If the second argument is a directory, the file is copied to "
				+ "the directory using the original file name.");
		list.add("Only works with files.");

		description = Collections.unmodifiableList(list);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		try {
			args = new ShellCommandParser(arguments).parsePath();

		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		if (args.length != 2) {
			env.writeln("Error! Copy command expects 2 arguments.");
			return ShellStatus.CONTINUE;
		}

		Path input;
		Path output;
		try {
			input = env.getCurrentDirectory().resolve(args[0].trim());
			output = env.getCurrentDirectory().resolve(args[1].trim());
		} catch (InvalidPathException ex) {
			env.writeln("Invalid path!");
			return ShellStatus.CONTINUE;
		}

		if (output.toFile().isFile()) {
			String line;
			
			do {
				env.write("Destination file " + output.getFileName().toString()
						+ " already exists. Do you wish to overwrite? (y, n)");
				line = env.readLine().toLowerCase();

			} while (!line.equals("y") && !line.equals("n"));

			if (line.equals("n")) {
				return ShellStatus.CONTINUE;
			}
		}

		// if the second argument is a directory, copy file using the same name:
		if (output.toFile().isDirectory()) {
			output = new File(output.toFile(), input.getFileName().toString())
					.toPath();
		}

		try (InputStream is = new BufferedInputStream(
				new FileInputStream(input.toString()));
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(output.toString()))) {

			byte[] buf = new byte[FOUR_KB];
			while (true) {
				int r = is.read(buf);
				if (r < 1)
					break;
				os.write(buf, 0, r);
			}

		} catch (IOException e) {
			env.writeln("Error during writing.");
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
