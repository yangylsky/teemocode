package tk.teemocode.module.base.convertor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.dto.Dto;

public abstract class BaseDtoConvertor<D extends Dto, B extends IBO> implements DtoConvertor<D, B> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Class<D> dtoClass;

	private Class<B> boClass;

	protected BaseDtoConvertor() {
		if(!this.getClass().equals(BaseDtoConvertor.class)) {
			Type classType = this.getClass().getGenericSuperclass();
			if(classType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) classType).getActualTypeArguments();
				dtoClass = ReflectionUtils.getParamType(paramTypes[0]);
				boClass = ReflectionUtils.getParamType(paramTypes[1]);
			}
		}
	}

	@Override
	public Class<D> getDtoClass() {
		return dtoClass;
	}

	@Override
	public D convert(B bo, String... fields) {
		return BaseConvertor.convert(bo, dtoClass, fields);
	}

	@Override
	public List<D> convert(List<B> bos, String... fields) {
		List<D> destObjs = new ArrayList<>();
		for(B bo : bos) {
			destObjs.add(convert(bo, fields));
		}
		return destObjs;
	}

	@Override
	public Set<D> convert(Set<B> bos, String... fields) {
		Set<D> destObjs = new HashSet<>();
		for(B bo : bos) {
			destObjs.add(convert(bo, fields));
		}
		return destObjs;
	}

	@Override
	public Page<D> convert(Page<B> boPage, String... fields) {
		Page<D> dtoPage = BaseConvertor.convert(boPage, dtoClass, false, fields);
		dtoPage.setResult(convert(boPage.getResult(), fields));
		return dtoPage;
	}

	@Override
	public B reverse(D dto, String... fields) {
		return BaseConvertor.convert(dto, boClass, fields);
	}

	@Override
	public B reverse(B bo, D dto, String... fields) {
		return BaseConvertor.convert(bo, dto, fields);
	}

	@Override
	public List<B> reverse(List<D> dtos, String... fields) {
		List<B> destObjs = new ArrayList<>();
		for(D dto : dtos) {
			destObjs.add(reverse(dto, fields));
		}
		return destObjs;
	}

	@Override
	public Set<B> reverse(Set<D> dtos, String... fields) {
		Set<B> destObjs = new HashSet<>();
		for(D dto : dtos) {
			destObjs.add(reverse(dto, fields));
		}
		return destObjs;
	}

	@Override
	public Page<B> reverse(Page<D> dtoPage, String... fields) {
		Page<B> boPage = BaseConvertor.convert(dtoPage, boClass, false, fields);
		boPage.setResult(reverse(dtoPage.getResult(), fields));
		return boPage;
	}
}
