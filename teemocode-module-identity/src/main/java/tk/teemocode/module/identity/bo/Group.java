package tk.teemocode.module.identity.bo;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import tk.teemocode.module.base.bean.Contact;
import tk.teemocode.module.base.bo.EntityBO;

/**
 * 组织机构实体类
 *
 */
@Entity
@Table(name = "id_group")
@Inheritance(strategy = InheritanceType.JOINED)
@AttributeOverrides({
    @AttributeOverride(name="name", column = @Column(unique=true,nullable=false)),
    @AttributeOverride(name="no", column = @Column(unique=true,nullable=false))
})
public class Group extends EntityBO {
	private static final long serialVersionUID = -6431655851328931584L;

	/**
	 * 机构层次
	 */
	private Integer level;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	/**
	 * 机构类型
	 */
	@ElementCollection
	@CollectionTable(name = "id_group_type", joinColumns = @JoinColumn(name = "group_id"))
	@Column(name = "type")
	private List<Integer> types;

	/**
	 * 父机构（uuid）
	 */
	private String parentUuid;

	/**
	 * 国家(uuid)
	 */
	private String countryUuid;

	/**
	 * 省(uuid)
	 */
	private String provinceUuid;

	/**
	 * 城市(uuid)
	 */
	private String cityUuid;

	/**
	 * 县区
	 */
	private String districtUuid;

	@Embedded
	private Contact contact;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	public String getCountryUuid() {
		return countryUuid;
	}

	public void setCountryUuid(String countryUuid) {
		this.countryUuid = countryUuid;
	}

	public String getProvinceUuid() {
		return provinceUuid;
	}

	public void setProvinceUuid(String provinceUuid) {
		this.provinceUuid = provinceUuid;
	}

	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}

	public String getDistrictUuid() {
		return districtUuid;
	}

	public void setDistrictUuid(String districtUuid) {
		this.districtUuid = districtUuid;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
