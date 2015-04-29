package tk.teemocode.module.index.convertor;

import java.util.Collection;

import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.search.Indexable;

public interface IndexItemConvertor<I extends Indexable, B extends IBO> {
	public Class<I> getIndexItemClass();

	public I convert(B bo);

	public Collection<I> convert(Collection<B> bos);
}
