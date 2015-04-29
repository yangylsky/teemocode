package tk.teemocode.web.upload;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import tk.teemocode.commons.exception.ProjectException;
import tk.teemocode.commons.util.UUIDUtil;
import tk.teemocode.module.dfs.model.FileBucket;
import tk.teemocode.module.dfs.model.FileOwner;
import tk.teemocode.module.dfs.model.FileSummary;
import tk.teemocode.module.dfs.model.PutFileResult;
import tk.teemocode.module.dfs.service.FileService;
import tk.teemocode.module.util.SystemEnv;

public class FileUploadUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

	private FileUploadUtil() {
	}

	/**
	 * 上传一个公用图片文件，文件将放入<code>FileBucket.Bucket_Img_Public</code>
	 * @param filedata
	 * @return
	 */
	public static FileSummary uploadPublicImageFile(MultipartFile filedata) {
		return uploadImageFile(filedata, true, FileBucket.Bucket_Img_Public.getName(), null);
	}

	/**
	 * 上传一个用户图片文件，文件将放入<code>FileBucket.Bucket_Img_Private</code>，文件的key为：userId/uuid.扩展名
	 * @param filedata
	 * @param userId
	 * @return
	 */
	public static FileSummary uploadImageFile(MultipartFile filedata, String userId) {
		if(StringUtils.isBlank(userId)) {
			throw new UploadException("用户ID不能为空");
		}
		return uploadImageFile(filedata, true, FileBucket.Bucket_Img_Private.getName(), userId);
	}

	/**
	 * 上传一个公用文件，文件将放入<code>FileBucket.Bucket_Img_Public</code>
	 * @param filedata
	 * @return
	 */
	public static FileSummary uploadPublicFile(MultipartFile filedata) {
		return uploadImageFile(filedata, false, FileBucket.Bucket_Img_Public.getName(), null);
	}

	/**
	 * 上传一个用户文件，文件将放入<code>FileBucket.Bucket_Img_Private</code>，文件的key为：userId/uuid.扩展名
	 * @param filedata
	 * @param userId
	 * @return
	 */
	public static FileSummary uploadFile(MultipartFile filedata, String userId) {
		if(StringUtils.isBlank(userId)) {
			throw new UploadException("用户ID不能为空");
		}
		return uploadImageFile(filedata, false, FileBucket.Bucket_Img_Private.getName(), userId);
	}

	/**
	 * 上传文件，返回文件摘要信息
	 * @param filedata - 文件内容
	 * @param mustImg - 文件必须是图片
	 * @param bucket - 文件容器单位
	 * @param owner - 文件所有者
	 * @return 文件摘要信息
	 */
	private static FileSummary uploadImageFile(MultipartFile filedata, boolean mustImg, String bucketName, String userId) {
		String originalFilename = filedata.getOriginalFilename();
		int idx = originalFilename.lastIndexOf(".");
		if(idx == -1) {
			throw new ProjectException("上传的文件名错误[扩展名为空]");
		}
		String suffix = filedata.getOriginalFilename().substring(idx);
		if(mustImg && !isImageFile(suffix)) {
			throw new ProjectException("上传的文件不是图片");
		}

		String fileName = UUIDUtil.generate() + suffix;
		String key = userId != null ? userId + "/" + fileName : fileName;
		try {
			FileService fileService = SystemEnv.getBean("dfsFileService");
			PutFileResult result = fileService.putFile(bucketName, key, filedata.getInputStream(), filedata.getSize());
			FileSummary summary = new FileSummary();
			summary.setBucketName(bucketName);
			summary.setKey(key);
			summary.setETag(result.getETag());
			summary.setOwner(userId != null ? new FileOwner(userId) : null);
			summary.setSize(filedata.getSize());
			summary.setLastModified(new Date());
			summary.setStorageClass(suffix);
			return summary;
		} catch(IOException e) {
			logger.error("", e);
			throw new UploadException(e);
		}
	}

	private static boolean isImageFile(String suffix) {
		return suffix.equalsIgnoreCase(".JPG") || suffix.equalsIgnoreCase(".JPEG") || suffix.equalsIgnoreCase(".PNG")
				|| suffix.equalsIgnoreCase(".GIF") || suffix.equalsIgnoreCase(".BMP") || suffix.equalsIgnoreCase(".TIF");
	}
}
