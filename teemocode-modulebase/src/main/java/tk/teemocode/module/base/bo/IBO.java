package tk.teemocode.module.base.bo;

import tk.teemocode.commons.util.bean.IdObject;

public interface IBO extends IdObject, Cloneable {
	public String getUuid();

	public Integer getTag();

	public void setTag(Integer tag);

	public IBO set(String name, Object value);

	public Object get(String name);

	/**
	 * 检查BO的有效性，无效的将返回null
	 * @return
	 */
	public IBO checkBO();

	/**
	 * 是否支持重复上架，即：在上架状态时，是否还可以做上架操作
	 * @return
	 */
	public boolean canRepeatPutOn();

	/**
	 * 是否是激活(上架)的
	 * @return
	 */
	public boolean isPutOn();

	/**
	 * 是否是未激活(下架)的
	 * @return
	 */
	public boolean isPulledOff();
}
