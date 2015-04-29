package tk.teemocode.commons.exception;

/**
 * Service层公用的Exception.
 * 继承自RuntimeException,会触发Spring的事务管理引起事务回退.
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -2331646981607122388L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}
