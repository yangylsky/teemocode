package tk.teemocode.commons.util;

public interface Sortable<T> extends Comparable<T> {
	public static final int DefaultIndex = 9999;
	
	public Integer getSortIdx();

	public void setSortIdx(Integer sortIdx);
}
