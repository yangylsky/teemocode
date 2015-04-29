package tk.teemocode.web.util;

import java.io.Serializable;

/**
 * 前端传入的查询参数
 * @author yangylsky
 *
 */
public abstract class SearchParams implements Serializable {
	/**
	 * 模糊匹配关键字
	 */
	private String q;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
}
