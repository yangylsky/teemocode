package com.teemocode.commons.component.export.excel;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.teemocode.commons.component.export.excel.ExcelUtil;
import com.teemocode.commons.component.export.excel.ReadExcelConfig;
import com.teemocode.commons.component.export.excel.xml.ReadExcelConfigParser;
import com.teemocode.commons.util.ResourceHelper;

public class TestReadExcelConfig {
	@Test(enabled = false)
	public void testReadExcel() throws Exception {
		File configFile = ResourceHelper.getSingleResource("com/hxsoft/util/export/excel/testreadexcel.xml").getFile();
		File readFile = ResourceHelper.getSingleResource("com/hxsoft/util/export/excel/test.xls").getFile();
		ReadExcelConfigParser parser = new ReadExcelConfigParser();
		Map<String, ?> map = parser.load(configFile);
		ReadExcelConfig config = (ReadExcelConfig) map.get("testBean1");
		List<Object[]> list = ExcelUtil.readExcel(readFile);
		List<?> objList = config.getObjectList(list);
		Assert.assertEquals(objList.size(), 44);
	}
}
