package tk.teemocode.module.dfs.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.module.dfs.DfsException;
import tk.teemocode.module.dfs.model.FileEntity;
import tk.teemocode.module.dfs.model.FileMetadata;
import tk.teemocode.module.dfs.model.FileSummary;
import tk.teemocode.module.dfs.model.PutFileResult;
import tk.teemocode.module.dfs.service.FileService;

public class LocalFileService implements FileService {
	private static Logger logger = LoggerFactory.getLogger(LocalFileService.class);

	private String basePath = "/img_teemocode";

	private String endpoint = "http://img.teemocode.com/";

	protected LocalFileService() {
	}

	public LocalFileService(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	@Override
	public void createBucket(String bucketName) {
		try {
			File bucketDir = new File(basePath + File.separator + bucketName);
			FileUtils.forceMkdir(bucketDir);
		} catch(IOException e) {
			logger.error("创建Bucket[" + bucketName + "]失败", e);
			throw new DfsException(e);
		}
	}

	@Override
	public void deleteBucket(String bucketName) {
		File bucketPath = getBucketPath(bucketName);
		if(ArrayUtils.isNotEmpty(bucketPath.list())) {
			throw new DfsException("不能删除非空Bucket[" + bucketName + "]");
		}
		try {
			FileUtils.forceDeleteOnExit(bucketPath);
		} catch(IOException e) {
			logger.error("删除Bucket[" + bucketName + "]失败", e);
			throw new DfsException(e);
		}
	}

	@Override
	public boolean isBucketExist(String bucketName) {
		File bucketPath = getBucketPath(bucketName, false);
		return bucketPath.exists();
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
			File bucketPath = getBucketPath(bucketName);
			File destFile = new File(bucketPath.getAbsolutePath() + File.separator + key);
			FileUtils.copyInputStreamToFile(content, destFile);
			Long checkSum = FileUtils.checksumCRC32(destFile);
		    return new PutFileResult(checkSum.toString());
		} catch(IOException e) {
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
		File bucketPath = getBucketPath(bucketName);
		File destFile = new File(bucketPath.getAbsolutePath() + File.separator + key);
		boolean deleteSucceed = FileUtils.deleteQuietly(destFile);
		if(!deleteSucceed) {
			logger.warn("删除文件失败[" + bucketName + ":" + key + "]");
		}
	}

	@Override
	public List<FileSummary> listFileSummaries(String bucketName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileEntity getFileEntity(String bucketName, String key) {
		try {
			File bucketPath = getBucketPath(bucketName);
			File destFile = new File(bucketPath.getAbsolutePath() + File.separator + key);
			if(!destFile.exists()) {
				return null;
			}
			FileEntity fileEntity = new FileEntity();
			fileEntity.setBucketName(bucketName);
			fileEntity.setKey(key);
			fileEntity.setObjectContent(new FileInputStream(destFile));
			fileEntity.setObjectMetadata(new FileMetadata());
			return fileEntity;
		} catch(IOException e) {
			logger.error("获取文件失败[" + bucketName + ":" + key + "]", e);
			throw new DfsException(e);
		}
	}

	@Override
	public FileMetadata getFileMetadata(String bucketName, String key) {
		//目前版本不存储FileMetadata
		return new FileMetadata();
	}

	@Override
	public byte[] getFileContent(String bucketName, String key) {
		try {
			File bucketPath = getBucketPath(bucketName);
			File destFile = new File(bucketPath.getAbsolutePath() + File.separator + key);
			if(!destFile.exists()) {
				return null;
			}
			return FileUtils.readFileToByteArray(destFile);
		} catch(IOException e) {
			logger.error("", e);
			throw new DfsException(e);
		}
	}

	private File getBucketPath(String bucketName) {
		return getBucketPath(bucketName, true);
	}

	private File getBucketPath(String bucketName, boolean createIfNotExist) {
		File bucketDir = new File(basePath + File.separator + bucketName);
		if(!bucketDir.exists()) {
			if(createIfNotExist) {
				bucketDir.mkdirs();
			} else {
				return null;
			}
		}
		if(!bucketDir.isDirectory()) {
			throw new DfsException("Bucket[" + bucketName + "]不是一个目录");
		}
		return bucketDir;
	}
}
