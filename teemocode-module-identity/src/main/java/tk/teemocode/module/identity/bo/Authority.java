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
import tk.teemocode.module.identity.util.IdentityConstant;

/**
 * 权限实体类
 *
 */
@Entity
@Table(name = "id_authority")
@AttributeOverrides({
    @AttributeOverride(name="name", column = @Column(unique=true,nullable=false)),
    @AttributeOverride(name="no", column = @Column(unique=true,nullable=false))
})
public class Authority extends EntityBO {
	private static final long serialVersionUID = 4812288085270154646L;

	/**
	 * 所属功能模块
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private Module module;

	/**
	 * 层次级别
	 */
	private Integer level;

	/**
	 * 支持method，data，也支持url,资源ant描述
	 */
	@Column(length = 4096)
	private String authExpression;


	/**
	 * 开放授权控制
	 */
	private Boolean needAuth = true;

	/**
	 * 权限类型
	 */
	private Integer authType = IdentityConstant.Auth_Type_Module;

	/**
	 * web层url资源描述
	 */
	private String authUrl;

	/**
	 * 是否禁用
	 */
	private Boolean disabled = false;

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getAuthExpression() {
		return authExpression;
	}

	public void setAuthExpression(String authExpression) {
		this.authExpression = authExpression;
	}

	public Boolean getNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(Boolean needAuth) {
		this.needAuth = needAuth;
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
}
