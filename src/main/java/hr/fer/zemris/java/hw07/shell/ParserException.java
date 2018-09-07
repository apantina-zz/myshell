package hr.fer.zemris.java.hw07.shell;
/**
 * A runtime exception used in the {@link ShellCommandParser} class.
 * @author 0036502252
 *
 */
public class ParserException extends RuntimeException {
	/**
     * Auto-generated serial ID for this exception.
	 */
	private static final long serialVersionUID = 4185867920555342254L;

	
    /** Constructs a new SmartScriptParserException with {@code null} as its
     * detail message.
     */
    public ParserException() {
        super();
    }

    /** Constructs a new SmartScriptParserException with the specified detail message.
     * @param   message the detail message.
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Constructs a new SmartScriptParserException with the specified detail message and cause.
     * @param  message the detail message 
     * @param  cause the cause
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a new SmartScriptParserException with the specified cause.
     * @param  cause the cause
     */
    public ParserException(Throwable cause) {
        super(cause);
    }


}
