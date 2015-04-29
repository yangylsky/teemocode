package tk.teemocode.module.base.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import tk.teemocode.commons.component.page.Page;

public interface IDao {
	public Session getSession();

	/**
	 * 将一级缓存中的所有持久化对象清除,释放其占用的内存资源.
	 */
	public void clearSession();

	/**
	 * 刷新一级缓存区的内容,使之与数据库数据保持同步.
	 */
	public void flushSession();

	public void refresh(Object entity);

	/**
	 * 将指定的持久化对象从一级缓存中清除,释放对象所占用的内存资源,指定对象从持久化状态变为脱管状态,从而成为游离对象.
	 * @param entity
	 */
	public void evict(final Object entity);

	/**
	 * 将指定类的所有持久化对象从二级缓存中清除,释放对象所占用的资源.
	 * @param clazz
	 */
	public <T> void evictCache(Class<T> clazz);

	/**
	 * 将某个类的指定Id的持久化对象从二级缓存中清除,释放对象所占用的资源.
	 * @param clazz
	 * @param id
	 */
	public <T> void evictCache(Class<T> clazz, Long id);

	/**
	 * <ol>
	 * 检索方式:
	 * <li>首先查找二级缓存,如果有则直接返回;
	 * <li>如果没有则返回null.
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T get(Class<T> clazz, Long id);

	/**
	 * <ol>
	 * 检索方式:
	 * <li>首先查找二级缓存,如果有则直接返回;
	 * <li>如果没有则判断是否是lazy,如果不是直接访问数据库检索,查到记录返回,查不到返回null;
	 * <li>如果是lazy则需要建立代理对象,对象的initialized属性为false,target属性为null.在访问获得的代理对象的属性时,检索数据库,
	 * 如果找到记录则把该记录的对象复制到代理对象的target上,并将initialized=true,如果找不到返回null.
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T load(Class<T> clazz, Long id);

	public <T> List<T> loadAll(Class<T> clazz);

	public <T> List<T> queryList(String hql, Object... values);

	public <T> T queryUnique(String hql, Object... values);

	public Integer queryForInt(String hql, Object... values);

	public Double queryForDouble(String hql, Object... values);

	public <T> T queryFor(String hql, Object... values);

	public <T> T saveOrUpdate(T entity);

	public <T> T save(T entity);

	public <T> T merge(T entity);

	public void delete(Object entity);

	public <T> void deleteById(Class<T> clazz, Long id);

	public <T> void deleteByUuid(Class<T> clazz, String uuid);

	public <T> void deleteByName(Class<T> clazz, String name);

	public <T> void batchDelete(Collection<T> entities);

	public <T> void batchSave(Collection<T> entities);

	public int bulkUpdate(String hql);

	public int bulkUpdate(String hql, Object... values);

	public <T> T queryTopOne(String hql, Object... values);

	public <T> List<T> queryTopN(int topN, String hql, Object... values);

	/**
	 * 获取查询的所有列名
	 * @param type
	 * @param query
	 * @return
	 */
	public String[] getQueryFieldNames(int type, String query);

	public void printStatistics(Class<?>... clazz);

	public <T> void saveOrUpdate(Collection<T> entities);

	public <T> T getById(Class<T> clazz, Long id);

	public <T> T getByUuid(Class<T> clazz, String uuid);

	public <T> T getByNo(Class<T> clazz, String no);

	public <T> T getByName(Class<T> clazz, String name);

	public <T> T getUniqueByProperty(Class<T> clazz, String propertyName, Object propertyValue);

	public <T> List<T> getAllBos(Class<T> clazz);

	public <T> List<T> getListByIds(Class<T> clazz, Long... ids);

	public <T> List<T> getListByUuids(Class<T> clazz, String... uuids);

	public int countObjs(String hql, Object... values);

	public <T> List<T> find(Class<T> clazz, String hql, Object... values);

	public <T> Page<T> findPage(Page<T> page, String hql, Object... values);

	public <T> Page<T> findPage(Page<T> page, int totalCount, String hql, Object... values);

	/**
	 * 按Criterion分页查询.
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount. 其中firstResult可直接指定,也可以指定pageNo.
	 * autoCount指定是否动态获取总结果数.
	 * @param criterion 数量可变的Criterion.
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public <T> Page<T> findPage(Class<T> clazz, Page<T> page, Criterion... criterion);

	public <T> List<Object[]> findByCriteria(Class<T> clazz, Map<String, String> alias, List<String> propertys, Page<T> page,
			Criterion... criterion);
}
