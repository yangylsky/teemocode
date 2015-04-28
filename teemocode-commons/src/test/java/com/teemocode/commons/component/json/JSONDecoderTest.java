package com.teemocode.commons.component.json;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.teemocode.commons.component.json.JSONDecoder;
import com.teemocode.commons.component.json.JSONEncoder;

public class JSONDecoderTest {
	@Test(enabled = true)
	public void testDecode() {
		String json = "{\"bool\":{\"must\":[{\"match\":{\"groupId\":{\"query\":\"6c0ba713c0a211e392790aedb984d9a7\",\"zero_terms_query\":\"all\"}}},{\"term\":{\"type\":101}}]}}";
		Object jsonObj = JSONDecoder.decode(json);
		//		System.out.println(jsonObj);
		String newJson = JSONEncoder.encode(jsonObj);
		Assert.assertEquals(json, newJson);
	}
}
