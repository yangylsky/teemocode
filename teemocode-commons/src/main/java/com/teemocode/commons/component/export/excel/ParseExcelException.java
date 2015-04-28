package com.teemocode.commons.component.export.excel;

public class ParseExcelException extends RuntimeException {
	private static final long serialVersionUID = -1361626475814314134L;

	public ParseExcelException() {
		super();
	}

	public ParseExcelException(String msg) {
		super(msg);
	}

	public ParseExcelException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ParseExcelException(Throwable cause) {
		super(cause);
	}
}
