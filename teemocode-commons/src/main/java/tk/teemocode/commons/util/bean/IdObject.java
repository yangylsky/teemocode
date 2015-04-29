package tk.teemocode.commons.util.bean;

import java.io.Serializable;

public interface IdObject extends Serializable {
	public Long getId();

	public void setId(Long id);

	public boolean isValidId();

	public boolean isValidUuid();

	public String generateUuid();
}
