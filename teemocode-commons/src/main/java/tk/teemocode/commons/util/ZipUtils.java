package tk.teemocode.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Java1.7开始支持压缩文件里带中文文件名的文件，无需org.apache.tools.zip包的支持
 * @author yangylsky
 */
public class ZipUtils {
	public static final Log log = LogFactory.getLog(ZipUtils.class);

	public static void zipFile(String toZipFilePath, String zipFileRootPath, String zipFileName) throws IOException {
		File compressedFile = new File(toZipFilePath);
		if("".equalsIgnoreCase(zipFileName)) {
			zipFileName = toZipFilePath;
		}
		if(!zipFileRootPath.endsWith(File.separator)) {
			zipFileRootPath = zipFileRootPath + File.separator;
		}
		ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileRootPath
				+ zipFileName)));
		log.debug("compress [" + toZipFilePath + "] start!");
		ZipUtils.zipFile(compressedFile, zipOutputStream, "");
		log.debug("compress [" + toZipFilePath + "] end!");
		zipOutputStream.close();
	}

	public static void zipFile(File toZipFile, ZipOutputStream zipOutputStream, String base) throws IOException {
		try {
			if(toZipFile.isDirectory()) {
				base = base == null ? "" : base + File.separator;
				for(File f : toZipFile.listFiles()) {
					ZipUtils.zipFile(f, zipOutputStream, base + f.getName());
				}
			} else {
				if(StringUtils.isBlank(base)) {
					base = toZipFile.getName();
				}
				zipOutputStream.putNextEntry(new ZipEntry(base));
				InputStream inputStream = new BufferedInputStream(new FileInputStream(toZipFile));
				int b;
				while((b = inputStream.read()) != -1) {
					zipOutputStream.write(b);
				}
				inputStream.close();
			}
		} catch(Exception e) {
			log.error(e.getMessage());
		}
	}

	public void zipStream(InputStream in, File outputFile) throws IOException {
		OutputStream out = new FileOutputStream(outputFile);
		zipStream(in, out);
		out.close();
	}

	public void zipStream(InputStream in, OutputStream out) throws IOException {
		in = new BufferedInputStream(in);
		out = new BufferedOutputStream(out);
		ZipOutputStream zip = new ZipOutputStream(out);
		try {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = in.read(buffer)) != -1) {
				zip.write(buffer, 0, bytesRead);
			}
			zip.finish();
		} finally {
			in.close();
			if(zip != null) {
				try {
					zip.close();
				} catch(Throwable e) {
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void unzipFile(String zipFilePath, String releasePath) throws IOException {
		ZipFile zipFile = new ZipFile(zipFilePath);
		Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipFile.entries();
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		ZipEntry zipEntry = null;
		String zipEntryNameStr = "";
		String[] zipEntryNameArray = null;
		while(enumeration.hasMoreElements()) {
			zipEntry = enumeration.nextElement();
			zipEntryNameStr = zipEntry.getName();
			zipEntryNameArray = zipEntryNameStr.split("/");
			String path = releasePath;
			File root = new File(releasePath);
			if(!root.exists()) {
				root.mkdir();
			}
			for(int i = 0; i < zipEntryNameArray.length; i++) {
				if(i < zipEntryNameArray.length - 1) {
					path = path + File.separator + zipEntryNameArray[i];
					new File(path).mkdir();
				} else {
					if(zipEntryNameStr.endsWith(File.separator)) {
						new File(releasePath + zipEntryNameStr).mkdir();
					} else {
						inputStream = zipFile.getInputStream(zipEntry);
						fileOutputStream = new FileOutputStream(new File(releasePath + zipEntryNameStr));
						byte[] buf = new byte[1024];
						int len;
						while((len = inputStream.read(buf)) > 0) {
							fileOutputStream.write(buf, 0, len);
						}
						inputStream.close();
						fileOutputStream.close();
					}
				}
			}
		}
		zipFile.close();
	}
}
