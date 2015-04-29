package tk.teemocode.module.base.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import tk.teemocode.commons.component.hibernate.HibernateUtil;
import tk.teemocode.commons.component.page.JSONHiExpression;
import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.component.page.QueryParameter;
import tk.teemocode.commons.util.Sortable;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.bo.BO;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.dao.IDao;

@SuppressWarnings("unchecked")
@Repository
public class Dao implements IDao {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	protected SessionFactory sessionFactory;

	private static Map<SessionFactory, Dao> daoCache = new HashMap<>();

	public Dao() {
	}

	protected Dao(SessionFactory sessionFactory) {
		this();
		this.sessionFactory = sessionFactory;
	}

	public static Dao getDao(SessionFactory sessionFactory) {
		Dao dao = daoCache.get(sessionFactory);
		if(dao == null) {
			dao = new Dao(sessionFactory);
			daoCache.put(sessionFactory, dao);
		}
		return dao;
	}

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void clearSession() {
		getSession().clear();
	}

	@Override
	public void flushSession() {
		flushSession(getSession());
	}

	@Override
	public void refresh(Object entity) {
		getSession().refresh(entity);
	}

	@Override
	public void evict(final Object entity) {
		getSession().evict(entity);
	}

	@Override
	public <T> void evictCache(Class<T> clazz) {
		sessionFactory.getCache().evictEntityRegion(clazz);
	}

	@Override
	public <T> void evictCache(Class<T> clazz, Long id) {
		sessionFactory.getCache().evictEntity(clazz, id);
	}

	@Override
	public <T> T get(Class<T> clazz, Long id) {
		return (T) getSession().get(clazz, id);
	}

