package tk.teemocode.module.identity.localservice;

import tk.teemocode.module.base.service.LocalService;
import tk.teemocode.module.identity.bo.User;

public interface UserLocalService extends LocalService<User> {
	public User getByEmail(String email);

	public User getByMobile(String mobile);

	public User getByWxOpenId(String wxOpenId);

	/**
	 * 根据部门名称获取机构管理员
	 * @param groupUuid
	 * @return
	 */
	public User getGroupSysUser(String groupUuid);
}
