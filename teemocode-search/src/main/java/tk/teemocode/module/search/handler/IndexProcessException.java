package tk.teemocode.module.search.handler;

public class IndexProcessException extends RuntimeException {
	private static final long	serialVersionUID	= -73658738L;

	public IndexProcessException(String msg) {
		super(msg);
	}

	public IndexProcessException() {
		super();
	}

	public IndexProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public IndexProcessException(Throwable cause) {
		super(cause);
	}
}
