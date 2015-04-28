package com.teemocode.commons.exception;

public class NoSuchObjectException extends ProjectException {
	private static final long serialVersionUID = -8255851518864007931L;

	public NoSuchObjectException() {
		super("No Such Object.");
	}

	public NoSuchObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchObjectException(String msg) {
		super(msg);
	}

	public NoSuchObjectException(Throwable cause) {
		super(cause);
	}
}
