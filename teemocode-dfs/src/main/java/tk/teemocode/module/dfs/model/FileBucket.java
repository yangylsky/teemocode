package tk.teemocode.module.dfs.model;

import com.aliyun.openservices.oss.model.Bucket;

/**
 * 文件容器单位。按大的类别进行文件存储，如：系统文件，公共文件，私有文件等。
 * @author yangylsky
 *
 */
public class FileBucket extends Bucket {
	public static final FileBucket Bucket_Img_Sys = new FileBucket("teemocode_img_sys");

	public static final FileBucket Bucket_Img_Public = new FileBucket("teemocode_img_public");

	public static final FileBucket Bucket_Img_Private = new FileBucket("teemocode_img_private");

	public FileBucket() {
		super();
	}

	public FileBucket(String name) {
		super(name);
	}

	public FileBucket(Bucket bucket) {
		super(bucket.getName());

		setOwner(bucket.getOwner());
		setCreationDate(bucket.getCreationDate());
	}
}
