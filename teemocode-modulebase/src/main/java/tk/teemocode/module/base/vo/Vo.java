package tk.teemocode.module.base.vo;

import java.io.Serializable;

public class Vo implements Serializable, Cloneable {
	private Long id;

	private String uuid;

	private Integer tag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}
}
