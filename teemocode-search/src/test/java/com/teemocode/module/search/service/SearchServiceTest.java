package com.teemocode.module.search.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.teemocode.commons.component.page.Page;
import com.teemocode.module.search.service.ESSearchService;
import com.teemocode.module.search.service.impl.ESSearchServiceImpl;
import com.google.gson.JsonObject;

public class SearchServiceTest {
	private ESSearchService searchService = new ESSearchServiceImpl();

	@Test(enabled = false)
	public void testSearchJsonById() {
		String json = searchService.searchJsonById("teemocode", "aaa003");
		System.out.println(json);
		Assert.assertNotNull(json);
	}

	@Test(enabled = false)
	public void testSearchJsonObjectById() {
		JsonObject jsonObject = searchService.searchJsonObjectById("teemocode", "aaa003");
		System.out.println(jsonObject);
		Assert.assertNotNull(jsonObject);
	}

	@Test(enabled = false)
	public void testSearchObjectById() {
		TestProduct product = searchService.searchObjectById(TestProduct.class, "teemocode", "aaa003");
		System.out.println(product);
		Assert.assertNotNull(product);
	}

	@Test(enabled = false)
	public void testSearchObjectsByIds() {
		Set<String> ids = new HashSet<String>(Arrays.asList(new String[] {"aaa003", "aaa005"}));
		List<TestProduct> products = searchService.searchListByIds(TestProduct.class, "teemocode", "route", ids);
		System.out.println(products);
		Assert.assertEquals(2, products.size());
	}

	@Test(enabled = false)
	public void testSearchJsonPage() {
		String json = searchService.searchJsonPage(getTestPage());
		System.out.println(json);
		Assert.assertNotNull(json);
	}

	@Test(enabled = false)
	public void testSearchJsonObjectPage() {
		Page<TestProduct> page = searchService.searchObjectPage(getTestPage());
		System.out.println(page);
		Assert.assertNotNull(page);
	}

	private Page<TestProduct> getTestPage() {
		String queryString = "{\"range\":{\"createDate\":{\"from\":\"2014-02-21T14:12:12\", \"to\":\"2014-02-22T14:12:12\"}}}";
		Page<TestProduct> page = new Page<>(TestProduct.class);
		page.setStart(2);
		page.setPageSize(10);
		page.setQueryExpression(queryString);
		page.setIndexNames(new String[] {"teemocode"});
		page.setTypeNames(new String[] {"route"});
		return page;
	}
}
