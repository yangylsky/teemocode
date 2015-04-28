package com.teemocode.commons.util.data;

import java.util.List;

import org.hibernate.Session;

public class DataResult {
	private int columnCount;

	private List<Object[]> result;

	private int resultCount;

	private String[] columnNames;

	private String[] columnLabels;

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public List<Object[]> getResult() {
		return result;
	}

	public void setResult(List<Object[]> result) {
		this.result = result;
		if(result != null) {
			resultCount = result.size();
		}
	}

	public int getResultCount() {
		return resultCount;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
	}

	public static DataResult fetchDataResult(String language, Session session, String queryString) {
		if("sql".equalsIgnoreCase(language)) {
			return new SqlExecutor().execute(session, queryString);
		} else if("hql".equalsIgnoreCase(language)) {
			return new HqlExecutor().execute(session, queryString);
		} else {
			throw new UnsupportedOperationException("Fetch type '" + language + "' is unsupported!");
		}
	}
}
