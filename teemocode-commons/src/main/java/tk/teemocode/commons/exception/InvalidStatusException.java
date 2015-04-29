package tk.teemocode.commons.exception;

public class InvalidStatusException extends ProjectException {
	private static final long serialVersionUID = -436754950066424628L;

	public InvalidStatusException() {
		super("Invalid Status.");
	}

	public InvalidStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStatusException(String msg) {
		super(msg);
	}

	public InvalidStatusException(Throwable cause) {
		super(cause);
	}
}
