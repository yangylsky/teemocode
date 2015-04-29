package tk.teemocode.module.identity.bo;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tk.teemocode.module.base.bo.EntityBO;

/**
 * 功能模块实体类
 *
 */
@Entity
@Table(name = "id_module")
@AttributeOverrides({
    @AttributeOverride(name="name", column = @Column(unique=true,nullable=false)),
    @AttributeOverride(name="no", column = @Column(unique=true,nullable=false))
})
public class Module extends EntityBO {
	private static final long serialVersionUID = 7784991384658426294L;

	/**
	 * 上级功能模块(uuid)
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private Module parent;

	/**
	 * 功能模块类型
	 */
	private String moduleType;

	/**
	 * 功能模块访问的url
	 */
	@Column(length=500)
	private String url;

	/**
	 * 功能模块层次级别
	 */
	private Integer level;

	/**
	 * 是否禁用
	 */
	private Boolean disabled = false;

	/**
	 * 功能模块扩展参数(json object)
	 */
	private String props;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Module getParent() {
		return parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}
}
