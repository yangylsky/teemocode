package tk.teemocode.module.base.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import tk.teemocode.module.base.dao.IJdbcDao;

@Repository
public class JdbcDao extends JdbcDaoSupport implements IJdbcDao {
	private static final Log log = LogFactory.getLog(Dao.class);

	public JdbcDao() {
	}

	public JdbcDao(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Resource(name = "dataSource")
	public void setDataSourceOverride(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	public void execute(String sql) {
		if(log.isDebugEnabled()) {
			log.debug("executeSQLExecute:" + sql);
		}
		getJdbcTemplate().execute(sql);
	}

	@Override
	public int update(String sql){
		if(log.isDebugEnabled()) {
			log.debug("executeSQLUpdate:" + sql);
		}
		return getJdbcTemplate().update(sql);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> elementType) {
		try {
			return getJdbcTemplate().queryForObject(sql, elementType);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, Class<T> elementType) {
		try {
			return getJdbcTemplate().queryForObject(sql, args, elementType);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public int queryForInt(String sql) {
		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	@Override
	public int queryForInt(String sql, Object[] args) {
		return getJdbcTemplate().queryForObject(sql, args, Integer.class);
	}

	@Override
	public long queryForLong(String sql) {
		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	@Override
	public double queryForDouble(String sql) {
		Double v = queryForObject(sql, Double.class);
		return v == null ? 0 : v;
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		if(log.isDebugEnabled()) {
			log.debug("queryForList:" + sql);
		}
		return getJdbcTemplate().queryForList(sql);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) {
		return getJdbcTemplate().queryForList(sql, elementType);
	}
}
