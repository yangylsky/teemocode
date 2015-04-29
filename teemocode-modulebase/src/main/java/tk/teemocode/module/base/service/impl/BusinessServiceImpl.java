package tk.teemocode.module.base.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import tk.teemocode.commons.util.UUIDUtil;
import tk.teemocode.commons.util.reflect.PojoUtil;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.bo.BO;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.convertor.DtoConvertor;
import tk.teemocode.module.base.dto.Dto;

@Service
public class BusinessServiceImpl extends BaseServiceImpl {
	/**
	 * 获取默认的DtoConvertor
	 * @return
	 */
	protected <D extends Dto, B extends IBO> DtoConvertor<D, B> getDtoConvertor() {
		throw new UnsupportedOperationException("本方法需要子类覆盖实现");
	}

	@SuppressWarnings("unchecked")
	protected <D extends Dto, B extends IBO> B saveByDto(D dto, Class<B> boClass) {
		B bo = null;
		if(BO.isValidUuid(dto.getUuid())) {
			bo = (B) getLocalService().getByUuid(dto.getUuid());
		} else {
			try {
				bo = boClass.newInstance();
				dto.setUuid(UUIDUtil.generate());
			} catch(InstantiationException | IllegalAccessException e) {
				logger.error("", e);
			}
		}
		String[] fieldNames = ReflectionUtils.getStaticField(dto.getClass(), "SAVE_FIELDS");
		if(fieldNames != null) {
			PojoUtil.objToObjByDescsWithNull(bo, dto, fieldNames);
		} else {
			PojoUtil.simpleObjToObj(bo, dto);
		}
		return (B) getLocalService().saveOrUpdate(bo);
	}

	protected <D extends Dto, B extends IBO> void saveByDtos(Collection<D> dtos, Class<B> boClass) {
		for(D dto : dtos){
			saveByDto(dto, boClass);
		}
	}

	protected <D extends Dto, B extends IBO> D getByUuid(String uuid) {
		D dto = _getByUuid(uuid);
		return dto;
	}

	@SuppressWarnings("unchecked")
	protected <D extends Dto, B extends IBO> D _getByUuid(String uuid) {
		B bo = (B) getLocalService().getByUuid(uuid);
		return (D) getDtoConvertor().convert(bo);
	}
}
