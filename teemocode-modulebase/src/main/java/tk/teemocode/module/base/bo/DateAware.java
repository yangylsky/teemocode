package tk.teemocode.module.base.bo;

import java.util.Date;

public interface DateAware {
	public Date getCreateDate();

	public void setCreateDate(Date createDate);

	public Date getModifyDate();

	public void setModifyDate(Date modifyDate);
}
