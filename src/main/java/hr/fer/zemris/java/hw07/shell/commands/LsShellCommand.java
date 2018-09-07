package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Non-recursively lists all the child directories of the specified path.
 * 
 * @author 0036502252
 *
 */
public class LsShellCommand implements ShellCommand {
	/**
	 * Appended to the start of the string if the file is executable.
	 */
	private static final String IS_EXECUTABLE = "x";
	/**
	 * Appended to the start of the string if the file is executable.
	 */
	private static final String IS_WRITABLE = "w";
	/**
	 * Appended to the start of the string if the file is executable.
	 */
	private static final String IS_READABLE = "r";
	/**
	 * Appended to the start of the string if the file is executable.
	 */
	private static final String IS_DIRECTORY = "d";
	/**
	 * Appended to the start of the string if a condition is not satisfied.
	 */
	private static final String NOT_SATISFIED = "-";
	/**
	 * The name of this command.
	 */
	private String name;
	/**
	 * The description of this command.
	 */
	private List<String> description;

	/**
	 * Constructs a new {@link LsShellCommand}.
	 */
	public LsShellCommand() {
		name = "ls";
		List<String> list = new ArrayList<>();
		list.add(
				"Takes a single argument – directory – and writes a directory listing (not recursive).");
		list.add(
				"Output is formatted as: attributes, size, date created, and file name.");
		description = Collections.unmodifiableList(list);
	}

	/**
	 * The output consists of 4 columns. First column indicates if current
	 * object is directory (d), readable (r), writable (w) and executable (x).
	 * Second column contains object size in bytes that is right aligned and
	 * occupies 10 characters. Follows file creation date/time and finally file
	 * name.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		File file;
		String pathName = null;
		try {
			pathName = new ShellCommandParser(arguments).parsePath()[0].trim();

		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		try {
			file = env.getCurrentDirectory().resolve(pathName).toFile();
		} catch (InvalidPathException ex) {
			env.writeln("Invalid path!");
			return ShellStatus.CONTINUE;
		}

		if (!file.isDirectory()) {
			env.writeln("The path " + arguments
					+ " does not represent a directory.");
			return ShellStatus.CONTINUE;
		}

		try {
			File[] children = file.listFiles();
			StringBuilder sb = new StringBuilder();
			for (File f : children) {
					Path path = f.toPath();
					sb.append(f.isDirectory() ? IS_DIRECTORY : NOT_SATISFIED)
							.append(f.canRead() ? IS_READABLE : NOT_SATISFIED)
							.append(f.canWrite() ? IS_WRITABLE : NOT_SATISFIED)
							.append(f.canExecute() ? IS_EXECUTABLE : NOT_SATISFIED)
							.append(' ');

					long size = f.length();
					sb.append(String.format("%10d", size)).append(' ');

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					BasicFileAttributeView faView = Files.getFileAttributeView(
							path, BasicFileAttributeView.class,
							LinkOption.NOFOLLOW_LINKS);
					BasicFileAttributes attributes = faView.readAttributes();
					FileTime fileTime = attributes.creationTime();
					String formattedDateTime = sdf
							.format(new Date(fileTime.toMillis()));
					sb.append(formattedDateTime).append(' ')
							.append(f.getName());

					env.writeln(sb.toString());

					sb.setLength(0);
				
			}
		} catch (IOException e) {
			env.writeln("Error during listing.");
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