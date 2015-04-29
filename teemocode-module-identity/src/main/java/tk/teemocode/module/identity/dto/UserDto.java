package tk.teemocode.module.identity.dto;

import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.base.dto.EntityDto;
import tk.teemocode.module.identity.UserInfo;

/**
 * 用户数据对象
 *
 */
public class UserDto extends EntityDto implements UserInfo {
	private static final long serialVersionUID = 704344849784267008L;

	private String groupUuid;

	/**
	 * 用户类型
	 */
	private Integer type;

	/**
	 * 用户密码（登录密码）
	 */
	private String password;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	/**
	 * 是否是超级管理员
	 */
	private Boolean superUser;

	/**
	 * 是否激活
	 */
	private Boolean active;

	/**
	 * 设备的UUID
	 */
	private String deviceUuid;

	/**
	 * 微信openId
	 */
	private String wxOpenId;

	private Contact contact;

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getSuperUser() {
		return superUser;
	}

	public void setSuperUser(Boolean superUser) {
		this.superUser = superUser;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
