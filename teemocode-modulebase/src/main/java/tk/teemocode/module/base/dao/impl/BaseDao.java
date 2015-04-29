package tk.teemocode.module.base.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.exception.InvalidDataException;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.dao.IBaseDao;

@SuppressWarnings("unchecked")
@Repository
public class BaseDao<B extends IBO> extends Dao implements IBaseDao<B> {
	private Class<B> boClass;

	public BaseDao() {
		if(!this.getClass().equals(BaseDao.class)) {
			Type classType = this.getClass().getGenericSuperclass();
			if(classType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) classType).getActualTypeArguments();
				boClass = ReflectionUtils.getParamType(paramTypes[0]);
			}
		}
	}

	public BaseDao(SessionFactory sessionFactory, Class<B> boClass) {
		super(sessionFactory);
		this.boClass = boClass;
	}

	@Override
	public Class<B> getBoClass() {
		return boClass;
	}

	@Override
	public B get(Long id) {
		return getById(getBoClass(), id);
	}

	@Override
	public B load(Long id) {
		return load(getBoClass(), id);
	}

	@Override
	public List<B> getAll() {
		return getAllBos(getBoClass());
	}

	@Override
	public List<B> getListByIds(Collection<Long> ids) {
		String hql = this.fromHql() + " where id in (:ids)";
		Query query = getSession().createQuery(hql);
		query.setParameterList("ids", ids);
		return query.list();
	}

	@Override
	public List<B> getListByIds(Long... ids) {
		return getListByIds(Arrays.asList(ids));
	}

	@Override
	public List<B> getListByUuids(Collection<String> uuids) {
		String hql = this.fromHql() + " where uuid in (:uuids)";
		Query query = getSession().createQuery(hql);
		query.setParameterList("uuids", uuids);
		return query.list();
	}

	@Override
	public List<B> getListByUuids(String... uuids) {
		return getListByUuids(Arrays.asList(uuids));
	}

	@Override
	public B getBoByHql(String hql, Object... values) {
		return (B) queryTopOne(hql, values);
	}

	@Override
	public List<B> getListByHql(String hql, Object... values) {
		return find(getBoClass(), hql, values);
	}

	@Override
	public void deleteById(Long id) {
		deleteById(getBoClass(), id);
	}

	@Override
	public B getBoById(Long id) {
		return getById(getBoClass(), id);
	}

	@Override
	public B getBoByNo(String no) {
		return getUniqueByProperty("no", no);
	}

	@Override
	public B getBoByName(String name) {
		return getUniqueByProperty("name", name);
	}

	@Override
	public List<B> getAllBos() {
		return getAllBos(getBoClass());
	}

	@Override
	public Page<B> findPage(Page<B> page) {
		return findPage(getBoClass(), page);
	}

	@Override
	public Page<B> findPage(Page<B> page, Criterion... criterion) {
		return findPage(getBoClass(), page, criterion);
	}

	@Override
	public List<B> getByProperty(String propertyName, Object value) {
		return createCriteria(Restrictions.eq(propertyName, value)).list();
	}

	@Override
	public B getUniqueByProperty(String propertyName, Object value) {
		try {
			return (B) createCriteria(Restrictions.eq(propertyName, value)).uniqueResult();
		} catch(NonUniqueResultException e) {
			throw new InvalidDataException("数据不唯一[" + propertyName + ": " + value + "]");
		}
	}

	@Override
	public B getTopOneByProperty(String propertyName, Object value) {
		List<B> result = createCriteria(Restrictions.eq(propertyName, value)).setFirstResult(0).setMaxResults(1).list();
		if(CollectionUtils.isNotEmpty(result)) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public boolean exist(String propertyName, Object value) {
		String hql = "select id " + fromHql() + " where " + propertyName + "=?";
		return queryFor(hql, value) != null;
	}

	protected String fromHql() {
		return "from " + getBoClass().getSimpleName();
	}

	protected Criteria createCriteria(Criterion... criterions) {
		return createCriteria(getBoClass(), criterions);
	}
}