	@Override
	public <T> T load(Class<T> clazz, Long id) {
		if(id != null) {
			try {
				return (T) getSession().load(clazz, id);
			} catch(DataAccessException e) {
				logger.error("", e);
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public <T> List<T> loadAll(Class<T> clazz) {
		Query query = getSession().createQuery("from " + clazz.getSimpleName());
		return query.list();
	}

	@Override
	public <T> List<T> find(Class<T> clazz, String hql, Object... values) {
		return (List<T>) doList(hql, values);
	}

	@Override
	public <T> List<T> queryList(String hql, Object... values) {
		return doList(hql, values);
	}

	protected <T> List<T> doList(String hql, Object... values) {
		return doList(getSession(), hql, values);
	}

	protected <T> List<T> doList(Session session, String hql, Object... values) {
		Query query = session.createQuery(HibernateUtil.removeRedundancy(hql));
		if(values != null) {
			for(int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List<T> result = query.list();
		return result == null ? new ArrayList<T>() : result;
	}

	@Override
	public <T> T queryUnique(String hql, Object... values) {
		List<T> objects = queryTopN(2, hql, values);
		if(CollectionUtils.isNotEmpty(objects)) {
			if(objects.size() == 1) {
				return objects.get(0);
			} else {
				logger.error("获取到的实体不唯一");
				throw new RuntimeException("获取到的实体不唯一，请联系系统管理员，相关信息：" + hql
						+ " - " + ArrayUtils.toString(values));
			}
		}
		return null;
	}

	@Override
	public Integer queryForInt(String hql, Object... values) {
		Integer result = queryFor(hql, values);
		return result == null ? 0 : result;
	}

	@Override
	public Double queryForDouble(String hql, Object... values) {
		Double result = queryFor(hql, values);
		return result == null ? 0d : result;
	}

	@Override
	public <T> T queryFor(String hql, Object... values) {
		Object result = queryTopOne(hql, values);
		return (T) result;
	}

	@Override
	public <T> T saveOrUpdate(T entity) {
		Session session = getSession();
		session.saveOrUpdate(entity);
		flushSession(session);
		return entity;
	}

	@Override
	public <T> T save(T entity) {
		Session session = getSession();
		session.save(entity);
		flushSession(session);
		return entity;
	}

	@Override
	public <T> T merge(T entity) {
		Session session = getSession();
		session.merge(entity);
		flushSession(session);
		return entity;
	}

	@Override
	public void delete(Object entity) {
		getSession().delete(entity);
	}

	@Override
	public <T> void deleteById(Class<T> clazz, Long id) {
		T bo = getById(clazz, id);
		delete(bo);
	}

	@Override
	public <T> void deleteByUuid(Class<T> clazz, String uuid) {
		T bo = getByUuid(clazz, uuid);
		delete(bo);
	}

	@Override
	public <T> void deleteByName(Class<T> clazz, String name) {
		T bo = getByName(clazz, name);
		delete(bo);
	}

	@Override
	public <T> void batchDelete(Collection<T> entities) {
		Session session = getSession();
		int i = 0;
		for(T entity : entities) {
			session.delete(entity);
			if(i % 20 == 0) {
				session.flush();
				session.clear();
			}
			i++;
		}
	}

	@Override
	public <T> void batchSave(Collection<T> entities) {
		Session session = getSession();
		int i = 0;
		for(T entity : entities) {
			session.save(entity);
			if(i % 20 == 0) {
				session.flush();
				session.clear();
			}
			i++;
		}
	}

	@Override
	public int bulkUpdate(String hql) {
		return doExecuteUpdate(hql, new Object[0]);
	}

	@Override
	public int bulkUpdate(String hql, Object... values) {
		return doExecuteUpdate(hql, values);
	}

	protected Integer doExecuteUpdate(String hql, Object... values) {
		return doExecuteUpdate(getSession(), hql, values);
	}

	protected Integer doExecuteUpdate(Session session, String hql, Object... values) {
		Query query = session.createQuery(HibernateUtil.removeRedundancy(hql));
		if(values != null) {
			for(int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.executeUpdate();
	}

	@Override
	public <T> void saveOrUpdate(Collection<T> entities) {
		if(entities != null) {
			for(Object entity : entities) {
				getSession().saveOrUpdate(entity);
			}
		}
	}

	@Override
	public int countObjs(String hql, Object... values) {
		String q = hql.trim();
		int index = q.toLowerCase().lastIndexOf("from ");
		if(q.indexOf("group by") > index || q.indexOf(" distinct ") >= 0) {
			List<?> results = find(null, hql, values);
			if(results != null && results.size() > 0) {
				return results.size();
			} else {
				return 0;
			}
		}
		String hsql = null;
		List<?> objs = null;
		if(index >= 0) {
			hsql = hql.substring(index);
			hsql = "select count(*) " + hsql;
			objs = doList(hsql, values);
			if(objs != null && objs.size() == 1) {
				return ((Long) objs.get(0)).intValue();
			} else {
				return 0;
			}
		} else {
			logger.error("hql have not 'from'." + HibernateUtil.removeRedundancy(hql));
		}
		objs = doList(hql, values);
		return objs.size();
	}

	@Override
	public <T> Page<T> findPage(Page<T> page, String hql, Object... values) {
		if(page == null){
			page = new Page<T>();
		}
		int totalCount = page.getTotalCount();
		if(page.getTotalCount() <= 0) {
			long start = System.currentTimeMillis();
			totalCount = countObjs(hql, values);
			logger.info("countObjs:" + totalCount + " results, for " + (System.currentTimeMillis() - start) + "ms.");
		}
		return findPage(page, totalCount, hql, values);
	}

	public <T> Page<T> find(String countPrefix, Page<T> page, String hql, Object... values) {
		if(page.isAutoCount()) {
			int fromIdx = hql.indexOf("from");
			String counthql = "select count("+countPrefix+") " + hql.substring(fromIdx);
			Query q = createQuery(counthql, values);
			List<T> objs = q.list();
			int totalCount = 0;
			if(objs != null && objs.size() == 1) {
				totalCount = ((Long) objs.get(0)).intValue();
			} else {
				totalCount = 0;
			}
			page.setTotalCount(totalCount);
		}
		hql = HibernateUtil.removeRedundancy(hql);
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

	@Override
	public <T> Page<T> findPage(Page<T> page, int totalCount, String hql, Object... values) {
		if(page == null || totalCount <= 0) {
			return new Page<T>();
		}
		page.setTotalCount(totalCount);
		if(StringUtils.isNotBlank(page.getSort()) && !StringUtils.containsIgnoreCase(hql, "order by")) {
			hql += " order by " + page.getSort() + " " + page.getDir();
		}
		Query query = createQuery(hql);
		if(page.getTotalCount() > 0) {
			page.count();
			query.setFirstResult(page.getStart());
			query.setMaxResults(page.getLimit());
		}
		if(values != null) {
			for(int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List<T> results = query.list();
		if(page.getTotalCount() <= 0) {
			if(results != null && results.size() > 0) {
				page.setTotalCount(results.size());
			} else {
				page.setTotalCount(0);
			}
			page.count();
		}
		page.setResult(results);
		return page;
	}

	protected Query createQuery(String hql, Object... values) {
		Query query = getSession().createQuery(HibernateUtil.removeRedundancy(hql));
		if(values != null) {
			for(int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	@Override
	public <T> T queryTopOne(String hql, Object... values) {
		List<T> result = queryTopN(1, hql, values);
		if(CollectionUtils.isNotEmpty(result)) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public <T> List<T> queryTopN(int topN, String hql, Object... values) {
		if(topN > 0) {
			Query q = getSession().createQuery(HibernateUtil.removeRedundancy(hql));
			q.setFirstResult(0);
			q.setMaxResults(topN);
			if(values != null) {
				for(int i = 0; i < values.length; i++) {
					q.setParameter(i, values[i]);
				}
			}
			return q.list();
		} else {
			throw new IllegalArgumentException("topN must great than 0!");
		}
	}

	@Override
	public String[] getQueryFieldNames(int type, String query){
		return HibernateUtil.getQueryFieldNames(type, query, getSession());
	}

	@Override
	public void printStatistics(Class<?>... clazz) {
		Statistics stats = getSession().getSessionFactory().getStatistics();
		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
		logger.error("Query Hit ratio:" + queryCacheHitRatio);
		for(Class<?> c : clazz) {
			EntityStatistics entityStats = stats.getEntityStatistics(c.getName());
			long changes = entityStats.getInsertCount() + entityStats.getUpdateCount() + entityStats.getDeleteCount();
			logger.error(c.getName() + " changed " + changes + "times");
		}
	}

	@Override
	public <T> T getById(Class<T> clazz, Long id) {
		long startTime = System.currentTimeMillis();
		T bo = load(clazz, id);
		try {
			if(bo != null && bo instanceof IBO) {
				((IBO) bo).getId(); //激活对象(强制代理对象从缓存或数据库填充数据)
			}
			return bo;
		} catch(ObjectNotFoundException e) {
			return null;
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			long processTime = System.currentTimeMillis() - startTime;
			if(processTime > 10 && logger.isWarnEnabled()) {
				logger.warn("load(" + clazz.getSimpleName() + ", " + id + "): " + processTime + " ms.");
			}
		}
	}

	@Override
	public <T> T getByUuid(Class<T> clazz, String uuid) {
		return getUniqueByProperty(clazz, "uuid", uuid);
	}

	@Override
	public <T> T getByNo(Class<T> clazz, String no) {
		return getUniqueByProperty(clazz, "no", no);
	}

	@Override
	public <T> T getByName(Class<T> clazz, String name) {
		return getUniqueByProperty(clazz, "name", name);
	}

	@Override
	public <T> T getUniqueByProperty(Class<T> clazz, String propertyName, Object propertyValue) {
		return (T) createCriteria(clazz, Restrictions.eq(propertyName, propertyValue)).uniqueResult();
	}

	@Override
	public <T> List<T> getAllBos(Class<T> clazz) {
		//		log.debug("getAllBos(" + clazz.getSimpleName() + ")");
		//		String hsql = "from " + clazz.getSimpleName() + " where id>0";
		//		return (List<T>) find(hsql, new Object[] {});
		return loadAll(clazz);
	}

	@Override
	public <T> Page<T> findPage(Class<T> clazz, Page<T> page, Criterion... criterion) {
		Criteria c = createCriteria(clazz, criterion);

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
			if(page.getOrder().endsWith(QueryParameter.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		} else {
			checkSortableOrder(clazz, c);
		}
		List<T> result = c.list();
		logger.debug("查询表达式：" + c.toString());
		if(totalCount <= 0) {
			result = new ArrayList<T>();
		}
		page.setResult(result);
		return page;
	}

	protected <T> Criteria createCriteria(Class<T> clazz, Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Criterion c : criterions) {
			if(c != null) {
				criteria.add(c);
			}
		}
		return criteria;
	}

	protected <T> int countQueryResult(Page<T> page, Criteria c) {
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

	@Override
	public <T> List<T> getListByIds(Class<T> clazz, Long... boIds) {
		List<T> allBos = new ArrayList<T>();
		for(Long boId : BO.notNull(boIds)) {
			T obj = getById(clazz, boId);
			if(obj != null) {
				allBos.add(obj);
			}
		}
		return allBos;
	}

	@Override
	public <T> List<T> getListByUuids(Class<T> clazz, String... uuids) {
		List<T> allBos = new ArrayList<T>();
		for(String uuid : BO.notNull(uuids)) {
			T obj = getByUuid(clazz, uuid);
			if(obj != null) {
				allBos.add(obj);
			}
		}
		return allBos;
	}

	@Override
	public <T> List<Object[]> findByCriteria(Class<T> clazz, Map<String, String> alias, List<String> propertys,
			Page<T> page, Criterion... criterion) {
		Criteria c = createCriteria(clazz, criterion);

		if(page.hasExpression()) {
			Map<String, String> _alias = JSONHiExpression.parseAlias(page);
			Set<String> set = _alias.keySet();
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = _alias.get(key);
				c.createAlias(key, value);
			}

			Criterion cri = JSONHiExpression.parseExpressonForCriteria(page);
			if(cri != null) {
				c.add(cri);
			}
		}

		//返回子对象或孙对象的列所必须设置的关联映射
		Set<String> set = alias.keySet();
		Iterator<String> iter = set.iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String value = alias.get(key);
			//c.createAlias(key, value);
			//设置映射的时候忽略已经存在的映射路径
			addAlias((CriteriaImpl)c, key, value);

		}

		// 设置投影集合
		ProjectionList proList = Projections.projectionList();
		for(String property : propertys) {
			if(StringUtils.isNotBlank(property)) {
				proList.add(Projections.groupProperty(property));
			}
		}
		c.setProjection(proList);

		if(page.isOrderBySetted()) {
			if(page.getOrder().endsWith(QueryParameter.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		} else {
			checkSortableOrder(clazz, c);
		}

		return c.list();
	}

	protected void flushSession(Session session) {
		session.flush();
	}

	protected <T> void checkSortableOrder(Class<T> clazz, Criteria c) {
		if(ClassUtils.isAssignable(clazz, Sortable.class)) {
			c.addOrder(Order.asc("sortIdx"));
		}
	}

	protected Criteria addAlias(CriteriaImpl criteriaImpl, String path, String name) {
		if(path == null) {
			return criteriaImpl;
		}
		for(Iterator<Subcriteria> iter = criteriaImpl.iterateSubcriteria(); iter.hasNext();) {
			Subcriteria subCriteria = iter.next();
			if(path.equals(subCriteria.getPath())) {
				return criteriaImpl;
			}
		}
		return criteriaImpl.createAlias(path, name);
	}
}
