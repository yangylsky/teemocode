package tk.teemocode.module.base.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IJdbcDao {
	public JdbcTemplate getJdbcTemplate();

	public void execute(String sql);

	public int update(String sql);

	public <T> T queryForObject(String sql, Class<T> elementType);

	public <T> T queryForObject(String sql, Object[] args, Class<T> elementType);
	
	public int queryForInt(String sql);
	
	public int queryForInt(String sql, Object[] args);
	
	public long queryForLong(String sql);
	
	public double queryForDouble(String sql);

	public List<Map<String, Object>> queryForList(String sql);
	
	public <T> List<T> queryForList(String sql, Class<T> elementType);
}
