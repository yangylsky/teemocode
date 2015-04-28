package com.teemocode.commons.exception;

public class SessionTimeoutException extends ProjectException{

	private static final long serialVersionUID = 2501413771479459459L;

	public SessionTimeoutException() {
		super("Session Timeout.");
	}

	public SessionTimeoutException(String msg) {
		super(msg);
	}

	public SessionTimeoutException(Throwable cause) {
		super(cause);
	}

	public SessionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
