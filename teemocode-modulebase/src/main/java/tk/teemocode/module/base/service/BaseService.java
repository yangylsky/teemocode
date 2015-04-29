package tk.teemocode.module.base.service;

public interface BaseService {
	/**
	 * 指定字段值的对象是否存在
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public boolean exist(String propertyName, Object value);
}
