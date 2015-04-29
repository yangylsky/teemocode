package tk.teemocode.web.upload;

import tk.teemocode.commons.exception.ProjectException;

public class UploadException extends ProjectException {
	private static final long serialVersionUID = -3211321543654656L;

	public UploadException() {
		super();
	}

	public UploadException(String msg) {
		super(msg);
	}

	public UploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadException(Throwable cause) {
		super(cause);
	}
}
