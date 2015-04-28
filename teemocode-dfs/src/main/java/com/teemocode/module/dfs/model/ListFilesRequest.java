package com.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.ListObjectsRequest;

/**
 * 列表文件的条件集
 * @author yangylsky
 *
 */
public class ListFilesRequest extends ListObjectsRequest {
	public ListFilesRequest() {
		super();
	}

	public ListFilesRequest(ListObjectsRequest listObjectsRequest) {
		super(listObjectsRequest.getBucketName(), listObjectsRequest.getPrefix(), listObjectsRequest.getMarker(),
				listObjectsRequest.getDelimiter(), listObjectsRequest.getMaxKeys());
	}
}
