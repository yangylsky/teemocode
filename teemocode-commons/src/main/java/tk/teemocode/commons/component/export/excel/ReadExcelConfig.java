package tk.teemocode.commons.component.export.excel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.teemocode.commons.util.DateUtil;
import tk.teemocode.commons.util.reflect.PojoUtil;

public class ReadExcelConfig {
	private final Log LOG = LogFactory.getLog(ReadExcelConfig.class);

	private String name;

	private String className;

	private String sheetName;

	private boolean hasHeader = true;

	private int dataFromRow = 1;

	private List<Column> columns = new ArrayList<Column>();

	private Map<String, Object> objMap = new HashMap<String, Object>();

	private String defaultDatePattern = DateUtil.getDatePattern();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
		if(!hasHeader) {
			dataFromRow = 0;
		}
	}

	public int getDataFromRow() {
		return dataFromRow;
	}

	public void setDataFromRow(int dataFromRow) {
		this.dataFromRow = dataFromRow;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Object> getObjectList(List<Object[]> list) throws ParseExcelException {
		List<Object> returnList = new ArrayList<Object>();
		for(int i = hasHeader ? dataFromRow : 0; i < list.size(); i++) {
			try {
				Object obj = getObject(i, list.get(i));
				returnList.add(obj);
			} catch(IllegalAccessException e) {
				LOG.error(e.getStackTrace());
				throw new ParseExcelException(e.getMessage());
			} catch(ClassNotFoundException e) {
				LOG.error(e.getStackTrace());
				e.printStackTrace();
				throw new ParseExcelException(e.getMessage());
			} catch(InstantiationException e) {
				LOG.error(e.getStackTrace());
			}
		}
		return returnList;
	}

	public Object getObject(int rowNum, Object[] objs) throws ParseExcelException, InstantiationException, IllegalAccessException,
	ClassNotFoundException {
		Object obj = Class.forName(className).newInstance();
		int errorColNum = 1;
		try {
			for(int i = 0; i < columns.size(); i++) {
				Column col = columns.get(i);
				if(col.isValidColumn() && i < objs.length) {
					errorColNum = i + 1;
					setTypeValue(obj, col, objs[i]);
				}
			}
			PojoUtil.mapToObj(objMap, obj);
			objMap.clear();
			return obj;
		} catch(Exception e) {
			e.printStackTrace();
			throw new ParseExcelException("错误(行: " + (rowNum + 1) + ", 列: " + errorColNum + ")");
		}
	}

	@SuppressWarnings("unchecked")
	private void setTypeValue(Object obj, Column col, Object value) throws IllegalAccessException,
	ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		Type type = col.getType();
		if(col.isMapKey()) {
			Map<String, Object> map = (Map<String, Object>) PropertyUtils.getProperty(obj, col.getName());
			if(map == null) {
				map = new HashMap<String, Object>();
			}
			value = getTypeValue(value, type.getDataType());
			//为map时value type仅支持简单类型
			if(value != null) {
				map.put(col.getMapKeyValue(), value);
			}
			PropertyUtils.setProperty(obj, col.getName(), map);
		} else {
			String dataType = type.getDataType();
			Object typeObj = obj;
			if(dataType.equals(Type.DATA_TYPE_OBJECT)) {
				String typeName = type.getName();
				String className = type.getClassName();
				String subDataType = type.getSubDataType();
				if(objMap.containsKey(typeName)) {
					typeObj = objMap.get(typeName);
				} else {
					typeObj = Class.forName(className).newInstance();
					objMap.put(typeName, typeObj);
				}
				value = getTypeValue(value, subDataType);
			} else if(dataType.equals(Type.DATA_TYPE_DATE)) {
				value = getDateValue(value, type);
			} else {
				value = getTypeValue(value, dataType);
			}
			if(value != null) {
				PojoUtil.valueToObj(col.getName(), value, typeObj);
			}
		}
	}

	private Object getDateValue(Object value, Type type) {
		if(!(value instanceof Date)) {
			if(value instanceof Double) {
				Double dValue = (Double) value;
				if(dValue > 190000) {
					value = parseDateValue(String.valueOf(dValue.longValue()), type.getSubDataType());
				} else {
					value = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) value);
				}
			} else if(value instanceof String) {
				if(!StringUtils.isBlank((String) value)){
					value = parseDateValue((String) value, type.getSubDataType());
				}else{
					value = null;
				}
			}
		}
		return value;
	}

	private Date parseDateValue(String s, String pattern) {
		if(pattern == null) {
			pattern = defaultDatePattern;
		}
		return DateUtil.convertStringToDate(pattern, s);
	}

	private Object getTypeValue(Object value, String type) {
		if(type.equals(Type.DATA_TYPE_STRING)) {
			if(!(value instanceof String)) {
				if(value instanceof Double) {
					double vDouble = ((Double) value).doubleValue();
					long vLong = ((Double) value).longValue();
					if(vDouble == vLong) {//如果小数位全是0
						value = String.valueOf(((Double) value).longValue());
					} else {
						value = String.valueOf(((Double) value).doubleValue());
					}
				} else {
					value = "" + (value == null ? "" : value);
				}
			}
		} else if(type.equals(Type.DATA_TYPE_DOUBLE)) {
			if(!(value instanceof Double)) {
				if(value instanceof String) {
					if(((String) value).trim().equals("")){
						value= null;
					}else{
						value = Double.valueOf((String) value);
					}
				} else if(value instanceof Number) {
					value = new Double(((Number) value).doubleValue());
				}
			}
		} else if(type.equals(Type.DATA_TYPE_FLOAT)) {
			if(!(value instanceof Float)) {
				if(value instanceof String) {
					value = Float.valueOf((String) value);
				} else if(value instanceof Number) {
					value = new Float(((Number) value).floatValue());
				}
			}
		} else if(type.equals(Type.DATA_TYPE_INTEGER)) {
			if(!(value instanceof Integer)) {
				if(value instanceof String) {
					if(((String) value).trim().equals("")){
						value = null;
					}else{
						value = Integer.valueOf((String) value);
					}
				} else if(value instanceof Number) {
					value = new Integer(((Number) value).intValue());
				}
			}
		} else if(type.equals(Type.DATA_TYPE_LONG)) {
			if(!(value instanceof Long)) {
				if(value instanceof String) {
					value = Long.valueOf((String) value);
				} else if(value instanceof Number) {
					value = new Long(((Number) value).longValue());
				}
			}
		} else if(type.equals(Type.DATA_TYPE_BOOLEAN)) {
			if(!(value instanceof Boolean)) {
				if(value instanceof String) {
					value = Boolean.valueOf((String) value);
				}
			}
		} else {
			value = "" + (value == null ? "" : value);
		}

		return value;
	}

	public String getDefaultDatePattern() {
		return defaultDatePattern;
	}

	public void setDefaultDatePattern(String defaultDatePattern) {
		this.defaultDatePattern = defaultDatePattern;
	}
}
