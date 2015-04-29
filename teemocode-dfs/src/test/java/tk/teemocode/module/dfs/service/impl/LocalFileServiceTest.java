package tk.teemocode.module.dfs.service.impl;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import tk.teemocode.module.dfs.model.FileBucket;
import tk.teemocode.module.dfs.service.FileService;
import tk.teemocode.module.dfs.service.impl.LocalFileService;

public class LocalFileServiceTest {
	private FileService fileService = new LocalFileService("C:\\temp\\img_test");

	@Test(enabled = false, priority = 100)
	public void testCreateBucket() {
		fileService.createBucket(FileBucket.Bucket_Img_Sys.getName());
		fileService.createBucket(FileBucket.Bucket_Img_Public.getName());
		fileService.createBucket(FileBucket.Bucket_Img_Private.getName());

		Assert.assertTrue(fileService.isBucketExist(FileBucket.Bucket_Img_Sys.getName()));
		Assert.assertTrue(fileService.isBucketExist(FileBucket.Bucket_Img_Public.getName()));
		Assert.assertTrue(fileService.isBucketExist(FileBucket.Bucket_Img_Private.getName()));
	}

	@Test(enabled = false, priority = 200)
	public void testPutFile() {
		String filePath = "C:\\temp\\activity_group.gif";
		String key = "ewqewqewq-31321-dsadsa\\activity_group.gif";
		File file = new File(filePath);
		long fileSize = FileUtils.sizeOf(file);
		fileService.putFile(FileBucket.Bucket_Img_Private.getName(), key, filePath);

		byte[] fileContent = fileService.getFileContent(FileBucket.Bucket_Img_Private.getName(), key);
		Assert.assertEquals(fileSize, fileContent.length);
	}

	@Test(enabled = false, priority = 300)
	public void testDeleteFile() {
		String key = "ewqewqewq-31321-dsadsa\\activity_group.gif";
		fileService.deleteFile(FileBucket.Bucket_Img_Private.getName(), key);

		byte[] fileContent = fileService.getFileContent(FileBucket.Bucket_Img_Private.getName(), key);
		Assert.assertNull(fileContent);
	}
}
