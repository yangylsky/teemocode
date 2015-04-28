package com.teemocode.commons.util.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

public class SqlExecutor implements DataExecutor {
	private static final Log log = LogFactory.getLog(SqlExecutor.class);

	@Override
	public DataResult execute(Session session, String queryString) {
		try {
			DataResult dataResult = new DataResult();
			Connection conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
			Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(queryString);
	        //取得数据表中的字段数目，类型等返回结果
	        ResultSetMetaData rsmd=rs.getMetaData();
			int columnCount = rsmd.getColumnCount(); //列的总数

			String[] columnNames = new String[columnCount];
			String[] columnLabels = new String[columnCount];
			for(int i = 1; i <= columnCount; i++) {
				columnNames[i - 1] = rsmd.getColumnName(i);
				columnLabels[i - 1] = rsmd.getColumnLabel(i);
			}

			List<Object[]> result = new ArrayList<Object[]>();
			while(rs.next()) {
				Object[] row = new Object[columnCount];
				for(int i = 1; i <= columnCount; i++) {
					row[i - 1] = rs.getObject(i);
				}
				result.add(row);
			}
			dataResult.setColumnCount(columnCount);
			dataResult.setColumnLabels(columnLabels);
			dataResult.setColumnNames(columnNames);
			dataResult.setResult(result);
			rs.close();
			stmt.close();
			session.close();
			return dataResult;
		} catch(SQLException e) {
			String msg = "执行SQL语句时产生数据库异常:\n" + queryString + "\n";
			log.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
}
