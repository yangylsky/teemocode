package tk.teemocode.module.base.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.bo.IBO;

public interface IBaseDao<B extends IBO> extends IDao {
	public Class<B> getBoClass();

	public B get(Long id);

	public B load(Long id);

	public void deleteById(Long id);

	public List<B> getAll();

	public List<B> getListByIds(Collection<Long> ids);

	public List<B> getListByIds(Long... ids);

	public List<B> getListByUuids(Collection<String> uuids);

	public List<B> getListByUuids(String... uuids);

	public List<B> getListByHql(String hql, Object... values);

	public List<B> getAllBos();

	public B getBoByHql(String hql, Object... values);

	public B getBoById(Long id);

	public B getBoByNo(String boNo);

	public B getBoByName(String boName);

	public Page<B> findPage(Page<B> page);

	public Page<B> findPage(Page<B> page, Criterion... criterion);

	public List<B> getByProperty(String propertyName, Object value);

	public B getUniqueByProperty(String propertyName, Object value);

	public B getTopOneByProperty(String propertyName, Object value);

	/**
	 * 指定值的实体对象是否存在
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public boolean exist(String propertyName, Object value);
}
