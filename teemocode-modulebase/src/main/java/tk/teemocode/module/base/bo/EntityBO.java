package tk.teemocode.module.base.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import tk.teemocode.commons.util.RandomUtil;

@MappedSuperclass
public abstract class EntityBO extends BaseComparableBO<EntityBO> implements IEntityBO, DateAware {
	/**
	 * 名称(标题)
	 */
	protected String name;

	/**
	 * 编号
	 */
	@Column(length = 32)
	protected String no = RandomUtil.getUniqueID();

	/**
	 * 显示名称
	 */
	protected String description;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createDate;

	/**
	 * 最后修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	protected Date modifyDate;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = fixValue(name);
	}

	@Override
	public String getNo() {
		return no;
	}

	@Override
	public void setNo(String no) {
		this.no = fixNo(no);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = fixValue(description);
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public Date getModifyDate() {
		return modifyDate;
	}

	@Override
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	//==========================Domain Business Method==============================//

	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof EntityBO) {
			EntityBO bo = (EntityBO) o;
			return super.equals(bo) || (StringUtils.isNotEmpty(getNo()) && getNo().equals(bo.getNo()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return StringUtils.isEmpty(getNo()) ? -1 : getNo().hashCode();
	}

	@Override
	public int compareTo(EntityBO anotherBo) {
		if(getNo() != null && anotherBo.getNo() != null) {
			return getNo().compareToIgnoreCase(anotherBo.getNo());
		}
		return super.compareTo(anotherBo);
	}

	@Override
	public String toString() {
		return "[id:" + getId() + ", no:" + getNo() + ", name:" + getName() + "]";
	}

	@Override
	public EntityBO checkBO() {
		if(super.checkBO() != null || StringUtils.isNotBlank(getName())) {
			return this;
		}
		return null;
	}

	public static String fixNo(String no) {
		String s = StringUtils.isBlank(no) ? null : StringUtils.trim(no.toUpperCase());
		return StringUtils.isBlank(s) ? null : s;
	}

	public static String fixValue(String value) {
		String s = StringUtils.isBlank(value) ? null : StringUtils.trim(value);
		return StringUtils.isBlank(s) ? null : s;
	}

	public static final String[] jsonListFields = {"id", "tag", "no", "name", "description", "createDate", "modifyDate"};
}
