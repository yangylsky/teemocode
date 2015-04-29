package tk.teemocode.module.base.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import tk.teemocode.module.util.SystemConstant;

public abstract class Dto implements Serializable, Cloneable {
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

	@Override
	public boolean equals(Object o) {
		if(o instanceof Dto) {
			return getId().equals(((Dto) o).getId());
		}
		return false;
	}

	public boolean isValidId() {
		return id != null && id > 0;
	}

	public boolean isValidUuid() {
		return StringUtils.isNotBlank(uuid);
	}

	public boolean isPutOn() {
		return getTag() != null && getTag() == SystemConstant.Tag_PutOn;
	}

	public boolean isPulledOff() {
		return getTag() != null && getTag() == SystemConstant.Tag_PullOff;
	}
}
