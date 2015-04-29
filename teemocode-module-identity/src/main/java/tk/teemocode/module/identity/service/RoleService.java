package tk.teemocode.module.identity.service;

import java.util.List;

public interface RoleService {
	/**
	 * 获取角色权限uuid列表
	 *
	 * @param uuid
	 *            角色uuid
	 * @return
	 */
	public List<String> getRoleAuths(String uuid);
}
