package tk.teemocode.commons.component.export.excel;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ColumnModel {

	private String header;

	private String dataIndex;

	private String objColumn;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}

	public String getObjColumn() {
		return objColumn;
	}

	public void setObjColumn(String objColumn) {
		this.objColumn = objColumn;
	}

	public void parseAliasAndPropertys(Map<String, String> alias, List<String> propertys) {
		String property = getDataIndex();
		String objColumn = getObjColumn();
		if (StringUtils.isNotBlank(objColumn)) {
			alias.put(property, property);
			if (objColumn.indexOf(".") != -1) {
				String[] colArr = objColumn.split("\\.");
				for (int i = 0; i < colArr.length - 1; i++) {
					String col = colArr[i];
					String value = property;
					if (i > 0) {
						value = colArr[i - 1];
					}
					alias.put(value + "." + col, col);
				}
				property = colArr[colArr.length - 2] + "." + colArr[colArr.length - 1];
			} else {
				property += "." + objColumn;
			}
		}
		propertys.add(property);
	}

}
