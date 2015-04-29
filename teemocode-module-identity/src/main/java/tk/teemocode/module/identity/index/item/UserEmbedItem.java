package tk.teemocode.module.identity.index.item;

import org.apache.commons.lang3.StringUtils;

import tk.teemocode.commons.util.reflect.PojoUtil;
import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.identity.bo.User;
import tk.teemocode.module.index.EmbedItem;
import tk.teemocode.module.util.CommonUtil;

public class UserEmbedItem extends EmbedItem {
	private Integer type;

	private String name;

	private String description;

	private String deviceUuid;

	private String wxOpenId;

	private Contact contact;

	public UserEmbedItem() {
	}

	public UserEmbedItem(User user) {
		PojoUtil.merge(this, user);
		PojoUtil.merge(this.contact, user.getContact());
	}

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

	//==========================Business Method==============================//

	public static UserEmbedItem getUserEmbedItem(String userUuid) {
		if(StringUtils.isBlank(userUuid)) {
			return null;
		}
		User user = CommonUtil.getBoByUuid(User.class, userUuid);
		return new UserEmbedItem(user);
	}
}
