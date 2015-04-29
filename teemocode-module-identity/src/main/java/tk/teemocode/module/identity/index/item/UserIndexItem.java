package tk.teemocode.module.identity.index.item;

import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.index.IndexItem;

public class UserIndexItem extends IndexItem {
	public static final String TypeName = "User";

	private Integer type;

	private String name;

	private String description;

	private String deviceUuid;

	private String wxOpenId;

	private Contact contact;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
