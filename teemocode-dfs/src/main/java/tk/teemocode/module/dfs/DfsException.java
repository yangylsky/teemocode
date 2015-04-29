package tk.teemocode.module.dfs;

import tk.teemocode.commons.exception.ProjectException;

public class DfsException extends ProjectException {
	public DfsException() {
		super();
	}

	public DfsException(String msg) {
		super(msg);
	}

	public DfsException(String message, Throwable cause) {
		super(message, cause);
	}

	public DfsException(Throwable cause) {
		super(cause);
	}
}
