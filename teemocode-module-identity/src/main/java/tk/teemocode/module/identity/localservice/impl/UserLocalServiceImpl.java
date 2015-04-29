package tk.teemocode.module.identity.localservice.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.teemocode.commons.util.security.MD5;
import tk.teemocode.module.base.service.impl.LocalServiceImpl;
import tk.teemocode.module.identity.bo.User;
import tk.teemocode.module.identity.localservice.UserLocalService;
import tk.teemocode.module.identity.util.IdentityConstant;

@Service
@Transactional(rollbackFor = Throwable.class)
public class UserLocalServiceImpl extends LocalServiceImpl<User> implements UserLocalService {
	@Override
	public User getByEmail(String email) {
		return getUniqueByProperty("contact.email", email);
	}

	@Override
	public User getByMobile(String mobile) {
		return getUniqueByProperty("contact.mobile", mobile);
	}

	@Override
	public User getByWxOpenId(String wxOpenId) {
		return getUniqueByProperty("wxOpenId", wxOpenId);
	}

	@Override
	public User getGroupSysUser(String groupUuid) {
		String hql = "from User where groupId=? and type=?";
		return getUniqueByHql(hql, new Object[] {groupUuid, IdentityConstant.User_Type_GroupSys});
	}

	@Override
	public User saveOrUpdate(User user) {
		if(StringUtils.isBlank(user.getPassword())) {
			user.setPassword(MD5.crypt(IdentityConstant.User_Origin_Password));
		}
		return super.saveOrUpdate(user);
	}
}
