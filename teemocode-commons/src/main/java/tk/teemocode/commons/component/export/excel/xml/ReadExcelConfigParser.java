package tk.teemocode.commons.component.export.excel.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import tk.teemocode.commons.component.export.excel.Column;
import tk.teemocode.commons.component.export.excel.ReadExcelConfig;
import tk.teemocode.commons.component.export.excel.Type;
import tk.teemocode.commons.util.XmlParser;

public class ReadExcelConfigParser extends XmlParser {
	@SuppressWarnings("unchecked")
	@Override
	public void parse(Element root, Map<String, Object> results) {
		Attribute name = root.attribute("name");
		Attribute className = root.attribute("className");
		Attribute sheetName = root.attribute("sheetName");
		Attribute hasHeader = root.attribute("hasHeader");
		ReadExcelConfig config = new ReadExcelConfig();
		config.setName(name.getValue());
		config.setSheetName(sheetName == null ? null : sheetName.getValue());
		config.setClassName(className.getValue());
		if(hasHeader == null) {
			config.setHasHeader(true);
		} else if(Boolean.valueOf(hasHeader.getValue())) {
			config.setHasHeader(true);
			Attribute dataFromRow = root.attribute("dataFromRow");
			if(dataFromRow != null) {
				config.setDataFromRow(Integer.parseInt(dataFromRow.getValue()));
			}
		} else {
			config.setHasHeader(false);
		}

		List<Column> list = new ArrayList<Column>();

		List<Element> elements = root.elements("cloumn");
		for(int i = 0; i < elements.size(); i++) {
			Element el = elements.get(i);
			Attribute colName = el.attribute("name");
			Attribute isMapKeyAttr = el.attribute("isMapKey");
			Column col;
			if(isMapKeyAttr != null) {
				Attribute mapKeyValueAttr = el.attribute("mapKeyValue");
				if(mapKeyValueAttr == null || colName == null) {
					throw new IllegalArgumentException("mapKeyValue or colName cannot be null when isMapKey=true");
				}
				col = new Column(colName.getValue(), getType(el));
				col.setMapKey(true);
				col.setMapKeyValue(mapKeyValueAttr.getValue());
			} else if(colName != null) {
				col = new Column(colName.getValue(), getType(el));
			} else {
				col = new Column();
			}
			list.add(col);
		}
		config.setColumns(list);
		results.put(name.getValue(), config);
	}
	private Type getType(Element el) {
			Element colType = el.element("type");
			Attribute dataType = colType.attribute("dataType");

			Type type = new Type();
			Attribute format = colType.attribute("format");
			if(format != null) {
				type.setFormat(format.getValue());
			}
			if(dataType == null) {
				type.setDataType(Type.DATA_TYPE_STRING);
			} else if(dataType.getValue().equals(Type.DATA_TYPE_OBJECT)) {
				Attribute typeName = colType.attribute("name");
				Attribute typeClassName = colType.attribute("className");
				Attribute subDataType = colType.attribute("subDataType");
				type.setDataType(dataType.getValue());
				type.setName(typeName.getValue());
				type.setClassName(typeClassName.getValue());
				type.setSubDataType(subDataType.getValue());
		} else if(dataType.getValue().equals(Type.DATA_TYPE_DATE)) {
			type.setDataType(dataType.getValue());
			Attribute subDataType = colType.attribute("subDataType");
			type.setSubDataType(subDataType == null ? null : subDataType.getValue());
			} else {
				type.setDataType(dataType.getValue());

		}
		return type;
	}
}
