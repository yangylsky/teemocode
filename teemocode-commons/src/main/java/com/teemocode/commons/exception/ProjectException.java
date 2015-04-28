package com.teemocode.commons.exception;

public class ProjectException extends RuntimeException {
	private static final long	serialVersionUID	= 7690873023173658738L;

	public ProjectException(String msg) {
		super(msg);
	}

	public ProjectException() {
		super();
	}

	public ProjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectException(Throwable cause) {
		super(cause);
	}
}
