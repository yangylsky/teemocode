package tk.teemocode.module.identity.dto;

import tk.teemocode.module.base.dto.EntityDto;

public class ModuleDto extends EntityDto {
	private static final long serialVersionUID = -2088135772640394706L;

	public static final String ALL_MODULE_FIELDS[] = {"uuid", "tag", "sortIdx", "createDate", "description", "modifyDate",
			"name", "no", "disabled", "parent", "moduleType", "url", "level"};

	public static final String SIMPLE_NAME = "Module";

	/**
	 * 上级功能模块(uuid)
	 */
	private String parent;

	/**
	 * 功能模块类型
	 */
	private String moduleType;

	/**
	 * 功能模块访问的url
	 */
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

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

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

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}
}
