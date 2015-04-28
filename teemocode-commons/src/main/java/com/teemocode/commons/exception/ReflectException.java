package com.teemocode.commons.exception;

public class ReflectException extends RuntimeException {
	private static final long serialVersionUID = -1856436857104403283L;

	public ReflectException() {
		super("Exceptioin occured when do reflect action.");
	}

	public ReflectException(String msg) {
		super(msg);
	}

	public ReflectException(Throwable cause) {
		super(cause);
	}

	public ReflectException(String message, Throwable cause) {
		super(message, cause);
	}
}
