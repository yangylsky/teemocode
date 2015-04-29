package tk.teemocode.module.dfs.service;

import java.io.InputStream;
import java.util.List;

import tk.teemocode.module.dfs.model.FileEntity;
import tk.teemocode.module.dfs.model.FileMetadata;
import tk.teemocode.module.dfs.model.FileSummary;
import tk.teemocode.module.dfs.model.PutFileResult;

/**
 * 提供统一的文件存储接口。兼容本地文件存储，阿里云开放存储服务OSS。
 * @author yangylsky
 *
 */
public interface FileService {
	/**
	 * 返回终端访问的前缀
	 * @return
	 */
	public String getEndpoint();

	/**
	 * 创建一个文件容器单位
	 * @param bucketName
	 */
	public void createBucket(String bucketName);

	/**
	 * 删除一个文件容器单位
	 * @param bucketName
	 */
	public void deleteBucket(String bucketName);

	public boolean isBucketExist(String bucketName);

	/**
	 * 传入一个文件到指定的容器单位
	 * @param bucketName
	 * @param key
	 * @param filePath
	 * @return
	 */
	public PutFileResult putFile(String bucketName, String key, String filePath);

	/**
	 * 传入文件内容到指定的容器单位
	 * @param bucketName
	 * @param key
	 * @param content
	 * @param length
	 * @return
	 */
	public PutFileResult putFile(String bucketName, String key, InputStream content, long length);

	/**
	 * 删除指定的文件
	 * @param bucketName
	 * @param key
	 */
	public void deleteFile(String bucketName, String key);

	/**
	 * 列出一个容器单位下所有的文件摘要
	 * @param bucketName
	 * @return
	 */
	public List<FileSummary> listFileSummaries(String bucketName);

	/**
	 * 获取指定的文件完整信息
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public FileEntity getFileEntity(String bucketName, String key);

	/**
	 * 获取指定的文件元数据
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public FileMetadata getFileMetadata(String bucketName, String key);

	/**
	 * 获取指定的文件内容
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public byte[] getFileContent(String bucketName, String key);
}
