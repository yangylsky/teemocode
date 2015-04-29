package tk.teemocode.module.base.bo;

import javax.persistence.MappedSuperclass;

import tk.teemocode.commons.util.Sortable;

@MappedSuperclass
public abstract class BaseComparableBO<B extends BaseComparableBO<B>> extends BO implements Sortable<B> {
	/**
	 * 排序号
	 */
	private Integer sortIdx;

	@Override
	public Integer getSortIdx() {
		return sortIdx;
	}

	@Override
	public void setSortIdx(Integer sortIdx) {
		this.sortIdx = sortIdx;
	}

	//==========================Domain Business Method==============================//

	@Override
	public int compareTo(B anotherBo) {
		int result = compareSortIdx(anotherBo);
		if(result != Integer.MAX_VALUE) {
			return result;
		}
		return super.compareTo(anotherBo);
	}

	protected int compareSortIdx(B anotherBo) {
		if(getSortIdx() != null && anotherBo.getSortIdx() != null && getSortIdx() >= 0 && anotherBo.getSortIdx() >= 0) {
			return getSortIdx() - anotherBo.getSortIdx();
		}
		return Integer.MAX_VALUE;
	}
}
