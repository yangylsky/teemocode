package com.teemocode.commons.component.export.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.teemocode.commons.component.export.excel.ExcelUtil;

public class TestExcel {
	public static void main(String[] args) throws IOException {
		TestExcel test = new TestExcel();
		String readFileName = "c:/temp/read.xls";
		String writeFileName = "c:/temp/write.xls";
		File foder = new File("c:/temp/");
		if(!foder.exists()) {
			foder.mkdir();
		}
		File file = new File(readFileName);

		if(file.exists()) {
			test.readExcel(readFileName);
			test.writeExcel(writeFileName);
		} else {
			file.createNewFile();
			test.writeExcel(readFileName);
			test.readExcel(readFileName);
			test.writeExcel(writeFileName);
		}
	}

	private void readExcel(String fileName) {
		List<?> list = ExcelUtil.readExcel(fileName);
		for(int i = 0; i < list.size(); i++) {
			Object[] res = (Object[]) list.get(i);
			for(Object re : res) {
				System.out.print(re == null ? " ," : re + ",");
			}
			System.out.println();
		}
	}

	private void writeExcel(String fileName) {
		List<Object[]> wList = new ArrayList<Object[]>();
		Object[] row1 = new Object[3];
		row1[0] = "\u5B8B\u4F53";
		row1[1] = new Double(123);

		row1[2] = new Date(System.currentTimeMillis());
		wList.add(row1);

		Object[] row2 = new Object[3];
		row2[0] = null;
		row2[1] = null;
		row2[2] = null;
		wList.add(row2);

		ExcelUtil.writeExcel(wList, fileName);
	}
}
