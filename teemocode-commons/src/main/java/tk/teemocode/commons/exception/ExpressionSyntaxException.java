package tk.teemocode.commons.exception;

public class ExpressionSyntaxException extends RuntimeException {
	private static final long serialVersionUID = 1710748859583062494L;

	public ExpressionSyntaxException() {
		super("Expression syntax error.");
	}

	public ExpressionSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpressionSyntaxException(String msg) {
		super(msg);
	}

	public ExpressionSyntaxException(Throwable cause) {
		super(cause);
	}
}
