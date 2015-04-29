package tk.teemocode.commons.component.export.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.util.DateUtil;

@SuppressWarnings("deprecation")
public class Excel {
	private final Logger logger = LoggerFactory.getLogger(Excel.class);

	// 3.2删除了Encoding设置，自动使用Unicode
//	private short encoding = HSSFWorkbook.ENCODING_UTF_16;
	private HSSFWorkbook workBook;

	private HSSFSheet currentSheet;

	private int[] rowHeights;

	private String DateFormat = "m/d/yy h:mm";

	public Excel() {
		workBook = new HSSFWorkbook();
		currentSheet = workBook.createSheet();
	}

	public Excel(String sheetName) {
		workBook = new HSSFWorkbook();
		currentSheet = sheetName == null ? workBook.createSheet() : workBook.createSheet(sheetName);
	}

	public Excel(InputStream in, String sheetName) {
		try {
			workBook = new HSSFWorkbook(in);
			currentSheet = sheetName == null ? workBook.getSheetAt(0) : workBook.getSheet(sheetName);
		} catch(FileNotFoundException e) {
			logger.error("Cannot find excel file.", e);
		} catch(IOException e) {
			logger.error("Read excel file error.", e);
		}
	}

	public void openNewSheet() {
		currentSheet = workBook.createSheet();
	}

	public void setCurrentSheet(int index) {
		if(workBook.getSheetAt(index) != null) {
			currentSheet = workBook.getSheetAt(index);
		}
	}

	public void setRowHeights(int[] rowHeights) {
		this.rowHeights = rowHeights;
		for(int i = 0; i < rowHeights.length; i++) {
			HSSFRow row;
			row = currentSheet.getRow(i);
			if(row == null) {
				row = currentSheet.createRow(i);
			}
			row.setHeight((short) rowHeights[i]);
		}
	}

	public void setColWidths(int[] colWs) {
		for(int i = 0; i < colWs.length; i++) {
			currentSheet.setColumnWidth(i, colWs[i]);
		}
	}

	public void writeString(String str, int row, int col, HSSFCellStyle style) {
		writeString(str, row, col, 1, 1, style);
	}

	public void writeString(String str, int startRow, int startCol, int rowSpan, int colSpan, HSSFCellStyle style) {
		// check the row number in sheet exist, create it if it does not exist;
		checkRegion(startRow, startCol, rowSpan, colSpan);
		HSSFRow row = currentSheet.getRow(startRow);
		HSSFCell cell = row.createCell(startCol);
		// 3.2删除了Encoding设置，自动使用Unicode
//		cell.setEncoding(encoding);
		cell.setCellValue(new HSSFRichTextString(str));
		if(style != null) {
			cell.setCellStyle(style);
		} else {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		if(rowSpan > 1 || colSpan > 1) {
			addMergedRegionWithStyle(startRow, startCol, rowSpan, colSpan);
		}
	}

	public void writeObject(Object obj, int startRow, int startCol, int rowSpan, int colSpan, HSSFCellStyle style) {
		// check the row number in sheet exist, create it if it does not exist;
		checkRegion(startRow, startCol, rowSpan, colSpan);
		HSSFRow row = currentSheet.getRow(startRow);
		HSSFCell cell = row.createCell(startCol);
		// 3.2删除了Encoding设置，自动使用Unicode
//		cell.setEncoding(encoding);
//		if(style == null) {
//			style = getDefaultCellStyle();
//		}

		Object value = obj;
		if(value instanceof Date) {
			if(style != null) {
				style.setDataFormat(HSSFDataFormat.getBuiltinFormat(getDateFormat()));
			}
			Date date = (Date) value;
			Date theDate = new Date(date.getYear(), date.getMonth(), date.getDate());
			if(date.equals(theDate)) {
				value = DateUtil.formatDate(date);
			} else {
				value = DateUtil.formatTime(date);
			}
		}
		if(style != null) {
			cell.setCellStyle(style);
		}

		setCellObjType(cell, value);
		setCellObjValue(cell, value);

		if(rowSpan > 1 || colSpan > 1) {
			addMergedRegionWithStyle(startRow, startCol, rowSpan, colSpan);
		}
	}

	private void addMergedRegionWithStyle(int startRow, int startCol, int rowSpan, int colSpan) {
		HSSFCellStyle style = currentSheet.getRow(startRow).getCell(startCol).getCellStyle();
		for(int i = startRow; i < startRow + rowSpan; i++) {
			HSSFRow row = currentSheet.getRow(i);
			for(int j = startCol; j < startCol + colSpan; j++) {
				HSSFCell cell = row.getCell(j);
				if(cell == null) {
					cell = row.createCell(j);
				}
				cell.setCellStyle(style);
			}
		}
		currentSheet.addMergedRegion(new CellRangeAddress(startRow, startRow + rowSpan - 1, startCol, startCol + colSpan - 1));
	}

	public void write(OutputStream os) {
		try {
			workBook.write(os);
		} catch(IOException e) {
			logger.error("Write excel file error.", e);
		}
	}

	public HSSFCellStyle createStyle() {
		return workBook.createCellStyle();
	}

	public HSSFFont createFont() {
		return workBook.createFont();
	}

	public String getStringValueAt(int rowNumber, int colNumber) {
		HSSFRow row = currentSheet.getRow(rowNumber);
		if(row == null) {
			return null;
		}
		HSSFCell cell = row.getCell(colNumber);
		if(cell == null) {
			return null;
		}

		String re = null;
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			re = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			re = cell.getBooleanCellValue() + "";
			break;
		case Cell.CELL_TYPE_ERROR:
			re = cell.getErrorCellValue() + "";
			break;
		case Cell.CELL_TYPE_STRING:
			re = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			re = cell.getNumericCellValue() + "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			re = cell.getCellFormula();
			break;
		}
		return re;
	}

