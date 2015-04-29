package tk.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.ObjectMetadata;
import tk.teemocode.commons.util.reflect.ReflectionUtils;

/**
 * 文件元数据信息(含用户自定义元数据信息)
 * @author yangylsky
 *
 */
public class FileMetadata extends ObjectMetadata {
	public FileMetadata() {
		super();
	}

	public FileMetadata(ObjectMetadata objectMetadata) {
		this();

		setUserMetadata(objectMetadata.getUserMetadata());
		ReflectionUtils.setFieldValue(this, "metadata", ReflectionUtils.getFieldValue(objectMetadata, "metadata"));
	}

	@Override
	public void setContentLength(long contentLength) {
		if(contentLength > 307200L) {
			throw new IllegalArgumentException("内容长度不能超过300K字节。");
		}

		super.setContentLength(contentLength);
	}
}
