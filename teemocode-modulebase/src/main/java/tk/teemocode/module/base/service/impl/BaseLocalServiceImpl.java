package tk.teemocode.module.base.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.dao.impl.Dao;
import tk.teemocode.module.base.service.BaseLocalService;
import tk.teemocode.module.util.SystemEnv;

@Service("base/BaseLocalService")
@Transactional(rollbackFor = Throwable.class)
public class BaseLocalServiceImpl implements BaseLocalService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String sessionFactoryName = "sessionFactory";

	protected Dao dao;

	@PostConstruct
	protected void initSessionFactory() {
		if(StringUtils.isNotBlank(getSessionFactoryName())) {
			SessionFactory sessionFactory = SystemEnv.getBean(getSessionFactoryName());
			this.dao = Dao.getDao(sessionFactory);
		}
	}

	public void setSessionFactoryName(String sessionFactoryName) {
		this.sessionFactoryName = sessionFactoryName;
	}

	public String getSessionFactoryName() {
		return sessionFactoryName;
	}

	@Override
	public void flushSession() {
		dao.flushSession();
	}

	@Override
	public <T> void refresh(T obj) {
		dao.refresh(obj);
	}

	@Override
	public <T> T saveOrUpdate(T obj) {
		dao.saveOrUpdate(obj);
		return obj;
	}

	@Override
	public <T> void batchSave(Collection<T> objs) {
		for(T obj : objs) {
			saveOrUpdate(obj);
		}
	}

	@Override
	public <T> void delete(T obj) {
		dao.delete(obj);
	}

	@Override
	public <T> void batchDelete(Collection<T> objs) {
		for(T obj : objs) {
			dao.delete(obj);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Integer queryForInt(String hql, Object... values) {
		return dao.queryForInt(hql, values);
	}

	@Transactional(readOnly = true)
	@Override
	public Double queryForDouble(String hql, Object... values) {
		return dao.queryForDouble(hql, values);
	}

	@Override
	public <T> T queryFor(String hql, Object... values) {
		return dao.queryFor(hql, values);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public <T> List<T> findDatasByHql(String hql, Object... values) {
		return  (List<T>) dao.queryList(hql, values);
	}

	@Transactional(readOnly = true)
	@Override
	public <T> Page<T> findPage(Page<T> page, String hql, Object... values) {
		return dao.findPage(page, hql, values);
	}

	@Transactional(readOnly = true)
	@Override
	public <T> Page<T> findPage(Class<T> clazz, Page<T> page, Criterion... criterion) {
		return dao.findPage(clazz, page, criterion);
	}

	@Override
	public Integer bulkUpdate(String hql, Object... values) {
		return dao.bulkUpdate(hql, values);
	}

	@Override
	public <T> T getUniqueByHql(String hql, Object... values) {
		return dao.queryUnique(hql, values);
	}

	@Override
	public <T> List<T> getListByHql(Class<T> clazz, String hql, Object... values) {
		return dao.find(clazz, hql, values);
	}
}
