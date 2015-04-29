package tk.teemocode.commons.exception;

public class AccessDenyException extends RuntimeException {
	private static final long serialVersionUID = 1710748859583062494L;

	public AccessDenyException() {
		super("Access Denied.");
	}

	public AccessDenyException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessDenyException(String msg) {
		super(msg);
	}

	public AccessDenyException(Throwable cause) {
		super(cause);
	}
}
