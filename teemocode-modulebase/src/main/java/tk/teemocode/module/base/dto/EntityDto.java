package tk.teemocode.module.base.dto;

import java.util.Date;

public abstract class EntityDto extends Dto {
	/**
	 * 名称(标题)
	 */
	protected String name;

	/**
	 * 编号
	 */
	protected String no;

	/**
	 * 描述(显示名称)
	 */
	protected String description;

	/**
	 * 创建时间
	 */
	protected Date createDate;

	/**
	 * 最后修改时间
	 */
	protected Date modifyDate;

	/**
	 * 排序号
	 */
	private Integer sortIdx;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getSortIdx() {
		return sortIdx;
	}

	public void setSortIdx(Integer sortIdx) {
		this.sortIdx = sortIdx;
	}
}
