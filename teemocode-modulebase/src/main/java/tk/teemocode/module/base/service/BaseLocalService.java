package tk.teemocode.module.base.service;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;

import tk.teemocode.commons.component.page.Page;

public interface BaseLocalService {
	public void flushSession();

	public <T> void refresh(T obj);

	public <T> T saveOrUpdate(T obj);

	public <T> void batchSave(Collection<T> objs);

	public <T> void delete(T obj);

	public <T> void batchDelete(Collection<T> objs);

	public <T> T getUniqueByHql(String hql, Object... values);

	public <T> List<T> getListByHql(Class<T> clazz, String hql, Object... values);

	public <T> List<T> findDatasByHql(String hql, Object... values);

	public <T> Page<T> findPage(Page<T> page, String hql, Object... values);

	public <T> Page<T> findPage(Class<T> clazz, Page<T> page, Criterion... criterion);

	public Integer queryForInt(String hql, Object... values);

	public Double queryForDouble(String hql, Object... values);

	public <T> T queryFor(String hql, Object... values);

	public Integer bulkUpdate(String hql, Object... values);
}
