package com.teemocode.commons.component.export.excel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColumnModelUtil {
	private static final Logger LOG = LoggerFactory.getLogger(ColumnModelUtil.class);
	
	public static List<Object[]> getListForColumnModel(List<?> bos, List<ColumnModel> cms, Class<?> clazz) {
		try { 
			List<Object[]> objs = new ArrayList<Object[]>();
			
			// 加入字段名
			Object[] headerName = new Object[cms.size()];
			int index = 0;
			for(ColumnModel cm : cms){
				headerName[index] = cm.getHeader();
				index++;
			}
			objs.add(headerName);
			
			// 过滤list 只取columnModel中的字段
			for(Object bo : bos){
				Object[] objArr = new Object[cms.size()];
				index = 0;
				for(ColumnModel cm : cms){
					objArr[index] = getFieldValueForObject(bo, cm);
					index ++;
				}
				objs.add(objArr);
			}
			return objs;
		} catch(Exception e) {
			LOG.error("Get list for columnModel failed, cannot get list.", e);
			return new ArrayList<Object[]>();
		} 
	}
	
	/**
	 * 根据ColumnMode获取BO的属性值
	 * @param bo
	 * @param cm
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValueForObject(Object bo, ColumnModel cm) throws Exception{
		Method[] methods = bo.getClass().getMethods();
		String fieldName = cm.getDataIndex();
		for(Method method : methods){
			if(method.getName().equals(getMethodForField(fieldName))){
				String objColumn = cm.getObjColumn();
				if(objColumn != null){
					return preparObject(bo, objColumn, method);
				}
				return method.invoke(bo);
			}
		}
		return null;
	}
	
	public static Object preparObject(Object bo, String objColumn, Method method) throws Exception {
		Object _bo = method.invoke(bo);
		if(_bo != null){
			String fieldName = objColumn;
			boolean hasNext = false;
			if(objColumn.indexOf(".")!=-1){
				hasNext = true;
				fieldName = fieldName.substring(0,fieldName.indexOf("."));
			}
			Method[] medthods = method.getReturnType().getMethods();
			for(Method _method : medthods){
				if(_method.getName().equals(getMethodForField(fieldName))){
					if(hasNext){
						return preparObject(_bo, objColumn.replace(fieldName+".", ""), _method);
					}
					return _method.invoke(_bo);
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 根据字段名获取getter方法名
	 * @param filedName 字段名称
	 * @return 
	 */
	public static String getMethodForField(String fieldName){
		StringBuffer sb = new StringBuffer();        
	    sb.append("get");        
	    sb.append(fieldName.substring(0, 1).toUpperCase());        
	    sb.append(fieldName.substring(1));  
		return sb.toString();
	}
}
