package com.teemocode.commons.component.export.rtf;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import com.teemocode.commons.exception.ProjectException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

public class RtfExportUtil {

	public static final int Para_Align_Left = Paragraph.ALIGN_LEFT;

	public static final int Para_Align_Right = Paragraph.ALIGN_RIGHT;

	public static final int Para_Align_Center = Paragraph.ALIGN_CENTER;

	public static final int Font_Normal = Font.NORMAL;

	public static final int Font_Bold = Font.BOLD;

	public static final int Font_Italic = Font.ITALIC;

	public static final int Font_UnderLine = Font.UNDERLINE;

	public static void writeTable(Document document, List<HashMap<Integer, String>> tableMap){
		try {
			HashMap<Integer, String> headerMap = tableMap.remove(0);
			Table table = new Table(headerMap.size());
			table.setBorderWidth(1);
			table.setBorderColor(Color.BLACK);
			table.setPadding(2);
			table.setSpacing(0);
			table.setAlignment(Table.ALIGN_CENTER);
			int[] widths = {10,50,10,12};
			table.setWidths(widths);
			table.setWidth(94);

			Font headerFont = getFont("C:/Windows/fonts/MSYHBD.TTF", 12, Font_Bold, Color.BLACK);
			writeRow(table, headerMap, headerFont, Para_Align_Center);
			Font rowFont = getFont("C:/Windows/fonts/MSYHBD.TTF", 10, Font_Normal, Color.BLACK);
			for (HashMap<Integer, String> rowMap : tableMap){
				writeRow(table, rowMap, rowFont, Para_Align_Left);
			}
			document.add(table);
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	private static void writeRow(Table table, HashMap<Integer, String> rowMap, Font font, int align){
		try {
			for(int i = 1; i <= rowMap.size(); i++) {
				Paragraph para = new Paragraph(rowMap.get(i), font);
				para.setAlignment(align);
				Cell cell = new Cell(para);
				table.addCell(cell);
			}
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	public static void writeImages(Document document, List<String> pics, int align){
		try {
			Paragraph para = new Paragraph();
			for (String picPath : pics){
				Image image = Image.getInstance(picPath);
				image.scaleToFit(100f, 100f);
				para.setAlignment(align);
				para.add(image);
			}
			document.add(para);
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	public static void writeImage(Document document, String imgPath,int align){
		try {
			Image image = Image.getInstance(imgPath);
			image.scaleAbsolute(350f, 250f);
			image.setAlignment(align);
			document.add(image);
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	public static void writeText(Document document, String text, int align, Font font){
		try {
			Paragraph para = new Paragraph(text, font);
			para.setFont(font);
			para.setAlignment(align);
			document.add(para);
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void writeHtmlText(Document document, String text, int align, Font font){
		try {
			Paragraph para = new Paragraph();
			StyleSheet ss = new StyleSheet();
			List<Element> htmlList = HTMLWorker.parseToList(new StringReader(text), ss);
	        for (Element element : htmlList) {
	        	para.add(element);
	        }
//			para.setFont(font);
//			para.setAlignment(align);
			document.add(para);
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	public static Document createDocument(String filePath){
		try {
			Document document = new Document(PageSize.A4);
			RtfWriter2.getInstance(document, new FileOutputStream(filePath));
			return document;
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}

	public static Font getFont(String fontUrl, int fontSize, int fontStyle, Color fontColor){
		try {
			BaseFont baseFont = BaseFont.createFont(fontUrl, BaseFont.IDENTITY_H ,BaseFont.NOT_EMBEDDED);
			return new Font(baseFont, fontSize, fontStyle, fontColor);
		} catch(Exception e) {
			throw new ProjectException(e);
		}
	}
}
