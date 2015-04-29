package tk.teemocode.module.base.bo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import tk.teemocode.commons.component.hibernate.HibernateUtil;
import tk.teemocode.commons.component.json.JSONEncoder;
import tk.teemocode.commons.exception.ReflectException;
import tk.teemocode.commons.util.Constants;
import tk.teemocode.commons.util.UUIDUtil;
import tk.teemocode.module.util.SystemConstant;

@MappedSuperclass
public abstract class BO implements IBO {
	private static final long serialVersionUID = 32113114666181814L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 48, unique = true)
	private String uuid;

	private Integer tag;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public Integer getTag() {
		return tag;
	}

	@Override
	public void setTag(Integer tag) {
		this.tag = tag;
	}

	//==========================Domain Business Method==============================//

	@Override
	public boolean equals(Object o) {
		if(isSameType(o)) {
			IBO bo = (IBO) o;
			if(getId() != null && bo.getId() != null) {
				return getId().equals(bo.getId());
			}
		}
		return false;
	}

	protected boolean isSameType(Object o) {
		return HibernateUtil.getEntityClass(this.getClass()).isInstance(o)
				|| (o != null && HibernateUtil.getEntityClass(o.getClass()).isInstance(this));
	}

	@Override
	public int hashCode() {
		return getId() == null ? -1 : toString().hashCode();
	}

	public int compareTo(IBO anotherBo) {
		if(getId() != null && anotherBo != null && anotherBo.getId() != null) {
			return hashCode() - anotherBo.hashCode();
		}
		return -1;
	}

	@Override
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	@Transient
	public Object get(String name) {
		try {
			return PropertyUtils.getNestedProperty(this, name);
		} catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ReflectException(e);
		}
	}

	@Override
	public BO set(String name, Object value) {
		try {
			PropertyUtils.setNestedProperty(this, name, value);
		} catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ReflectException(e);
		}
		return this;
	}

	@Override
	public boolean canRepeatPutOn() {
		return false;	//默认不支持重复上架
	}

	@Override
	public boolean isPutOn() {
		return getTag() != null && getTag() == SystemConstant.Tag_PutOn;
	}

	@Override
	public boolean isPulledOff() {
		return getTag() != null && getTag() == SystemConstant.Tag_PullOff;
	}

	@Override
	public BO checkBO() {
		if(isValidId()) {
			return this;
		} else {
			setId(null);
		}
		return null;
	}

	/**
	 * 检测Id是否合法
	 * @return
	 */
	@Override
	public boolean isValidId() {
		return isValidId(getId());
	}

	@Override
	public boolean isValidUuid() {
		return isValidUuid(uuid);
	}

	/**
	 * 是否是新创建的实体
	 * @return
	 */
	public boolean isNew() {
		return !isValidId();
	}

	@Override
	public String generateUuid() {
		String newUuid = UUIDUtil.generate();
		setUuid(newUuid);
		return newUuid;
	}

	public static BO checkBO(BO bo) {
		if(bo == null) {
			return null;
		}
		return bo.checkBO();
	}

	public static Long parseBoId(IBO bo) {
		if(bo != null) {
			Long boId = bo.getId();
			if(isValidId(boId)) {
				return boId;
			}
		}
		return null;
	}

	public static boolean isValidId(Long boId) {
		return boId != null && boId > 0;
	}

	public static boolean isValidUuid(String uuid) {
		return StringUtils.isNotBlank(uuid);
	}

	public static <B extends IBO> String[] getBoIdsArray(Collection<B> bos) {
		return getBoIds(bos).toArray(new String[] {});
	}

	public static <B extends IBO> List<Long> getBoIds(Collection<B> bos) {
		List<Long> ids = new ArrayList<>();
		if(bos != null) {
			for(B bo : bos) {
				if(bo != null) {
					ids.add(bo.getId());
				}
			}
		}
		return ids;
	}

	public static <B extends IBO> B getBo(Collection<B> col, Long id) {
		if(col != null && id != null) {
			for(B b : col) {
				if(b != null && id.equals(b.getId())) {
					return b;
				}
			}
		}
		return null;
	}

	public static String getIdsString(IBO[] bos) {
		String text = "";
		for(IBO bo : bos) {
			if(bo != null && bo.getId() != null) {
				text += Constants.Separator1 + bo.getId().toString() + Constants.Separator1;
			}
		}
		return text;
	}

	public static <B extends IBO> String getJsonIds(List<B> bos) {
		return JSONEncoder.encode(bos == null ? new ArrayList<Long>() : getBoIds(bos));
	}

	public static <T> void setItems(Collection<T> destCol, Collection<T> srcCol) {
		destCol.clear();
		if(srcCol != null) {
			for(T obj : srcCol) {
				destCol.add(obj);
			}
		}
	}

	public static <B extends IBO> void addItem(Collection<B> col, B item) {
		if(!col.contains(item)) {
			col.add(item);
		}
	}

	public static <B extends IBO> void removeItem(Collection<B> col, B item) {
		if(col != null && col.contains(item)) {
			col.remove(item);
		}
	}

	public static String notNull(String v) {
		return v == null ? "" : v;
	}

	public static Integer notNull(Integer v) {
		return v == null ? 0 : v;
	}

	public static Double notNull(Double v) {
		return v == null ? 0 : v;
	}

	public static String[] notNull(String[] arr) {
		return arr == null ? new String[0] : arr;
	}

	public static Long[] notNull(Long[] arr) {
		return arr == null ? new Long[0] : arr;
	}

	@SuppressWarnings("unchecked")
	public static <B extends IBO> List<B> notNull(List<B> c) {
		return (List<B>) notNull(c, List.class);
	}

	@SuppressWarnings("unchecked")
	public static <B extends IBO> Set<B> notNull(Set<B> c) {
		return (Set<B>) notNull(c, Set.class);
	}

	public static <B extends IBO, C extends Collection<B>> Collection<B> notNull(Collection<B> c, Class<C> clazz) {
		if(c == null) {
			if(ClassUtils.isAssignable(clazz, List.class)) {
				return new ArrayList<B>();
			} else if(ClassUtils.isAssignable(clazz, Set.class)) {
				return new HashSet<B>();
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return c;
	}

	public static final List<Long> convertToLong(List<Integer> ids) {
		List<Long> newIds = new ArrayList<>();
		for(Integer id : ids) {
			newIds.add(id.longValue());
		}
		return newIds;
	}

	// ==========================Domain Business Method==============================//

	public String getTagDesc() {
		return getTagDesc(getTag());
	}

	public static String getTagDesc(int tag) {
		switch(tag) {
			case SystemConstant.Tag_Active:
				return "未上架";
			case SystemConstant.Tag_PutOn:
				return "已上架";
			case SystemConstant.Tag_PullOff:
				return "已下架";
			case SystemConstant.Tag_Archived:
				return "已归档";
			case SystemConstant.Tag_Disabled:
				return "被禁止";
			case SystemConstant.Tag_Deleted:
				return "已删除";
			default:
				throw new IllegalStateException("状态错误");
		}
	}
}
