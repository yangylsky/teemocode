package com.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.OSSObject;

/**
 * 文件完整信息，包括元数据和文件内容
 * @author yangylsky
 *
 */
public class FileEntity extends OSSObject {
	public FileEntity() {
		super();
	}

	public FileEntity(OSSObject ossObject) {
		this();

		setBucketName(ossObject.getBucketName());
		setKey(ossObject.getKey());
		setObjectMetadata(ossObject.getObjectMetadata());
		setObjectContent(ossObject.getObjectContent());
	}
}
