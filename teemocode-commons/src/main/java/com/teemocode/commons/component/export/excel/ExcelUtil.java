package com.teemocode.commons.component.export.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	public static List<Object[]> readExcel(String fileName) {
		return readExcel(new File(fileName));
	}

	public static List<Object[]> readExcel(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			List<Object[]> list = ExcelUtil.readExcel(in);
			in.close();
			return list;
		} catch(FileNotFoundException e) {
			logger.error("Read Excel file failed, cannot find file.", e);
			return new ArrayList<Object[]>();
		} catch(IOException e) {
			logger.error("Read Excel file failed, read error.", e);
			return new ArrayList<Object[]>();
		}
	}

	public static List<Object[]> readExcel(InputStream in) {
		try {
			List<Object[]> list = new ArrayList<Object[]>();
			Excel excel = new Excel(in, null);
			int num = excel.getLastRowNum();
			for(int i = 0; i <= num; i++) {
				HSSFRow row = excel.getRow(i);
				if(row != null) {
					int colNumber = row.getLastCellNum();
					if(colNumber<=0) {
						continue;
					}
					Object[] re = new Object[colNumber];
					boolean isNullRow = true;
					for(int j = 0; j < colNumber; j++) {
						if(row.getCell(j) != null) {
							re[j] = excel.getObjValueAt(i, j);
							if(re[j]!= null) {
								isNullRow = false;
							}
						} else {
							re[j] = null;
						}
					}
					if(!isNullRow) {
						list.add(re);
					}
				}
			}
			return list;
		} catch(Exception e) {
			logger.error("Read Excel result failed.", e);
			throw new RuntimeException("读取Excel文件内容失败,请检查文件格式!");
		}
	}

	public static void writeExcel(List<Object[]> list, String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			ExcelUtil.writeExcel(list, out);
			out.close();
		} catch(FileNotFoundException e) {
			logger.error("Write Excel file failed, cannot find file.", e);
		} catch(IOException e) {
			logger.error("Write Excel file failed, write error.", e);
		}
	}

	public static void writeExcel(List<Object[]> list, OutputStream out) {
		try {
			Excel excel = new Excel();
			excel.setColWidths(new int[] {2000, 2000, 2000});
			excel.setRowHeights(new int[] {255, 255});

			for(int i = 0; i < list.size(); i++) {
				Object[] res = list.get(i);
				for(int j = 0; j < res.length; j++) {
					HSSFCellStyle style = excel.getDefaultCellStyle();
					if(res[j] == null) {
						res[j] = "";
					}
					excel.writeObject(res[j], i, j, 1, 1, style);
				}
			}
			excel.write(out);
		} catch(Exception e) {
			logger.error("Write Excel failed.", e);
		}
	}

	public static void writeExcel(Excel excel, HSSFCellStyle[] styles, String[] headers, List<Object[]> dataList,
			OutputStream out) {
		try {
			int startIdx = 0;
			if(headers != null) {
				for(int j = 0; j < headers.length; j++) {
					HSSFFont font = excel.getDefaultFont();
					font.setBoldweight(Font.BOLDWEIGHT_BOLD);
					HSSFCellStyle style = excel.getDefaultCellStyle();
					style.setFont(font);
					excel.writeObject(headers[j], 0, j, 1, 1, style);
				}

				startIdx++;
			}
			for(int i = 0; i < dataList.size(); i++) {
				Object[] res = dataList.get(i);
				for(int j = 0; j < res.length; j++) {
					if(res[j] == null) {
						res[j] = "";
					}
					excel.writeObject(res[j], startIdx + i, j, 1, 1, styles == null ? null : styles[j]);
				}
			}

			excel.write(out);
		} catch(Exception e) {
			logger.error("Write Excel failed.", e);
		}
	}
}
