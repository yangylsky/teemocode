package tk.teemocode.module.identity.bo;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import tk.teemocode.module.base.bo.EntityBO;
import tk.teemocode.module.identity.util.IdentityConstant;

/**
 * 角色实体类
 *
 */
@Entity
@Table(name = "id_role")
@AttributeOverrides({
    @AttributeOverride(name="name", column = @Column(unique=true,nullable=false)),
    @AttributeOverride(name="no", column = @Column(unique=true,nullable=false))
})
public class Role extends EntityBO {
	private static final long serialVersionUID = 605104382863061027L;

	/**
	 * 是否禁用
	 */
	private Boolean disabled = false;

	/**
	 * 角色类型（10-系统角色 20-机构角色）
	 */
	private Integer roleType = IdentityConstant.Role_Type_Sys;

	/**
	 * 角色机构uuid（支持机构角色定义)
	 */
	private String groupId;

	@ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinTable(name="id_role_authority",
	joinColumns={@JoinColumn(name="role_id")},
	inverseJoinColumns={@JoinColumn(name="authority_id")})
	private List<Authority> auths;

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<Authority> getAuths() {
		return auths;
	}

	public void setAuths(List<Authority> auths) {
		this.auths = auths;
	}

	//==========================Domain Business Method==============================//

	public List<String> getAuthUuids() {
		// TODO Auto-generated method stub
		return null;
	}
}
