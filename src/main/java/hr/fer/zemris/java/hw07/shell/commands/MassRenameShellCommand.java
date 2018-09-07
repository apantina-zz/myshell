package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ParserException;
import hr.fer.zemris.java.hw07.shell.ShellCommandParser;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.ShellCommand;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderInfo;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderParser;

/**
 * Renames multiple files according to a regular expression and its potential
 * groupings. Also supports multiple subcommands to show the potential result of
 * renaming before actual renaming is executed.
 * 
 * @author 0036502252
 *
 */
public class MassRenameShellCommand implements ShellCommand {
	/**
	 * The input path.
	 */
	private Path input;
	/**
	 * The output path.
	 */
	private Path output;
	/**
	 * The arguments sent by the user, and separated by a parser.
	 */
	private String[] args;
	/**
	 * The environment of the shell.
	 */
	private Environment env;
	/**
	 * The description of this command.
	 */
	private List<String> description;
	/**
	 * The name of this command.
	 */
	private String name;

	/**
	 * Constructs a new {@link MassRenameShellCommand}.
	 */
	public MassRenameShellCommand() {
		this.name = "massrename";
		this.description = new ArrayList<>();
		description.add("Used for renaming multiple files at once.");
		description.add("The first argument is the source directory.");
		description.add("The second argument is the destination directory.");
		description.add("The third argument is the desired subcommand.");
		description.add("The supported subcommands are: filter, groups, show,"
				+ " execute.");
		description.add("The filter subcommand shows which files match the"
				+ " regex from the 4th argument.");
		description.add("The groups subcommand how the filenames are grouped"
				+ " using the regex.");
		description.add("The show subcommand shows how the files will be"
				+ " renamed using the regex and its groupings, determined by"
				+ " the additional 5th argument.");
		description
				.add("The execute subcommand executes the renaming process.");

	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		this.env = env; // saved, since it's used in multiple methods

		try {
			args = new ShellCommandParser(arguments).parsePath();
		} catch (RuntimeException ex) {
			env.writeln("Could not parse the path.");
			return ShellStatus.CONTINUE;
		}

		try {
			input = env.getCurrentDirectory().resolve(args[0].trim());
			output = env.getCurrentDirectory().resolve(args[1].trim());
		} catch (InvalidPathException ex) {
			env.writeln("Paths are invalid!");
			return ShellStatus.CONTINUE;
		}

		if (args.length != 4 && args.length != 5) {
			env.writeln("Expected 4 or 5 arguments for the command!");
			return ShellStatus.CONTINUE;
		}
		try {
			executeSubcommand();
		} catch (IllegalArgumentException ex) {
			env.writeln("Invalid regular expression!");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Executes a given subcommand, based on the argument sent by the user.
	 */
	private void executeSubcommand() {
		switch (args[2]) {
		case "filter":
			printFilter();
			break;
		case "groups":
			groups();
			break;
		case "show":
			show();
			break;
		case "execute":
			try {
				execute();
			} catch (IOException e) {
				env.writeln("Couldn't move files.");
			}
			break;
		default:
			env.writeln(args[2] + " is not a valid command name!");
		}
	}

	/**
	 * Executes the filter subcommand. Doesn't print anything, since this method
	 * is used in other subcommands.
	 * 
	 * @return the list of filtered files
	 * @see MassRenameShellCommand#filter()
	 */
	private List<File> filter() {
		File inputDir = input.toFile();
		File outputDir = output.toFile();

		if (!inputDir.isDirectory()) {
			env.writeln("Input path is not a valid directory!");
		}

		if (!outputDir.isDirectory()) {
			outputDir.mkdir();
		}

		Pattern pattern = Pattern.compile(args[3],
				Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

		List<File> filtered = new ArrayList<>();
		for (File f : inputDir.listFiles()) {
			Matcher matcher = pattern.matcher(f.getName());
			if (matcher.matches()) {
				filtered.add(f);
			}
		}

		return filtered;
	}

	/**
	 * Prints the list of filtered commands to the shell.
	 */
	private void printFilter() {
		List<File> filtered = filter();
		filtered.forEach(System.out::println);
	}

	/**
	 * Prints the groupings for each filename.
	 */
	private void groups() {
		List<File> filtered = filter();

		Pattern pattern = Pattern.compile(args[3],
				Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

		StringBuilder sb = new StringBuilder();
		for (File f : filtered) {
			Matcher matcher = pattern.matcher(f.getName());
			matcher.matches();
			sb.append(f.getName());
			for (int i = 0, n = matcher.groupCount(); i <= n; i++) {
				sb.append(" " + i + ": " + matcher.group(i));
			}
			env.writeln(sb.toString());
			sb.setLength(0);
		}
	}

	/**
	 * Shows how the files will be renamed when the execute subcommand is
	 * called.
	 * 
	 * @return the new filenames mapped to their corresponding files, or null
	 * in case the parser reports an error.
	 */
	private Map<File, String> show() {

		NameBuilderParser parser = new NameBuilderParser(args[4]);
		NameBuilder builder;
		try {
			builder = parser.getNameBuilder();
		} catch (ParserException ex) {
			env.writeln("Parser encountered an exception: " + ex.getMessage());
			return null;
		}

		Pattern pattern = Pattern.compile(args[3],
				Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		List<File> filtered = filter();

		Map<File, String> oldToNew = new HashMap<>();
		for (File file : filtered) {
			Matcher matcher = pattern.matcher(file.getName());
			matcher.matches();

			NameBuilderInfo info = new BuilderInfoImpl(matcher);
			builder.execute(info);

			String novoIme = info.getStringBuilder().toString();
			System.out.println(file.getName() + " => " + novoIme);
			oldToNew.put(file, novoIme);
		}
		return oldToNew;

	}

	/**
	 * Executes the mass renaming command.
	 * 
	 * @throws IOException
	 *             in case the moving process goes awry
	 */
	private void execute() throws IOException {

		Map<File, String> oldToNew = show();

 		if(oldToNew == null) return;
		
		oldToNew.forEach((file, newFile) -> {
			Path newPath = Paths.get(output.toString() + "/" + newFile);
			try {
				Files.move(file.toPath(), newPath);
			} catch (IOException e) {
				env.writeln("Error moving file: " + file.toString());
			}
		});
	}

	/**
	 * @author 0036502252
	 *
	 */
	/**
	 * A simple implementation of a {@link NameBuilderInfo} object.
	 * 
	 * @author 0036502252
	 *
	 */
	static class BuilderInfoImpl implements NameBuilderInfo {
		/**
		 * Used for creating a new filename.
		 */
		private StringBuilder sb;
		/**
		 * The matcher used for regex group indexing.
		 */
		private Matcher matcher;

		/**
		 * Constructs a new {@link BuilderInfoImpl}.
		 * 
		 * @param matcher
		 *            the matcher used for regex group indexing
		 */
		public BuilderInfoImpl(Matcher matcher) {
			this.matcher = matcher;
			this.sb = new StringBuilder();
		}

		@Override
		public StringBuilder getStringBuilder() {
			return sb;
		}

		/**
		 * Uses the matcher to return a substring from the desired group index.<
		 */
		@Override
		public String getGroup(int index) {
			return matcher.group(index);
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
