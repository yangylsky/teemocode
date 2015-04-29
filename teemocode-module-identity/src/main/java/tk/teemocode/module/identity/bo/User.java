package tk.teemocode.module.identity.bo;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.base.bo.EntityBO;

/**
 * 用户实体类
 *
 */
@Entity
@Table(name = "id_user")
@AttributeOverrides({
	@AttributeOverride(name = "name", column = @Column(unique = true, nullable = false)),
	@AttributeOverride(name = "no", column = @Column(unique = true, nullable = false))
})
public class User extends EntityBO {
	private static final long serialVersionUID = 1288299096022375253L;

	/**
	 * 用户机构uuid
	 */
	private String groupUuid;

	/**
	 * 用户密码（登录密码）
	 */
	private String password;

	/**
	 * 用户类型
	 */
	private Integer type;

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
	 * 用户列表
	 */
	@ElementCollection
	@CollectionTable(name = "id_user_role", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role_uuid")
	private List<String> roles;

	/**
	 * 设备的UUID，在未登录的情况下，作为后期匹配用户用
	 */
	private String deviceUuid;

	/**
	 * 微信openId
	 */
	private String wxOpenId;

	/**
	 * 联系信息
	 */
	@Embedded
	private Contact contact;

	public User() {
	}

	public User(String uuid) {
		setUuid(uuid);
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
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

	//==========================Domain Business Method==============================//

	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof User) {
			User user = (User) o;
			return super.equals(user) || (StringUtils.isNotEmpty(getName()) && getName().equals(user.getName()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return StringUtils.isEmpty(getName()) ? -1 : getName().hashCode();
	}

	@Override
	public int compareTo(EntityBO anotherBo) {
		if(getName() != null && anotherBo.getName() != null) {
			return getName().compareToIgnoreCase(anotherBo.getName());
		}
		return super.compareTo(anotherBo);
	}
}
