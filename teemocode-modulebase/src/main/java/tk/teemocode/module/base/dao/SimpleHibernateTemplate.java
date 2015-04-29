package tk.teemocode.module.base.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import tk.teemocode.commons.component.hibernate.HibernateUtil;
import tk.teemocode.commons.component.json.JSONDecoder;
import tk.teemocode.commons.component.json.JSONHiEncoder;
import tk.teemocode.commons.component.page.JSONHiExpression;
import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.component.page.QueryParameter;
import tk.teemocode.commons.util.reflect.ReflectionUtils;

/**
 * Hibernate的范型基类. 可以在service类中直接创建使用.也可以继承出DAO子类,在多个Service类中共享DAO操作. 参考Spring2.5自带
 * 的Petlinc例子,取消了HibernateTemplate. 通过Hibernate的sessionFactory.getCurrentSession()获得session,直接使用Hibernate原生API.
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 */
@SuppressWarnings("unchecked")
public class SimpleHibernateTemplate<T, PK extends Serializable> {
	protected Log logger = LogFactory.getLog(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	public SimpleHibernateTemplate(SessionFactory sessionFactory, Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void flushSession() {
		getSession().flush();
	}

	public void save(T entity) {
		Assert.notNull(entity);
		getSession().saveOrUpdate(entity);
		if(logger.isDebugEnabled()) {
			logger.debug("save entity: " + JSONHiEncoder.encode(entity));
		}
	}

	public void update(T entity) {
		Assert.notNull(entity);
		getSession().update(entity);
		if(logger.isDebugEnabled()) {
			logger.debug("save entity: " + JSONHiEncoder.encode(entity));
		}
	}

	public void refresh(T entity) {
		Assert.notNull(entity);
		getSession().refresh(entity);
		if(logger.isDebugEnabled()) {
			logger.debug("refresh entity: " + JSONHiEncoder.encode(entity));
		}
	}

	public void merge(T entity) {
		Assert.notNull(entity);
		getSession().merge(entity);
		if(logger.isDebugEnabled()) {
			logger.debug("refresh entity: " + JSONHiEncoder.encode(entity));
		}
	}

	/**
	 * 批量存储
	 * @param cols 对象列表
	 */
	public void saveBatch(Collection<? extends T> cols) {
		int i = 0;
		Session se = getSession();
		for(T col : cols) {
			getSession().saveOrUpdate(col);
			if(++i % 20 == 0) {
				se.flush();
				se.clear();
			}
		}
	}

	/**
	 * 批量删除
	 * @param cols 对象列表
	 */
	public void deleteBatch(Collection<? extends T> cols) {
		int i = 0;
		Session se = getSession();
		for(T col : cols) {
			se.delete(col);
			if(++i % 20 == 0) {
				se.flush();
				se.clear();
			}
		}
	}

	public void deleteBatch(PK[] ids) {
		int i = 0;
		Session se = getSession();
		for(PK id : ids) {
			this.delete(id);
			if(++i % 20 == 0) {
				se.flush();
				se.clear();
			}
		}
	}

	/**
	 * 删除对象
	 * @param entity 对象
	 */
	public void delete(T entity) {
		Assert.notNull(entity);
		getSession().delete(entity);
		if(logger.isDebugEnabled()) {
			logger.debug("delete entity: {}" + entity);
		}
	}

	/**
	 * 删除对象
	 * @param id 对象id
	 */
	public void delete(PK id) {
		Assert.notNull(id);
		delete(get(id));
	}

	public List<T> getAll() {
		return findByCriteria();
	}

	public Page<T> findAll(Page<T> page) {
		return findByCriteria(page);
	}

	/**
	 * 按id获取对象.
	 */
	public T get(final PK id) {
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 按id获取对象.lazz加载
	 */
	public T load(final PK id) {
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 按HQL查询对象列表.
	 * @param hql hql语句
	 * @param values 数量可变的参数
	 */
	public List<?> find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL分页查询.
	 * @param page 分页参数.包括pageSize 和firstResult.
	 * @param hql hql语句.
	 * @param values 数量可变的参数.
	 * @return 分页查询结果,附带结果列表及所有查询时的参数.
	 */
	public Page<T> find(Page<T> page, String hql, Object... values) {
		Assert.notNull(page);

		if(page.isAutoCount()) {
			int fromIdx = hql.indexOf("from");
			String counthql = "select count(*) " + hql.substring(fromIdx);
			Query q = createQuery(counthql, values);
			List<?> objs = q.list();
			int totalCount = 0;
			if(objs != null && objs.size() == 1) {
				totalCount = ((Long) objs.get(0)).intValue();
			} else {
				totalCount = 0;
			}
			page.setTotalCount(totalCount);
		}
		hql = HibernateUtil.removeRedundancy(hql);
		hql = HibernateUtil.removeEndRedundancy(hql);
		Query q = createQuery(hql, values);
		if(page.isFirstSetted()) {
			q.setFirstResult(page.getFirst());
		}
		if(page.isPageSizeSetted()) {
			q.setMaxResults(page.getPageSize());
		}
		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按HQL分页查询.
	 * @param countPrefix 总结果查询前缀.
	 * @param page 分页参数.包括pageSize 和firstResult.
	 * @param hql hql语句.
	 * @param values 数量可变的参数.
	 * @return 分页查询结果,附带结果列表及所有查询时的参数.
	 */
	public Page<T> find(String countPrefix, Page<T> page, String hql, Object... values) {
		Assert.notNull(page);

		if(page.isAutoCount()) {
			int fromIdx = hql.indexOf("from");
			String counthql = "select count("+countPrefix+") " + hql.substring(fromIdx);
			Query q = createQuery(counthql, values);
			List<?> objs = q.list();
			int totalCount = 0;
			if(objs != null && objs.size() == 1) {
				totalCount = ((Long) objs.get(0)).intValue();
			} else {
				totalCount = 0;
			}
			page.setTotalCount(totalCount);
		}
		hql = HibernateUtil.removeRedundancy(hql);
		hql = HibernateUtil.removeEndRedundancy(hql);
		Query q = createQuery(hql, values);
		if(page.isFirstSetted()) {
			q.setFirstResult(page.getFirst());
		}
		if(page.isPageSizeSetted()) {
			q.setMaxResults(page.getPageSize());
		}
		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按HQL查询唯一对象.
	 */
	public Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询Intger类形结果.
	 */
	public Integer findInt(String hql, Object... values) {
		return ((Number) findUnique(hql, values)).intValue();
	}

	/**
	 * 按HQL查询Long类型结果.
	 */
	public Long findLong(String hql, Object... values) {
		return ((Number) findUnique(hql, values)).longValue();
	}

	/**
	 * 按Criterion查询对象列表.
	 * @param criterion 数量可变的Criterion.
	 */
	public List<T> findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	/**
	 * 按Criterion分页查询.
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount. 其中firstResult可直接指定,
	 * 也可以指定pageNo. autoCount指定是否动态获取总结果数.
	 * @param criterion 数量可变的Criterion.
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public Page<T> findByCriteria(Page<T> page, Criterion... criterion) {
		Assert.notNull(page);

		Criteria c = createCriteria(criterion);

		if(page.hasExpression()) {
			Map<String, String> alias = JSONHiExpression.parseAlias(page);
			Set<String> set = alias.keySet();
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = alias.get(key);
				c.createAlias(key, value);
			}

			Criterion cri = JSONHiExpression.parseExpressonForCriteria(page);
			if(cri != null) {
				c.add(cri);
			}
		}

		int totalCount = 0;
		if(page.isAutoCount()) {
			totalCount = countQueryResult(page, c);
			page.setTotalCount(totalCount);
		}
		if(page.isFirstSetted()) {
			c.setFirstResult(page.getFirst());
		}
		if(page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());
		}
		if(page.isOrderBySetted()) {
			String[][] orders = this.buildPageOrder(page);
			for(String[] order : orders) {
				String field = order[0];
				String dir = order[1];
				if(dir.equals(QueryParameter.ASC)) {
					c.addOrder(Order.asc(field));
				} else {
					c.addOrder(Order.desc(field));
				}
			}

		}

		List<T> result = c.list();
		if(logger.isDebugEnabled()) {
			logger.debug("查询表达式：" + c.toString());
		}
		if(totalCount <= 0) {
			result = new ArrayList<T>();
		}
		page.setResult(result);
		return page;
	}

	private String[][] buildPageOrder(Page<T> page) {
		Object fields = null;
		try{
			fields = JSONDecoder.decode(page.getOrderBy());
		}
		catch(Exception e)
		{
			fields = page.getOrderBy();
		}
		Object orders = null;
		try{
			orders = JSONDecoder.decode(page.getOrder());
		}
		catch(Exception e)
		{
			orders = page.getOrder();
		}
		String[][] pageOrder =  null;
		if ( fields instanceof String )
		{
			pageOrder = new String[1][2];
			pageOrder[0][0] = (String)fields;
			pageOrder[0][1] = (String)orders;;
		}
		else if ( fields instanceof List )
		{
			List<String> fs = (List<String>)fields;
			List<String> ds = (List<String>)orders;
			pageOrder = new String[fs.size()][2];
			for ( int i = 0;i < fs.size();i++ )
			{
				pageOrder[i][0] = fs.get(i);
				pageOrder[i][1] = ds.get(i);
			}
		}

		return pageOrder;
	}

	/**
	 * 按属性查找对象列表.
	 */
	public List<T> findByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		return createCriteria(Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 按属性查找唯一对象.
	 */
	public T findUniqueByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(Restrictions.eq(propertyName, value)).uniqueResult();
	}

	/**
	 * 按属性查找唯一对象.
	 */
	public T findTopOneByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		List<T> result = createCriteria(Restrictions.eq(propertyName, value)).list();
		if(CollectionUtils.isNotEmpty(result)) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if(values != null) {
			for(int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for(Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 指定值的实体对象是否存在
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public boolean exist(String propertyName, Object value) {
		Object object = findUniqueByProperty(propertyName, value);
		return (object != null);
	}

	protected <B> int countQueryResult(Page<B> page, Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = (List<OrderEntry>) ReflectionUtils.getFieldValue(impl, "orderEntries");
		ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList<OrderEntry>());

		// 执行Count查询
		int totalCount = ((Number) c.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		if(totalCount < 1) {
			return -1;
		}

		// 将之前的Projection和OrderBy条件重新设回去
		c.setProjection(projection);

		if(projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if(transformer != null) {
			c.setResultTransformer(transformer);
		}

		ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		return totalCount;
	}

	public List<?> findALLBySQL(String sql) {
		List<?> list = null;
		try {
			list = getSession().createSQLQuery(sql).list();
		} catch(Exception e) {
			logger.error("SQL 查询异常:{}" + e.getMessage());
		}

		return list;
	}

	public List<T> findALLObjsBySQL(String sql) {
		List<T> list = null;
		try {
			list = getSession().createSQLQuery(sql).addEntity(entityClass).list();
		} catch(Exception e) {
			logger.error("SQL 查询异常:{}" + e.getMessage());
		}
		return list;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<T> findALLByHQLQuery(Query query) {
		return query.list();
	}

	/**
	 * @param sql
	 * @return
	 */
	public Integer executeUpdateSQL(String sql) {
		try {
			return getSession().createQuery(sql).executeUpdate();
		} catch(Exception e) {
			logger.error("SQL 执行异常:{}" + e.getMessage());
		}

		return 0;
	}

	/**
	 * @param hql
	 * @param values
	 * @return
	 */
	public Integer executeUpdateHQL(String hql, Object... values) {
		try {
			Query queryObject = getSession().createQuery(hql);
			if(values != null) {
				for(int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.executeUpdate();
		} catch(Exception e) {
			logger.error("Hsq 执行异常:{}" + e.getMessage());
		}

		return 0;
	}

	public List<T> getListByIds(PK... ids) {
		Query query = createByIdsQuery();
		return this.findALLByHQLQuery(query.setParameterList("ids", ids));
	}

	public List<T> getListByIds(List<PK> ids) {
		Query query = createByIdsQuery();
		return this.findALLByHQLQuery(query.setParameterList("ids", ids));
	}

	private Query createByIdsQuery() {
		String hql = "from " + entityClass.getSimpleName() + " e where e.id in(:ids)";
		return getSession().createQuery(hql);
	}

	public List<T> getListByNames(String... names) {
		Query query = createByNamesQuery();
		return this.findALLByHQLQuery(query.setParameterList("names", names));
	}

	private Query createByNamesQuery() {
		String hql = "from " + entityClass.getSimpleName() + " e where e.name in(:names)";
		return getSession().createQuery(hql);
	}
}
