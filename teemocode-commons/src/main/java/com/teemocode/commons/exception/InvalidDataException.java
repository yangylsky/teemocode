package com.teemocode.commons.exception;

public class InvalidDataException extends ProjectException {
	private static final long serialVersionUID = -43675321312312L;

	public InvalidDataException() {
		super("Invalid Data.");
	}

	public InvalidDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataException(String msg) {
		super(msg);
	}

	public InvalidDataException(Throwable cause) {
		super(cause);
	}
}
