package hr.fer.zemris.java.hw07.shell.commands;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Copies an entire directory, and all of its contents, to another directory.
 * 
 * @author 0036502252
 *
 */
public class CptreeShellCommand implements ShellCommand {

	/**
	 * This command's description.
	 */
	private List<String> description;
	/**
	 * The name of this command.
	 */
	private String name;

	/**
	 * Constructs a new {@link CptreeShellCommand}.
	 */
	public CptreeShellCommand() {
		this.name = "cptree";
		this.description = new ArrayList<>();
		description.add("Copies a directory from one path to another.");
		description.add("The first argument is the source directory,");
		description.add("and the second argument is the destination.");
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

		Path src;
		Path dest;
		try {
			src = env.getCurrentDirectory().resolve(args[0].trim());
			dest = env.getCurrentDirectory().resolve(args[1].trim());
		} catch (InvalidPathException ex) {
			env.writeln("Invalid path!");
			return ShellStatus.CONTINUE;
		}

		if (!Files.exists(dest) && dest.getParent() != null
				&& Files.exists(dest.getParent())) {
			try {
				Files.createDirectory(dest);
			} catch (IOException e) {
				env.writeln("Error creating new directory.");
			}
		} else if (!Files.exists(dest) && dest.getParent() != null
				&& !Files.exists(dest.getParent())) {
			env.writeln("Error! Invalid directory!");
			return ShellStatus.CONTINUE;
		}

		try {
			Files.walkFileTree(src, new MyCopyVisitor(dest));
		} catch (IOException e) {
			env.writeln("Could not copy the directory.");
			e.printStackTrace();
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Custom implementation of a {@link FileVisitor} which copies a directory
	 * from a source path to a destination path.
	 * 
	 * @author 0036502252
	 *
	 */
	static class MyCopyVisitor extends SimpleFileVisitor<Path> {
		/**
		 * The destination path.
		 */
		Path dest;

		/**
		 * Constructs a new {@link MyCopyVisitor}.
		 * 
		 * @param dest
		 *            the desired destination path where the source folder will
		 *            be copied
		 */
		public MyCopyVisitor(Path dest) {
			this.dest = dest;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			dest = dest.resolve(dir.getFileName());
			Files.createDirectory(dest);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			Path target = dest.resolve(file.getFileName());
			Files.copy(file, target, StandardCopyOption.COPY_ATTRIBUTES);

			System.out.println("Copied file " + file + " to path "
					+ dest.resolve(file.getFileName()));

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {

			dest = dest.getParent();
			return FileVisitResult.CONTINUE;
		}
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