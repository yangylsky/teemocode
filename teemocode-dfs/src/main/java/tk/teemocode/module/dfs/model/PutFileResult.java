package tk.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.PutObjectResult;

/**
 * 存储文件的结果
 * @author yangylsky
 *
 */
public class PutFileResult extends PutObjectResult {
	public PutFileResult() {
		super();
	}

	public PutFileResult(String eTag) {
		this();

		setETag(eTag);
	}

	public PutFileResult(PutObjectResult putObjectResult) {
		super();

		setETag(putObjectResult.getETag());
	}
}
