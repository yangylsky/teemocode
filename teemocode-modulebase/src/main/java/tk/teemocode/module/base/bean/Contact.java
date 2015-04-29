package tk.teemocode.module.base.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import tk.teemocode.module.base.bo.EmbedBO;

/**
 * 联系人信息
 * @author yangylsky
 *
 */
@Embeddable
public class Contact implements EmbedBO {
	/**
	 * 联系人姓名
	 */
	@Column(length = 40)
	private String linkman;

	/**
	 * 地址
	 */
	@Column(length = 100)
	private String address;

	/**
	 * 座机
	 */
	@Column(length = 40)
	private String phone;

	/**
	 * 手机
	 */
	@Column(length = 40)
	private String mobile;

	/**
	 * QQ(可多个)
	 */
	private String qq;

	/**
	 * 传真
	 */
	@Column(length = 40)
	private String fax;

	/**
	 * 电子邮箱
	 */
	@Column(length = 50)
	private String email;

	/**
	 * 网址
	 */
	private String website;

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}
