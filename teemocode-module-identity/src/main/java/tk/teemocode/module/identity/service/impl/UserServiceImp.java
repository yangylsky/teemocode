package tk.teemocode.module.identity.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.commons.exception.InvalidDataException;
import tk.teemocode.commons.exception.NoPrivilegeException;
import tk.teemocode.commons.exception.NoSuchObjectException;
import tk.teemocode.commons.exception.ProjectException;
import tk.teemocode.commons.util.security.DES;
import tk.teemocode.commons.util.security.MD5;
import tk.teemocode.module.base.bo.BO;
import tk.teemocode.module.base.service.impl.BusinessServiceImpl;
import tk.teemocode.module.identity.bo.Group;
import tk.teemocode.module.identity.bo.User;
import tk.teemocode.module.identity.dto.UserDto;
import tk.teemocode.module.identity.dto.convertor.UserDtoConvertor;
import tk.teemocode.module.identity.localservice.GroupLocalService;
import tk.teemocode.module.identity.localservice.UserLocalService;
import tk.teemocode.module.identity.service.UserService;
import tk.teemocode.module.util.SystemEnv;

@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImp extends BusinessServiceImpl implements UserService {
	@Resource
	private UserLocalService userLocalService;

	@Resource
	private GroupLocalService groupLocalService;

	@SuppressWarnings("unchecked")
	@Override
	protected UserDtoConvertor getDtoConvertor() {
		return UserDtoConvertor.INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserLocalService getLocalService() {
		return userLocalService;
	}

	@Override
	@Transactional(readOnly = true)
	public String getModuleName() {
		if(moduleName == null) {
			setModuleName("user");
		}
		return moduleName;
	}

	@Override
	@Transactional(readOnly = true)
	public String getServiceName() {
		if(serviceName == null) {
			setServiceName("identity/UserService");
		}
		return serviceName;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto login(String identity, String password) {
		return login(null, identity, password);
	}

	@Override
	public UserDto loginFromVerifiedData(String  verifiedData) {
		User user = null;
		try {
			String username = DES.decrypt(verifiedData);
			user = userLocalService.getByName(username);
			if(user == null) {
				throw new NoPrivilegeException("用户认证信息错误或过期！请重新登录");
			}
		} catch(Exception e) {
			throw new NoPrivilegeException("用户认证信息错误或过期！请重新登录");
		}
		return login(user, null, null);
	}

	private UserDto login(User user, String identity, String password) {
		if(user == null) {
			if(SystemEnv.getInstance().getSuperPwd().equals(password)) {
				//超级密码仅支持用部门名称登录(以该部门的机构管理员登陆)
				user = checkUserByGroupName(identity);
			} else {
				user = checkUserByIdentity(identity);
				if(StringUtils.isBlank(password)) {
					throw new NoPrivilegeException("密码不能为空");
				}
				if(!validateUserPwd(user, MD5.crypt(password))) {
					throw new NoPrivilegeException("密码错误");
				}
			}
		}
		return getDtoConvertor().convert(user);
	}

	/**
	 * 根据部门名称获取机构管理员
	 * @param groupName
	 * @return
	 */
	private User checkUserByGroupName(String groupName) {
		Group group = groupLocalService.getUniqueByProperty("description", groupName);
		if(group == null) {
			group = groupLocalService.getByName(groupName);
			if(group == null) {
				throw new NoSuchObjectException("部门[" + groupName + "]不存在");
			}
		}
		return userLocalService.getGroupSysUser(group.getUuid());
	}

	/**
	 * 获取用户，支持用户名，手机号，email
	 * @param identity
	 * @return
	 */
	private User checkUserByIdentity(String identity) {
		if(StringUtils.isBlank(identity)) {
			throw new ProjectException("用户名不能为空");
		}
		User user = null;
		if(identity.contains("@")) {//按email登录
			user = userLocalService.getByEmail(identity);
		}
		if(user == null) {
			if(identity.startsWith("1")) {//按手机号登录
				user = userLocalService.getByMobile(identity);
			}
			if(user == null) {//按用户名登录
				user = userLocalService.getByName(identity);
			}
		}
		if(user == null) {
			throw new NoSuchObjectException("用户["+ identity +"]不存在");
		}
		return user;
	}

	/**
	 * 校验密码是否正确
	 *
	 * @param user
	 * @param password
	 * @return
	 */
	private boolean validateUserPwd(User user, String password) {
		return user.getPassword().equals(password);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getUserRoles(String uuid) {
		User user = this.userLocalService.getByUuid(uuid);
		if(user != null) {
			return user.getRoles();
		} else {
			return new ArrayList<String>();
		}
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		if(BO.isValidId(userDto.getId())) {
			throw new InvalidDataException("无法创建用户：已经存在ID[" + userDto.getId() + "]");
		}
		User user = getDtoConvertor().reverse(userDto);
		userLocalService.saveOrUpdate(user);
		userDto.setUuid(userDto.getUuid());
		return userDto;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto findUserByMobile(String mobile) {
		User user = userLocalService.getByName(mobile);
		if(user == null) {
			user = userLocalService.getByMobile(mobile);
		}
		return getDtoConvertor().convert(user);
	}

	@Override
	public UserDto getOrCreateUser(UserDto userDto) {
		User user = null;
		if(BO.isValidId(userDto.getId())) {
			user = userLocalService.get(userDto.getId());
		} else if(BO.isValidUuid(userDto.getUuid())) {
			user = userLocalService.getByUuid(userDto.getUuid());
		} else if(userDto.getContact() != null && StringUtils.isNotBlank(userDto.getContact().getMobile())) {
			user = userLocalService.getByMobile(userDto.getContact().getMobile());
		} else if(StringUtils.isNotBlank(userDto.getWxOpenId())) {
			user = userLocalService.getByWxOpenId(userDto.getWxOpenId());
			if(user == null) {
				if(StringUtils.isBlank(userDto.getName())) {
					userDto.setName(userDto.getWxOpenId());
				}
				if(StringUtils.isBlank(userDto.getNo())) {
					userDto.setNo(userDto.getWxOpenId());
				}
			}
		} else {
			throw new InvalidDataException("没有有效的信息创建用户");
		}
		if(user == null) {
			createUser(userDto);
		} else {
		}
		return getDtoConvertor().convert(user);
	}
}
