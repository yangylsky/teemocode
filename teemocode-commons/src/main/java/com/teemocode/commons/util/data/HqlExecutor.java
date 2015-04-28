package com.teemocode.commons.util.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class HqlExecutor implements DataExecutor {
	private static final Log log = LogFactory.getLog(HqlExecutor.class);

	@SuppressWarnings("unchecked")
	@Override
	public DataResult execute(Session session, String queryString) {
		try {
			DataResult dataResult = new DataResult();
			Query query = session.createQuery(queryString);
			List<Object[]> result = query.list();
			String[] columnNames = query.getReturnAliases();
			String[] columnLabels = columnNames;

			dataResult.setColumnCount(columnNames.length);
			dataResult.setColumnLabels(columnLabels);
			dataResult.setColumnNames(columnNames);
			dataResult.setResult(result);
			session.close();
			return dataResult;
		} catch(HibernateException e) {
			String msg = "执行HQL语句时产生数据库异常:\n" + queryString + "\n";
			log.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

}
