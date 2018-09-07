package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.*;

/**
 * A simple shell implementation with various functionalities.
 * 
 * @author 0036502252
 *
 */
public class MyShell {

	/**
	 * The key used to access the shell's directory stack.
	 */
	public static final String STACK_KEY = "cdstack";

	/**
	 * The main method from which the shell is started.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Environment env = new ShellEnvironment(sc);
		ShellStatus status = ShellStatus.CONTINUE;

		StringBuilder sb = new StringBuilder();

		env.writeln("Welcome to MyShell v1.0!");

		while (status == ShellStatus.CONTINUE) {
			
			env.write(env.getPromptSymbol().toString() + " ");
			String line = env.readLine().trim();
			sb.append(line);
			if (line.equals("exit")) {
				env.commands().get(line).executeCommand(env, null);
			}

			while (line.endsWith(env.getMorelinesSymbol().toString())) {
				sb.setLength(sb.length() - 1);
				env.write(env.getMultilineSymbol().toString() + " ");
				line = env.readLine().trim();
				sb.append(line);
			}

			if (sb.toString().startsWith("symbol")) {
				env.commands().get("symbol").executeCommand(env, sb.toString());
			} else {
				String commandName = sb.toString().split(" ")[0];
				String arguments = sb.toString().substring(commandName.length())
						.trim();

				ShellCommand cmd = env.commands().get(commandName);

				if (cmd == null) {
					env.writeln("Command \"" + commandName + "\" not found.");
				} else {
					status = cmd.executeCommand(env, arguments);
				}
			}
			sb.setLength(0); // reset the builder
		}

		sc.close();

	}

	/**
	 * The environment used by the shell for reading and writing.
	 * 
	 * @author 0036502252
	 *
	 */
	static class ShellEnvironment implements Environment {
		/**
		 * The user uses this symbol to continue the command over multiple
		 * lines.
		 */
		private Character moreLinesSymbol;
		/**
		 * Replaces the prompt symbol when multiple lines are used for a single
		 * command.
		 */
		private Character multilineSymbol;
		/**
		 * Printed on the screen to indicate that the user is expected to enter
		 * a command.
		 */
		private Character promptSymbol;
		/**
		 * A map of all supported commands, mapped by their name.
		 */
		private SortedMap<String, ShellCommand> commands;
		/**
		 * The scanner instance used for reading lines from the user.
		 */
		private Scanner scanner;

		/**
		 * The current working directory.
		 */
		private Path currentDirectory;

		/**
		 * The data which the environment can use for its various operations.
		 */
		private Map<String, Object> sharedData;

		/**
		 * Initializes the shell environment.
		 * 
		 * @param sc
		 *            the scanner instance used for reading lines from the user
		 */
		public ShellEnvironment(Scanner sc) {
			initCommands();
			initSymbols();
			this.scanner = sc;
			this.currentDirectory = Paths.get(".").normalize().toAbsolutePath();
			sharedData = new HashMap<>();
		}

		/**
		 * Adds the supported commands to the environment's command map.
		 */
		private void initCommands() {
			commands = new TreeMap<>();

			commands.put("exit", new ExitShellCommand());
			commands.put("ls", new LsShellCommand());
			commands.put("copy", new CopyShellCommand());
			commands.put("charsets", new CharsetsShellCommand());
			commands.put("cat", new CatShellCommand());
			commands.put("help", new HelpShellCommand());
			commands.put("hexdump", new HexdumpShellCommand());
			commands.put("mkdir", new MkdirShellCommand());
			commands.put("tree", new TreeShellCommand());
			commands.put("symbol", new SymbolShellCommand());

			commands.put("cd", new CDShellCommand());
			commands.put("pushd", new PushdShellCommand());
			commands.put("popd", new PopdShellCommand());
			commands.put("pwd", new PWDShellCommand());
			commands.put("listd", new ListdShellCommand());
			commands.put("dropd", new DropdShellCommand());
			commands.put("rmtree", new RmtreeShellCommand());
			commands.put("cptree", new CptreeShellCommand());
			commands.put("massrename", new MassRenameShellCommand());

		}

		/**
		 * Initializes the shell's symbols to their default values.
		 */
		private void initSymbols() {
			promptSymbol = '>';
			multilineSymbol = '|';
			moreLinesSymbol = '\\';
		}

		@Override
		public String readLine() throws ShellIOException {
			try {
				return scanner.nextLine();
			} catch (RuntimeException e) {
				throw new ShellIOException(
						"An exception occurred during reading: ", e.getCause());
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			try {
				System.out.print(text);
			} catch (RuntimeException e) {
				throw new ShellIOException(
						"An exception occurred during writing: ", e.getCause());
			}
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			try {
				System.out.println(text);
			} catch (RuntimeException e) {
				throw new ShellIOException(
						"An exception occurred during writing: ", e.getCause());
			}
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multilineSymbol = symbol;
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			promptSymbol = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return moreLinesSymbol;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			moreLinesSymbol = symbol;
		}

		@Override
		public Path getCurrentDirectory() {
			return currentDirectory.normalize();
		}

		@Override
		public void setCurrentDirectory(Path path) {
			if (!path.toFile().isDirectory()) {
				throw new ShellIOException("Given path is not a directory!");
			}
			this.currentDirectory = path.toAbsolutePath();
		}

		@Override
		public Object getSharedData(String key) {
			return sharedData.get(key);
		}

		@Override
		public void setSharedData(String key, Object value) {
			sharedData.put(key, value);
		}
	}
}