package tk.teemocode.module.base.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.service.BaseService;
import tk.teemocode.module.base.service.LocalService;

public abstract class BaseServiceImpl implements BaseService {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String moduleName;

	protected String serviceName;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public boolean exist(String propertyName, Object value) {
		return getLocalService().exist(propertyName, value);
	}

	/**
	 * 获取默认的LocalService
	 * @return
	 */
	protected <M extends IBO, S extends LocalService<M>> S getLocalService() {
		throw new UnsupportedOperationException("本方法需要子类覆盖实现");
	}
}
