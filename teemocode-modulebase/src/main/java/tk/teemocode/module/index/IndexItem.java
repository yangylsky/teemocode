package tk.teemocode.module.index;

import io.searchbox.annotations.JestId;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import tk.teemocode.commons.util.reflect.ReflectionUtils;
import tk.teemocode.module.search.Indexable;

public abstract class IndexItem implements Indexable, Serializable, Cloneable {
	private String indexName;

	private String typeName;

	@JestId
	private String uuid;

	private Integer tag;

	@Override
	public String getIndexName() {
		if(StringUtils.isBlank(indexName)) {
			indexName = ReflectionUtils.getStaticField(getClass(), "IndexName");
			if(StringUtils.isBlank(indexName)) {
				indexName = SearchConstant.DEFAULT_INDEX_NAME;
			}
		}
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	@Override
	public String getTypeName() {
		if(StringUtils.isBlank(typeName)) {
			typeName = ReflectionUtils.getStaticField(getClass(), "TypeName");
			if(StringUtils.isBlank(typeName)) {
				typeName = getClass().getSimpleName();
			}
		}
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getId() {
		return getUuid();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}
}
