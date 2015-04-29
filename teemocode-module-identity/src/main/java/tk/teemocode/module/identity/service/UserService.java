package tk.teemocode.module.identity.service;

import java.util.List;

import tk.teemocode.module.base.service.BaseService;
import tk.teemocode.module.identity.dto.UserDto;

public interface UserService extends BaseService {
	/**
	 * 用户登录
	 * @param identity
	 * @param password
	 * @return
	 */
	public UserDto login(String identity, String password);

	/**
	 * 仅用于从认证过的用户加密信息登录，无需校验用户名和密码
	 * @param verifiedData
	 * @return
	 */
	public UserDto loginFromVerifiedData(String  verifiedData);

	/**
	 * 获取用户角色uuid列表
	 *
	 * @param uuid
	 *            用户uuid
	 * @return
	 */
	public List<String> getUserRoles(String uuid);

	/**
	 * 根据手机号查找唯一的用户(匹配手机号和用户名字段)
	 * @param mobile
	 * @return
	 */
	public UserDto findUserByMobile(String mobile);

	/**
	 * 根据基本信息字段创建用户
	 * @param userDto
	 * @return
	 */
	public UserDto createUser(UserDto userDto);

	/**
	 * 根据用户手机号或微信wxOpenId等信息创建或获取用户信息
	 * @param userDto
	 * @return
	 */
	public UserDto getOrCreateUser(UserDto userDto);
}
