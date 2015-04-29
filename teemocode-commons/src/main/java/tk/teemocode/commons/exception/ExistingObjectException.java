package tk.teemocode.commons.exception;


public class ExistingObjectException extends ProjectException{
	private static final long	serialVersionUID	= 2352117469035262965L;

	public ExistingObjectException() {
		super("This Object already existing!");
	}

	public ExistingObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistingObjectException(String message) {
		super(message);
	}

	public ExistingObjectException(Throwable cause) {
		super(cause);
	}

}
