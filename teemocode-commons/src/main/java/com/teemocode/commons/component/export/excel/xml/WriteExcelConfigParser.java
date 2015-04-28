package com.teemocode.commons.component.export.excel.xml;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.teemocode.commons.component.export.excel.Excel;
import com.teemocode.commons.util.XmlParser;

public class WriteExcelConfigParser extends XmlParser {
	@SuppressWarnings("unchecked")
	@Override
	public void parse(Element root, Map<String, Object> results) throws Exception {
//		Attribute excelName = root.attribute("name");
		Attribute sheetName = root.attribute("sheetName");
		Attribute rowHeight = root.attribute("rowHeight");
		Attribute columnWidth = root.attribute("columnWidth");
		Attribute hasHeader = root.attribute("hasHeader");
		boolean isHeader = hasHeader != null ? hasHeader.getValue().equals("true") : true;
		Excel excel = (sheetName != null && !sheetName.getValue().equals("")) ? new Excel(sheetName.getValue()) : new Excel();
		List<Element> columns = root.elements("cloumn");
//		List<HSSFCellStyle> colStyles = new ArrayList();
		int[] heights = new int[columns.size()];
		int[] widths = new int[columns.size()];

		if(rowHeight != null) {
			for(int i = 0; i < heights.length; i++) {
				heights[i] = Short.valueOf(rowHeight.getValue()).shortValue();
			}

		} else {
			for(int i = 0; i < heights.length; i++) {
				heights[i] = (short) 255;
			}
		}
		if(columnWidth != null) {
			for(int i = 0; i < widths.length; i++) {
				widths[i] = Short.valueOf(rowHeight.getValue()).shortValue();
			}
		} else {
			for(int i = 0; i < widths.length; i++) {
				widths[i] = (short) 2000;
			}
		}
		String[] titles = new String[columns.size()];
		HSSFCellStyle[] styles = new HSSFCellStyle[columns.size()];
		for(int i = 0; i < columns.size(); i++) {
			Element col = columns.get(i);
//			Attribute colName = col.attribute("name");
			Attribute title = col.attribute("title");
			Attribute width = col.attribute("width");
			if(title != null) {
				titles[i] = title.getValue();
			}
			if(width != null) {
				widths[i] = Short.valueOf(width.getValue()).shortValue();
			}

			HSSFCellStyle style = excel.getDefaultCellStyle();
			Element cell = col.element("cell");
			Attribute borderTopStyle = cell.attribute("border-top-style");
			Attribute borderLeftStyle = cell.attribute("border-left-style");
			Attribute borderBottomStyle = cell.attribute("border-bottom-style");
			Attribute borderRightStyle = cell.attribute("border-right-style");
			Attribute verticalAlign = cell.attribute("vertical-align");
			Attribute textAlign = cell.attribute("text-align");
			Attribute wordWrap = cell.attribute("word-wrap");
			Attribute backgroundColor = cell.attribute("background-color");

			if(borderTopStyle != null) {
				style.setBorderTop(Short.valueOf(borderTopStyle.getValue()).shortValue());
			}
			if(borderLeftStyle != null) {
				style.setBorderLeft(Short.valueOf(borderLeftStyle.getValue()).shortValue());
			}
			if(borderBottomStyle != null) {
				style.setBorderBottom(Short.valueOf(borderBottomStyle.getValue()).shortValue());
			}
			if(borderRightStyle != null) {
				style.setBorderRight(Short.valueOf(borderRightStyle.getValue()).shortValue());
			}
			if(verticalAlign != null) {
				style.setVerticalAlignment(Short.valueOf(verticalAlign.getValue()).shortValue());
			}
			if(textAlign != null) {
				style.setAlignment(Short.valueOf(textAlign.getValue()).shortValue());
			}
			if(wordWrap != null) {
				style.setWrapText(wordWrap.getValue().equals("true"));
			}
			if(backgroundColor != null) {
				style.setFillBackgroundColor(Short.valueOf(backgroundColor.getValue()).shortValue());
			}

			HSSFFont fontStyle = excel.getDefaultFont();
			Element font = col.element("font");
			if(font != null) {
				Attribute color = font.attribute("color");
				Attribute fontSize = font.attribute("font-size");
				Attribute fontFamily = font.attribute("font-family");
				Attribute fontWeight = font.attribute("font-weight");
				Attribute italic = font.attribute("italic");

				if(color != null) {
					fontStyle.setColor(Short.valueOf(color.getValue()).shortValue());
				}
				if(fontSize != null) {
					fontStyle.setFontHeightInPoints(Short.valueOf(fontSize.getValue()).shortValue());
				}
				if(fontFamily != null) {
					fontStyle.setFontName(fontFamily.getValue());
				}
				if(fontWeight != null) {
					fontStyle.setBoldweight(Short.valueOf(fontWeight.getValue()).shortValue());
				}
				if(italic != null) {
					fontStyle.setItalic(italic.getValue().equals("true"));
				}

				style.setFont(fontStyle);
			}

			styles[i] = style;
		}

		excel.setRowHeights(heights);
		excel.setColWidths(widths);

		results.put("excel", excel);
		results.put("styles", styles);
		results.put("columnSize", columns.size());
		if(isHeader) {
			results.put("header", titles);
		}
	}
}
