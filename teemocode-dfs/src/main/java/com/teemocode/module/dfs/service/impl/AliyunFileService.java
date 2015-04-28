package com.teemocode.module.dfs.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.common.utils.IOUtils;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.teemocode.module.dfs.DfsException;
import com.teemocode.module.dfs.model.FileEntity;
import com.teemocode.module.dfs.model.FileMetadata;
import com.teemocode.module.dfs.model.FileSummary;
import com.teemocode.module.dfs.model.PutFileResult;
import com.teemocode.module.dfs.service.FileService;

public class AliyunFileService implements FileService {
	private static Logger logger = LoggerFactory.getLogger(AliyunFileService.class);

	private static String key = "<key>";

	private static String secret = "<secret>";

	private static String endpoint = "http://files.teemocode.com/";

	private static OSSClient client;

	static {
		try {
			client = new OSSClient(endpoint, key, secret);
		} catch(Throwable t) {
			logger.warn("阿里云OSS客户端创建失败", t);
			throw new DfsException(t);
		}
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	@Override
	public void createBucket(String bucketName) {
		try {
			client.createBucket(bucketName);
		} catch(OSSException | ClientException e) {
			logger.error("创建Bucket[" + bucketName + "]失败", e);
			throw new DfsException(e);
		}
	}

	@Override
	public void deleteBucket(String bucketName) {
		try {
			client.deleteBucket(bucketName);
		} catch(OSSException | ClientException e) {
			logger.error("删除Bucket[" + bucketName + "]失败", e);
			throw new DfsException(e);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isBucketExist(String bucketName) {
		try {
			return client.doesBucketExist(bucketName);
		} catch(OSSException | ClientException e) {
			throw new DfsException(e);
		}
	}

	@Override
	public PutFileResult putFile(String bucketName, String key, String filePath) {
		File file = new File(filePath);
		try {
			InputStream content = new FileInputStream(file);
			return putFile(bucketName, key, content, file.length());
		} catch(FileNotFoundException e) {
			throw new DfsException("文件不存在：" + filePath);
		}
	}

	@Override
	public PutFileResult putFile(String bucketName, String key, InputStream content, long length) {
	    // 创建上传File的Metadata
		FileMetadata meta = new FileMetadata();

	    // 必须设置ContentLength
	    meta.setContentLength(length);

		try {
		    PutObjectResult putResult = client.putObject(bucketName, convertKey(key), content, meta);
		    return new PutFileResult(putResult);
		} catch(OSSException | ClientException e) {
			logger.error("发送文件失败[" + bucketName + ":" + key + "]", e);
			throw new DfsException(e);
		} finally {
		    if(content != null) {
			    try {
					content.close();
				} catch(IOException e) {
					logger.error("", e);
				}
		    }
		}
	}

	@Override
	public void deleteFile(String bucketName, String key) {
		try {
			client.deleteObject(bucketName, convertKey(key));
		} catch(OSSException | ClientException e) {
			logger.error("删除文件失败[" + bucketName + ":" + key + "]", e);
			throw new DfsException(e);
		}
	}

	@Override
	public List<FileSummary> listFileSummaries(String bucketName) {
		try {
			ObjectListing listing = client.listObjects(bucketName);
			List<FileSummary> fileSummaries = new ArrayList<>();
			for(OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
				fileSummaries.add(new FileSummary(objectSummary));
			}
			return fileSummaries;
		} catch(OSSException | ClientException e) {
			logger.error("获取文件列表[" + bucketName + "]失败", e);
			throw new DfsException(e);
		}
	}

	@Override
	public FileEntity getFileEntity(String bucketName, String key) {
		try {
			OSSObject ossObject = client.getObject(bucketName, convertKey(key));
			return new FileEntity(ossObject);
		} catch(OSSException | ClientException e) {
			logger.error("获取文件失败[" + bucketName + ":" + key + "]", e);
			throw new DfsException(e);
		}
	}

	@Override
	public FileMetadata getFileMetadata(String bucketName, String key) {
		try {
			ObjectMetadata objectMetadata = client.getObjectMetadata(bucketName, convertKey(key));
			return new FileMetadata(objectMetadata);
		} catch(OSSException | ClientException e) {
			logger.error("获取文件元数据失败[" + bucketName + ":" + key + "]", e);
			throw new DfsException(e);
		}
	}

	@Override
	public byte[] getFileContent(String bucketName, String key) {
		InputStream fileContent = getFileEntity(bucketName, key).getObjectContent();
		try {
			return IOUtils.readStreamAsBytesArray(fileContent);
		} catch(IOException e) {
			logger.error("", e);
			throw new DfsException(e);
		} finally {
			IOUtils.safeClose(fileContent);
		}
	}

	private String convertKey(String key) {
		return StringUtils.replaceChars(key, "\\", "/");
	}
}
