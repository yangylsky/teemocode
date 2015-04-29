package tk.teemocode.module.index;

import java.io.Serializable;

public abstract class EmbedItem implements Serializable, Cloneable {
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
