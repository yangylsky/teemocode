package com.teemocode.commons.util.data;

import org.hibernate.Session;

public interface DataExecutor {
	public DataResult execute(Session session, String queryString);
}
