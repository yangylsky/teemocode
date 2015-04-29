package tk.teemocode.module.identity;

import java.io.Serializable;

public interface UserInfo extends Serializable {
	public String getName();

	public String getUuid();
}
