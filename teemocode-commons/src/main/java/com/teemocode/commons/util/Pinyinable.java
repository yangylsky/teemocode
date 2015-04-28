package com.teemocode.commons.util;

/**
 * 汉语拼音支持接口，实现该接口的对象应该具有名称为name或value的property之一或全部。
 * @author yangylsky
 *
 */
public interface Pinyinable {
	/**
	 * 获取可转换拼音的原始值
	 * @return
	 */
	public String getPinyinableValue();

	/**
	 * pinyinableValue对应的简拼(首字母拼音)
	 * @return
	 */
	public String getPinyin();

	public void setPinyin(String pinyin);

	/**
	 * pinyinableValue对应的全拼
	 * @return
	 */
	public String getFullPinyin();

	public void setFullPinyin(String pinyin);
}
