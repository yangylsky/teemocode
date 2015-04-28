package com.teemocode.module.search;

/**
 * 支持创建索引项的对象
 * @author yangylsky
 *
 */
public interface Indexable {
	/**
	 * 索引名称
	 * 注：索引为顶级单位，类似于数据库
	 * @return
	 */
	public String getIndexName();

	/**
	 * 索引的TypeName
	 * 注：TypeName属于Index下一级的单位，类似于数据库表
	 * @return
	 */
	public String getTypeName();

	/**
	 * 索引项ID
	 * @return
	 */
	public String getId();
}
