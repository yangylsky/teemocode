package tk.teemocode.module.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import tk.teemocode.commons.component.async.disruptor.DisruptorEvent;
import tk.teemocode.commons.component.async.disruptor.DisruptorFactory;
import tk.teemocode.commons.component.async.disruptor.DisruptorProcessor;
import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.bo.BO;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.dao.IDao;
import tk.teemocode.module.index.convertor.ConvertorCache;
import tk.teemocode.module.index.convertor.IndexItemConvertor;
import tk.teemocode.module.search.Indexable;

public class CommonUtil {
	protected static ApplicationContext appContext;

	protected CommonUtil() {}

	protected static IDao dao;

	public static void init(ApplicationContext ctx) {
		appContext = ctx;
		if(dao == null) {
			dao = (IDao) getBean("dao");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) appContext.getBean(name);
	}

	public static <N extends Number> List<Long> convertToLong(List<N> values) {
		List<Long> l = new ArrayList<Long>();
		for(Number v : values) {
			l.add(v.longValue());
		}
		return l;
	}

	//==========================DAO, BO Method==============================//

	public static <T> List<T> getAllBos(Class<T> clazz) {
		return dao.getAllBos(clazz);
	}

	public static <T> Page<T> getPageBos(Page<T> page) {
		return dao.findPage(page.getTClass(), page);
	}

	public static <B extends IBO> B getBoById(Class<B> clazz, Long id) {
		return dao.getById(clazz, id);
	}

	public static <B extends IBO> List<B> getBosByIds(Class<B> clazz, Long... ids) {
		return dao.getListByIds(clazz, ids);
	}

	public static <B extends IBO> List<B> getBosByUuids(Class<B> clazz, List<String> uuids) {
		return dao.getListByUuids(clazz, uuids == null ? null : uuids.toArray(new String[0]));
	}

	public static <B extends IBO> List<B> getBosByUuids(Class<B> clazz, String... uuids) {
		return dao.getListByUuids(clazz, uuids);
	}

	public static <B extends IBO> B getBoByUuid(Class<B> clazz, String uuid) {
		return dao.getByUuid(clazz, uuid);
	}

	public static <B extends IBO> B getBoByNo(Class<B> clazz, String no) {
		return dao.getByNo(clazz, no);
	}

	public static <B extends IBO> B getBoByName(Class<B> clazz, String name) {
		return dao.getByName(clazz, name);
	}

	public static <B extends IBO> B getBoByProperty(Class<B> clazz, String propertyName, Object propertyValue) {
		return dao.getUniqueByProperty(clazz, propertyName, propertyValue);
	}

	public static <T> T queryUnique(String hql, Object... values) {
		return dao.queryUnique(hql, values);
	}

	public static <T> T queryTopOne(String hql, Object... values) {
		return dao.queryTopOne(hql, values);
	}

	public static <T> List<T> queryList(String hql, Object... values) {
		return dao.queryList(hql, values);
	}

	/**
	 * 根据对象ID集合, 整理合并集合.
	 *
	 * 页面发送变更后的子对象id列表时,在Hibernate中删除整个原来的子对象集合再根据页面id列表创建一个全新的集合这种看似最简单的做法是不行的.
	 * 因此采用如此的整合算法：在源集合中删除id不在目标集合中的对象,根据目标集合中的id创建对象并添加到源集合中.
	 * 因为新建对象只有ID被赋值, 因此本函数不适合于cascade-save-or-update自动持久化子对象的设置.
	 *
	 * @param srcObjects 源集合,元素为对象.
	 * @param ids  目标集合,元素为ID.
	 * @param clazz  集合中对象的类型,必须为IdEntity子类
	 */
	public static <T extends BO> void mergeByIds(final Collection<T> srcObjects, final Collection<Long> ids,
			final Class<T> clazz) {

		//目标集合为空, 删除源集合中所有对象后直接返回.
		if (ids == null) {
			srcObjects.clear();
			return;
		}

		//遍历源对象集合,如果其id不在目标ID集合中的对象删除.
		//同时,在目标集合中删除已在源集合中的id,使得目标集合中剩下的id均为源集合中没有的id.
		Iterator<T> srcIterator = srcObjects.iterator();
		try {
			while(srcIterator.hasNext()) {
				T element = srcIterator.next();
				Long id = element.getId();

				if(!ids.contains(id)) {
					srcIterator.remove();
				} else {
					ids.remove(id);
				}
			}

			//ID集合目前剩余的id均不在源集合中,创建对象,为id属性赋值并添加到源集合中.
			for(Long id : ids) {
				T element = clazz.newInstance();
				element.setId(id);
				srcObjects.add(element);
			}
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 根据ID集合,创建对象集合.
	 *
	 *
	 * @param ids  目标集合,元素为ID.
	 * @param clazz  集合中对象的类型,必须为IdEntity子类
	 * @return
	 */
	public static <T extends IBO> List<T> createByIds(final Collection<Long> ids, final Class<T> clazz) {
		try{
			List<T> list = new ArrayList<T>();
			for (Long id : ids) {
				T element = clazz.newInstance();
				element.setId(id);
				list.add(element);
			}

			return list;
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	public static DisruptorProcessor<?, Indexable> getCreateIndexItemProcessor() {
		return DisruptorFactory.getProcessor(SystemConstant.Disruptor_Topic_CreateItem);
	}

	public static DisruptorProcessor<?, Indexable> getDeleteIndexItemProcessor() {
		return DisruptorFactory.getProcessor(SystemConstant.Disruptor_Topic_DeleteItem);
	}

	@SuppressWarnings("unchecked")
	public static <E extends DisruptorEvent<T>, T> DisruptorProcessor<E, T> getAsyncProcessor(String topic) {
		return (DisruptorProcessor<E, T>) DisruptorFactory.getProcessor(topic);
	}

	/**
	 * 创建或更新索引项
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <B extends IBO> void createIndexItem(B bo) {
		Set<IndexItemConvertor> convertors = ConvertorCache.getConvertor(bo.getClass());
		for(IndexItemConvertor convertor : convertors) {
			Indexable indexItem = convertor.convert(bo);
			CommonUtil.getCreateIndexItemProcessor().send(indexItem);
		}
	}

	/**
	 * 删除索引项
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <B extends IBO> void deleteIndexItem(B bo) {
		Set<IndexItemConvertor> convertors = ConvertorCache.getConvertor(bo.getClass());
		for(IndexItemConvertor convertor : convertors) {
			Indexable indexItem = convertor.convert(bo);
			CommonUtil.getDeleteIndexItemProcessor().send(indexItem);
		}
	}
}
