package tk.teemocode.module.index.convertor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.convertor.BaseConvertor;
import tk.teemocode.module.search.Indexable;

public abstract class BaseIndexItemConvertor<I extends Indexable, B extends IBO> implements IndexItemConvertor<I, B> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Class<I> indexItemClass;

	private Class<B> boClass;

	@SuppressWarnings("unchecked")
	protected BaseIndexItemConvertor() {
		if(!this.getClass().equals(BaseIndexItemConvertor.class)) {
			Type classType = this.getClass().getGenericSuperclass();
			if(classType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) classType).getActualTypeArguments();
				indexItemClass = (Class<I>) paramTypes[0];
				boClass = (Class<B>) paramTypes[1];
			}
		}
	}

	@Override
	public Class<I> getIndexItemClass() {
		return indexItemClass;
	}

	public Class<B> getBoClass() {
		return boClass;
	}

	@Override
	public I convert(B bo) {
		return BaseConvertor.convert(bo, getIndexItemClass());
	}

	@Override
	public Collection<I> convert(Collection<B> bos) {
		Collection<I> indexItems;
		if(bos instanceof Set) {
			indexItems = new HashSet<>();
		} else {
			indexItems = new ArrayList<>();
		}
		for(B bo : bos) {
			indexItems.add(convert(bo));
		}
		return indexItems;
	}
}
