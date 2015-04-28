package com.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.OSSObjectSummary;

/**
 * 文件摘要信息
 * @author yangylsky
 *
 */
public class FileSummary extends OSSObjectSummary {
	public FileSummary() {
		super();
	}

	public FileSummary(OSSObjectSummary ossObjectSummary) {
		this();

		setBucketName(ossObjectSummary.getBucketName());
		setKey(ossObjectSummary.getKey());
		setETag(ossObjectSummary.getETag());
		setSize(ossObjectSummary.getSize());
		setLastModified(ossObjectSummary.getLastModified());
		setOwner(ossObjectSummary.getOwner());
	}
}