	public Object getObjValueAt(int rowNumber, int colNumber) {
		HSSFRow row = currentSheet.getRow(rowNumber);
		if(row == null) {
			return null;
		}
		HSSFCell cell = row.getCell(colNumber);
		if(cell == null) {
			return null;
		}

		Object re = null;
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			re = null;
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			re = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			re = cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_STRING:
			re = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			re = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			re = cell.getCellFormula();
			break;
		}
		return re;
	}

	public HSSFRow getRow(int rowNumber) {
		HSSFRow row = currentSheet.getRow(rowNumber);
		if(row == null) {
			return null;
		}
		return row;
	}

	public int getLastRowNum() {
		return currentSheet.getLastRowNum();
	}

	private boolean checkRowExist(int rowNumber) {

		HSSFRow row = currentSheet.getRow(rowNumber);
		if(row == null) {
			row = currentSheet.createRow(rowNumber);
			if(rowHeights != null && rowHeights.length > rowNumber){
				row.setHeight((short) rowHeights[rowNumber]);
			}else{
				row.setHeight((short) ((rowHeights!=null && rowHeights.length>0)?rowHeights[0]:255));
			}
			return false;
		}
		return true;
	}

	private boolean checkCellExist(int rowNumber, int colNumber) {
		HSSFRow row = currentSheet.getRow(rowNumber);
		if(row == null) {
			checkRowExist(rowNumber);
			row = currentSheet.getRow(rowNumber);
		}
		HSSFCell cell = row.getCell(colNumber);
		if(cell == null) {
			row.createCell(colNumber);
			return false;
		}
		return true;
	}

	private void checkRegion(int startRow, int startCol, int rowSpan, int colSpan) {
		for(int i = startRow; i < startRow + rowSpan; i++) {
			checkRowExist(i);
			for(int j = startCol; j < startCol + colSpan; j++) {
				checkCellExist(i, j);
			}
		}
	}

	private void setCellObjType(HSSFCell cell, Object obj) {
		if(obj instanceof String) {
			String sValue = (String) obj;
			if(StringUtils.isBlank(sValue)) {
				cell.setCellType(Cell.CELL_TYPE_BLANK);
			} else {
				if(StringUtils.isNumeric(sValue) && !sValue.startsWith("0")) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				} else {
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
			}
		} else if(obj instanceof Number) {
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if(obj instanceof Date) {
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
	}

	private void setCellObjValue(HSSFCell cell, Object obj) {
		if(obj instanceof Number) {
			Number nValue = (Number) obj;
			cell.setCellValue(nValue.doubleValue());
		} else if(obj instanceof String) {
			cell.setCellValue(new HSSFRichTextString(String.valueOf(obj)));
		} else if(obj instanceof Date) {
			cell.setCellValue((Date) obj);
		} else {
			cell.setCellValue(new HSSFRichTextString(String.valueOf(obj)));
		}
	}

	public HSSFCellStyle getDefaultCellStyle() {
		HSSFCellStyle style = createStyle();
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setWrapText(false);
		style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		HSSFFont font = getDefaultFont();
		style.setFont(font);
		return style;
	}

	public HSSFFont getDefaultFont() {
		HSSFFont font = createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("\u5B8B\u4F53");
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font.setItalic(false);

		return font;
	}

	//3.2删除了Encoding设置，自动使用Unicode
//	public short getEncoding() {
//		return encoding;
//	}
//
//	public void setEncoding(short encoding) {
//		this.encoding = encoding;
//	}

	public String getDateFormat() {
		return DateFormat;
	}

	public void setDateFormat(String dateFormat) {
		DateFormat = dateFormat;
	}
}
