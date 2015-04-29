package tk.teemocode.module.base.convertor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.dto.Dto;
import tk.teemocode.module.base.vo.Vo;

public abstract class BaseVoConvertor<V extends Vo, D extends Dto> implements VoConvertor<V, D> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Class<V> voClass;

	private Class<D> dtoClass;

	protected BaseVoConvertor() {
		if(!this.getClass().equals(BaseVoConvertor.class)) {
			Type classType = this.getClass().getGenericSuperclass();
			if(classType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) classType).getActualTypeArguments();
				voClass = ReflectionUtils.getParamType(paramTypes[0]);
				dtoClass = ReflectionUtils.getParamType(paramTypes[1]);
			}
		}
	}

	@Override
	public V convert(D dto, String... fields) {
		return BaseConvertor.convert(dto, voClass, fields);
	}

	@Override
	public List<V> convert(List<D> dtos, String... fields) {
		return BaseConvertor.convert(dtos, voClass, fields);
	}

	@Override
	public Set<V> convert(Set<D> dtos, String... fields) {
		return BaseConvertor.convert(dtos, voClass, fields);
	}

	@Override
	public Page<V> convert(Page<D> dtoPage, String... fields) {
		return BaseConvertor.convert(dtoPage, voClass, fields);
	}

	@Override
	public D reverse(V vo, String... fields) {
		return BaseConvertor.convert(vo, dtoClass, fields);
	}

	@Override
	public List<D> reverse(List<V> vos, String... fields) {
		return BaseConvertor.convert(vos, dtoClass, fields);
	}

	@Override
	public Set<D> reverse(Set<V> vos, String... fields) {
		return BaseConvertor.convert(vos, dtoClass, fields);
	}

	@Override
	public Page<D> reverse(Page<V> voPage, String... fields) {
		return BaseConvertor.convert(voPage, dtoClass, fields);
	}
}
