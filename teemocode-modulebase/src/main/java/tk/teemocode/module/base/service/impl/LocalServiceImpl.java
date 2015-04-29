package tk.teemocode.module.base.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.commons.util.PinyinUtil;
import tk.teemocode.commons.util.Pinyinable;
import tk.teemocode.commons.util.Sortable;
import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.base.bo.DateAware;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.bo.VersionAware;
import tk.teemocode.module.base.dao.impl.BaseDao;
import tk.teemocode.module.base.service.LocalService;
import tk.teemocode.module.util.SystemConstant;
import tk.teemocode.module.util.SystemEnv;

@Service("base/LocalService")
@Transactional(rollbackFor = Throwable.class)
public class LocalServiceImpl<B extends IBO> extends BaseLocalServiceImpl implements LocalService<B> {
	private Class<B> boClass;

	protected BaseDao<B> baseDao;

	public LocalServiceImpl() {
		if(!this.getClass().equals(LocalServiceImpl.class)) {
			Type classType = this.getClass().getGenericSuperclass();
			if(classType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) classType).getActualTypeArguments();
				this.boClass = ReflectionUtils.getParamType(paramTypes[0]);
			}
		}
	}

	@Override
	@PostConstruct
	protected void initSessionFactory() {
		super.initSessionFactory();

		if(StringUtils.isNotBlank(getSessionFactoryName())) {
			SessionFactory sessionFactory = SystemEnv.getBean(getSessionFactoryName());
			this.baseDao = new BaseDao<B>(sessionFactory, getBoClass());
		}
	}

	protected String fromHql() {
		return "from " + getSimpleName();
	}

	@Override
	public String getSimpleName() {
		return getBoClass().getSimpleName();
	}

	public Class<B> getBoClass() {
		return boClass;
	}

	@Override
	@Transactional(readOnly = true)
	public B get(Long id) {
		return baseDao.get(getBoClass(), id);
	}

	@Override
	@Transactional(readOnly = true)
	public B load(Long id) {
		return baseDao.load(getBoClass(), id);
	}

	@Override
	public B saveOrUpdate(B obj) {
		validate4Save(obj);

		saveOrUpdate(obj, true);

		return obj;
	}

	protected void validate4Save(B obj) {
	}

	protected B saveOrUpdate(B obj, Boolean autoComplete) {
		if(autoComplete) {
			autoComplete(obj);
		}
		if(obj.isValidId()) {
			return baseDao.merge(obj);
		} else {
			return baseDao.saveOrUpdate(obj);
		}
	}

	@SuppressWarnings("rawtypes")
	private void autoComplete(B obj) {
		if(!obj.isValidId()) {
			obj.setId(null);
		}
		if(!obj.isValidUuid()) {
			obj.generateUuid();
		}
		if(obj.getTag() == null) {
			obj.setTag(SystemConstant.Tag_Active);
		}
		if(obj instanceof Pinyinable) {
			Pinyinable pinyinObj = (Pinyinable) obj;
			if(StringUtils.isBlank(pinyinObj.getPinyin())) {
				PinyinUtil.updatePinyin(pinyinObj);
			}
			if(StringUtils.isBlank(pinyinObj.getFullPinyin())) {
				PinyinUtil.updateFullPinyin(pinyinObj);
			}
			PinyinUtil.updatePinyin(pinyinObj, true);
		}
		if(obj instanceof DateAware) {
			DateAware dateAwareObj = (DateAware) obj;
			Date now = new Date();
			if(dateAwareObj.getCreateDate() == null) {
				dateAwareObj.setCreateDate(now);
			}
			dateAwareObj.setModifyDate(now);
		}
		if(obj instanceof VersionAware) {
			VersionAware versionAwareObj = (VersionAware) obj;
			if(versionAwareObj.getVersion() == null) {
				versionAwareObj.setVersion(1);
			}
		}
		if(obj instanceof Sortable) {
			Sortable sortableObj = (Sortable) obj;
			if(sortableObj.getSortIdx() == null) {
				sortableObj.setSortIdx(SystemConstant.SortIdx_Default);
			}
		}
	}

	@Override
	public void delete(B obj) {
		super.delete(obj);
	}

	@Override
	public void deleteByIds(Long... ids) {
		for(Long id : ids) {
			B obj = get(id);
			delete(obj);
		}
	}

	@Override
	public void deleteByUuids(String... uuids) {
		for(String uuid : uuids) {
			B obj = getByUuid(uuid);
			delete(obj);
		}
	}

	@Override
	public boolean updateTag4Delete(B obj) {
		if(obj.getTag() != null && obj.getTag() == SystemConstant.Tag_Deleted) {
			logger.debug("实体[" + obj.getUuid() + "]已标记为删除，无需再次删除");
			return false;
		}
		obj.setTag(SystemConstant.Tag_Deleted);
		saveOrUpdate(obj);
		return true;
	}

	@Override
	public Set<String> updateTag4Delete(String... uuids) {
		Set<String> changedUuids = new HashSet<>();
		if(ArrayUtils.isNotEmpty(uuids)) {
			for(String uuid : uuids) {
				B obj = getByUuid(uuid);
				if(updateTag4Delete(obj)) {
					changedUuids.add(uuid);
				}
			}
		}
		return changedUuids;
	}

	@Override
	@Transactional(readOnly = true)
	public List<B> getAll() {
		return baseDao.getAll();
	}

	@Override
	public boolean putOnByUuid(String uuid) {
		B obj = getByUuid(uuid);
		return putOn(obj);
	}

	@Override
	public boolean putOn(B obj) {
		if(obj.isPutOn() && !obj.canRepeatPutOn()) {
			logger.debug("实体[" + obj.getUuid() + "]已激活(上架)，无需再次激活(上架)");
			return false;
		}
		obj.setTag(SystemConstant.Tag_PutOn);
		if(obj instanceof VersionAware) {
			VersionAware versionAwareObj = (VersionAware) obj;
			versionAwareObj.setVersion(versionAwareObj.getVersion() + 1);
		}
		saveOrUpdate(obj);
		return true;
	}

	@Override
	public Set<String> batchPutOnByUuids(String... uuids) {
		Set<String> changedUuids = new HashSet<>();
		for(String uuid : uuids) {
			if(putOnByUuid(uuid)) {
				changedUuids.add(uuid);
			}
		}
		return changedUuids;
	}

	@Override
	public boolean pullOffByUuid(String uuid) {
		B obj = getByUuid(uuid);
		return pullOff(obj);
	}

	@Override
	public boolean pullOff(B obj) {
		if(!obj.isPutOn()) {
			logger.debug("数据[" + obj.getUuid() + "]目前不为上架状态，不能下架");
			return false;
		}
		if(obj.isPulledOff()) {
			logger.debug("数据[" + obj.getUuid() + "]已失效(下架)，无需再次失效(下架)");
			return false;
		}
		obj.setTag(SystemConstant.Tag_PullOff);
		saveOrUpdate(obj);
		return true;
	}

	@Override
	public Set<String> batchPullOffByUuids(String... uuids) {
		Set<String> changedUuids = new HashSet<>();
		for(String uuid : uuids) {
			if(pullOffByUuid(uuid)) {
				changedUuids.add(uuid);
			}
		}
		return changedUuids;
	}

	@Override
	@Transactional(readOnly = true)
	public List<B> getListByHql(String hql, Object... values) {
		return baseDao.getListByHql(hql, values);
	}

	@Override
	@Transactional(readOnly = true)
	public int getCountByHql(String hql, Object...values) {
		return baseDao.countObjs(hql, values);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean exist(String propertyName, Object value) {
		return baseDao.exist(propertyName, value);
	}

	@Transactional(readOnly = true)
	@Override
	public List<B> getByProperty(String propertyName, Object value) {
		return baseDao.getByProperty(propertyName, value);
	}

	@Transactional(readOnly = true)
	@Override
	public B getByName(String name) {
		return baseDao.getBoByName(name);
	}

	@Transactional(readOnly = true)
	@Override
	public B getByNo(String no) {
		return baseDao.getBoByNo(no);
	}

	@Override
	public B getByUuid(String uuid) {
		return baseDao.getByUuid(getBoClass(), uuid);
	}

	@Transactional(readOnly = true)
	@Override
	public B getUniqueByProperty(String propertyName, Object value) {
		return baseDao.getUniqueByProperty(propertyName, value);
	}

	@Transactional(readOnly = true)
	@Override
	public List<B> getListByIds(Long... ids) {
		return baseDao.getListByIds(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<B> getListByIds(Collection<Long> ids) {
		return baseDao.getListByIds(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<B> getListByUuids(String... uuids) {
		return baseDao.getListByUuids(uuids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<B> getListByUuids(Collection<String> uuids) {
		return baseDao.getListByUuids(uuids);
	}
}
