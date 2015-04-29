package tk.teemocode.commons.util;

import java.io.IOException;

import org.testng.annotations.Test;

import tk.teemocode.commons.util.ZipUtils;

public class ZipUtilTest {
	@Test(enabled = false)
	public void testZipFile() throws IOException {
		String toZipPath = "C:\\temp\\test";
		String zipFileRootPath = "C:\\temp";
		long startTime = System.currentTimeMillis();
		ZipUtils.zipFile(toZipPath, zipFileRootPath, "中文.zip");
		System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");
	}
}
