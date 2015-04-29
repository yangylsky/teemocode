package tk.teemocode.module.base.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import tk.teemocode.module.base.bo.IBO;

public interface LocalService<B extends IBO> extends BaseLocalService {
	public B get(Long id);

	public B load(Long id);

	public B saveOrUpdate(B obj);

	public void delete(B obj);

	public void deleteByIds(Long... ids);

	public void deleteByUuids(String... uuids);

	public boolean updateTag4Delete(B obj);

	/**
	 * 批量设置对象的tag为删除状态
	 * @param uuids
	 * @return 操作成功的uuids
	 */
	public Set<String> updateTag4Delete(String... uuids);

	/**
	 * 上架
	 * @param uuids
	 * @return 是否实际变更了状态
	 */
	public boolean putOnByUuid(String uuid);

	/**
	 * 上架
	 * @param obj
	 * @return 是否实际变更了状态
	 */
	public boolean putOn(B obj);

	/**
	 * 批量上架
	 * @param uuids
	 * @return 操作成功的uuids
	 */
	public Set<String> batchPutOnByUuids(String... uuids);

	/**
	 * 下架
	 * @param obj
	 * @return 是否实际变更了状态
	 */
	public boolean pullOff(B obj);

	/**
	 * 下架
	 * @param uuids
	 * @return 是否实际变更了状态
	 */
	public boolean pullOffByUuid(String uuid);

	/**
	 * 批量下架
	 * @param uuids
	 * @return 操作成功的uuids
	 */
	public Set<String> batchPullOffByUuids(String... uuids);

	public List<B> getAll();

	public List<B> getListByIds(Long... ids);

	public List<B> getListByIds(Collection<Long> ids);

	public List<B> getListByUuids(String... uuids);

	public List<B> getListByUuids(Collection<String> uuids);

	public B getByName(String name);

	public B getByNo(String no);

	public B getByUuid(String uuid);

	public String getSimpleName();

	public List<B> getListByHql(String hql, Object... values);

	public int getCountByHql(String hql, Object...value);

	public List<B> getByProperty(String propertyName, Object value);

	public B getUniqueByProperty(String propertyName, Object value);

	/**
	 * 指定字段值的实体是否存在
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public boolean exist(String propertyName, Object value);
}
