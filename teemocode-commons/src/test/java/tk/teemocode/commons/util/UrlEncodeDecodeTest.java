package tk.teemocode.commons.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UrlEncodeDecodeTest {
	private static String originUrl = "http://localhost:8080/cas/login?time=1259348273921";

	private static String encodedUrl = "http%3A%2F%2Flocalhost%3A8080%2Fcas%2Flogin%3Ftime%3D1259348273921";

	@Test
	public void testDecodeUrl() throws UnsupportedEncodingException {
		String url = URLDecoder.decode(encodedUrl, "UTF-8");
		System.out.println(url);
		Assert.assertEquals(url, originUrl);
	}

	@Test
	public void testEncodeUrl() throws UnsupportedEncodingException {
		String url = URLEncoder.encode(originUrl, "UTF-8");
		System.out.println(url);
		Assert.assertEquals(url, encodedUrl);
	}
}
