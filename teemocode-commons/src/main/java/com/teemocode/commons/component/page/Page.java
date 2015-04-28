package com.teemocode.commons.component.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.teemocode.commons.util.bean.BeanUtils;

public class Page<T> extends QueryParameter implements Serializable {
	private static final long serialVersionUID = 7794929627061473289L;

	private Class<T> tClass;

	/** 开始页码 */
	private int start = -1;

	/** 每页多少 */
	private int limit = -1;

	private int pageNo = 1;

	/** 总记录数 */
	private int totalCount;

	private boolean autoCount = true;

	/** 分页结果 */
	private List<T> result;

	/**
	 * toJson的字段
	 */
	private String[] toJsonFields = {};

	public Class<T> getTClass() {
		return tClass;
	}

	public Page() {
		super();
	}

	public Page(Class<T> tClass) {
		this();

		this.tClass = tClass;
	}

	public Page(Class<T> tClass, int limit) {
		this(tClass);

		this.limit = limit;
	}

	public Page(int limit) {
		this();
		this.limit = limit;
	}

	public Page(int limit, boolean autoCount) {
		this();
		this.limit = limit;
		this.autoCount = autoCount;
	}

	/**
	 * 获得每页的记录数量,无默认值.
	 */
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return limit;
	}

	public void setPageSize(int pageSize) {
		limit = pageSize;
	}

	/**
	 * 是否已设置每页的记录数量.
	 */
	public boolean isPageSizeSetted() {
		return limit > -1;
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
	 */
	public int getFirst() {
		if(start > -1) {
			return start;
		}
		if(pageNo < 1 || limit < 1) {
			return -1;
		} else {
			return ((pageNo - 1) * limit);
		}
	}

	/**
	 * 是否已设置第一条记录记录在总结果集中的位置.
	 */
	public boolean isFirstSetted() {
		return ((start > -1) || (pageNo > 0 && limit > 0));
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 是否自动获取总页数,默认为false. 注意本属性仅于query by Criteria时有效,query by HQL时本属性无效.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 取得倒转的排序方向
	 */
	public String getInverseOrder() {
		if(dir.endsWith(DESC)) {
			return ASC;
		} else {
			return DESC;
		}
	}

	/**
	 * 页内的数据列表.
	 */
	public List<T> getResult() {
		if(totalCount <= 0) {
			return new ArrayList<T>();
		}
		return result;
	}

	public void setResult(List<T> result) {
		if(ArrayUtils.isNotEmpty(toJsonFields)) {
			this.result = BeanUtils.createObjects(result, toJsonFields);
		} else {
			this.result = result;
		}
//		if(totalCount <= 0) {
//			setTotalCount(result.size());
//		}
	}

	public void clearResult() {
		if(result != null) {
			result.clear();
		}
	}

	/**
	 * 计算总页数.
	 */
	public int getTotalPages() {
		if(totalCount == -1) {
			return -1;
		}

		int count = totalCount / limit;
		if(totalCount % limit > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 返回下页的页号,序号从1开始.
	 */
	public int getNextPage() {
		if(isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 返回上页的页号,序号从1开始.
	 */
	public int getPrePage() {
		if(isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	public String[] getToJsonFields() {
		return toJsonFields;
	}

	public void setToJsonFields(String[] toJsonFields) {
		this.toJsonFields = toJsonFields;
	}

	public void count() {
		if(pageNo <= 0) {
			pageNo = 1;
		}
		int pt = getTotalPages();
		if(pageNo > pt) {
			pageNo = pt;
		}
		start = (pageNo - 1) * limit;
		if(start <= 0 || start > totalCount) {
			start = 0;
		}
	}

	public <O extends Object> List<O> fetchResult(List<O> totalResults) {
		if(CollectionUtils.isEmpty(totalResults)) {
			updateByResultSize(0);
		} else {
			updateByResultSize(totalResults.size());
		}
		List<O> results = new ArrayList<O>();
		if(totalResults != null) {
			for(int i = 0; i < totalResults.size(); i++) {
				if(i >= getStart() && (i - getStart()) < getLimit()) {
					results.add(totalResults.get(i));
				}
			}
		}
		return results;
	}

	public void updateByResultSize(int resultSize) {
		if(resultSize > 0) {
			setTotalCount(resultSize);
		} else {
			setTotalCount(0);
		}
		count();
	}
}
