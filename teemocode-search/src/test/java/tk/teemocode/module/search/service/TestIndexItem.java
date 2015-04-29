package tk.teemocode.module.search.service;

import io.searchbox.annotations.JestId;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import tk.teemocode.module.search.Indexable;

public abstract class TestIndexItem implements Indexable, Serializable, Cloneable {
	public static final String DEFAULT_INDEX_NAME = "teemocode-test";

	@JestId
	private String uuid;

	private String indexName;

	private String typeName;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String getIndexName() {
		return StringUtils.isBlank(indexName) ? DEFAULT_INDEX_NAME : indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	@Override
	public String getTypeName() {
		return StringUtils.isBlank(typeName) ? StringUtils.replaceChars(getClass().getName(), '.', '_') : typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getId() {
		return getUuid();
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof TestIndexItem) {
			return getId().equals(((TestIndexItem) o).getId());
		}
		return false;
	}
}
