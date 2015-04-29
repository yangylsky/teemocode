package tk.teemocode.commons.exception;

public class NoPrivilegeException extends ProjectException {
	private static final long serialVersionUID = -7550207017990480321L;

	public NoPrivilegeException() {
		super("Not privilege!");
	}

	public NoPrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPrivilegeException(String message) {
		super(message);
	}

	public NoPrivilegeException(Throwable cause) {
		super(cause);
	}
}
