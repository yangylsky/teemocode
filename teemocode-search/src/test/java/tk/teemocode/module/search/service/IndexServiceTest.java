package tk.teemocode.module.search.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.util.PinyinUtil;
import tk.teemocode.module.search.service.ESIndexService;
import tk.teemocode.module.search.service.ESSearchService;
import tk.teemocode.module.search.service.impl.ESIndexServiceImpl;
import tk.teemocode.module.search.service.impl.ESSearchServiceImpl;

public class IndexServiceTest {
	private ESIndexService indexService = new ESIndexServiceImpl();

	private ESSearchService searchService = new ESSearchServiceImpl();

	@Test(enabled = false)
	public void testCreateIndex() throws Exception {
		indexService.createIndex(TestProduct.DEFAULT_INDEX_NAME);
		Thread.sleep(1000);
		Assert.assertTrue(indexService.indexExists(TestProduct.DEFAULT_INDEX_NAME));
	}

	@Test(enabled = false)
	public void testDeleteIndex() throws Exception {
		indexService.deleteIndex(TestProduct.DEFAULT_INDEX_NAME);
		Thread.sleep(1000);
		Assert.assertFalse(indexService.indexExists(TestProduct.DEFAULT_INDEX_NAME));
	}

	@Test(enabled = false)
	public void testCreateItem() throws Exception {
		TestProduct product = createItem();
		TestProduct result = searchService.searchObjectById(TestProduct.class, product.getIndexName(), product.getId());
		Assert.assertEquals(product, result);
	}

	private TestProduct createItem() throws Exception {
		TestProduct product = new TestProduct();
		product.setUuid("rrr-001");
		product.setNo("BJ-001");
		product.setName("北京一地5日游");
		product.setBrief("北京一地5日游天安门天坛八达岭222");
		indexService.createItem(product);
		Thread.sleep(20);
		return product;
	}

	@Test(enabled = false)
	public void testDeleteItem() throws Exception {
		TestProduct product = createItem();
		TestProduct result = searchService.searchObjectById(TestProduct.class, product.getIndexName(), product.getId());
		Assert.assertEquals(product, result);

		indexService.deleteItem(product.getIndexName(), product.getTypeName(), product.getId());
		Thread.sleep(20);
		result = searchService.searchObjectById(TestProduct.class, product.getIndexName(), product.getId());
		Assert.assertNull(result);
	}

	@Test(enabled = false)
	public void testBulkCreateItems() throws Exception {
		if(!indexService.indexExists(TestProduct.DEFAULT_INDEX_NAME)) {
			indexService.createIndex(TestProduct.DEFAULT_INDEX_NAME);
		} else {
			indexService.deleteIndex(TestProduct.DEFAULT_INDEX_NAME);
			Thread.sleep(1000);
		}
		long start = System.currentTimeMillis();
		List<TestProduct> products = new ArrayList<>();
		for(int i = 1; i <= 10000; i++) {
			TestProduct product = new TestProduct();
			product.setUuid("rrr-000" + i);
			product.setNo("BJ-000" + i);
			product.setName("北京一地" + i + "日游");
			product.setDescription("北京一地" + i + "日游");
			product.setGroupUuid(String.valueOf(i / 100));
			product.setUserUuid(String.valueOf(i / 10));
			product.setVersion(1);
			product.setCreateDate(new Date());
			product.setModifyDate(new Date());
			product.setCountry("中国");
			product.setDestination("北京、故宫、景山、北海、八达岭、定陵、天坛、颐和园、阅微草堂、观国家博物馆、逛什刹海");
			product.setCityStart("昆明");
			product.setDays(i);
			product.setPrice1(2500d);
			product.setPrice2(2000d);
			product.setRecommendProfit(500d);
			product.setTitleImg("test.jpg");
			product.setSortIdx(i);
			product.setBrief("北京一地" + i + "日游"
					+ "“全家福集体照一张” 每日矿泉水奉上 品尝北京特色小吃"
					+ "\n畅游之旅—游故宫、景山、北海、八达岭、定陵、天坛、颐和园、阅微草堂、观国家博物馆、逛什刹海、王府井步行街、前门大栅栏、"
					+ "\n开心之旅—赏地道老北京表演【京韵表演】。"
					+ "\n放松之旅—宿挂三准四、普通商务等多选择。"
					+ "\n享受之旅—享充足的自由活动时间，您能身临其境的感受北京大街小巷！用心享受此次北京之行每一个美好难忘的瞬间！"
					+ "（自由活动期间，请注意自身安全）");
			PinyinUtil.updatePinyin(product, true);
			products.add(product);
			indexService.createItem(product);
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
//		indexService.bulkCreateItems(products);
//		Thread.sleep(10 * products.size());
	}

	@Test(enabled = false)
	public void testSearchCreatedItems() {
		Page<TestProduct> page = searchService.searchObjectPage(getTestPage());
		Assert.assertEquals(page.getResult().size(), page.getPageSize());
	}

	private Page<TestProduct> getTestPage() {
		String queryString = "{\"term\":{\"groupId\":\"12\"}}";
		Page<TestProduct> page = new Page<>(TestProduct.class);
		page.setStart(10);
		page.setPageSize(20);
		page.setQueryExpression(queryString);
		page.setIndexNames(new String[] {TestProduct.DEFAULT_INDEX_NAME});
		page.setTypeNames(new String[] {new TestProduct().getTypeName()});
		return page;
	}

	@Test(enabled = false)
	public void testBulkDeleteItems() throws Exception {
		List<TestProduct> products = new ArrayList<>();
		for(int i = 0; i < 1000; i++) {
			TestProduct route = new TestProduct();
			route.setUuid("rrr-000" + i);
			products.add(route);
		}

		indexService.bulkDeleteItems(products);
		Thread.sleep(10 * products.size());

		TestProduct result = searchService.searchObjectById(TestProduct.class, TestProduct.DEFAULT_INDEX_NAME, "rrr-0001");
		Assert.assertNull(result);
		result = searchService.searchObjectById(TestProduct.class, TestProduct.DEFAULT_INDEX_NAME, "rrr-0001000");
		Assert.assertNull(result);
	}
}
