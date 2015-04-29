package tk.teemocode.session.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;

import tk.teemocode.commons.exception.NoPrivilegeException;
import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.module.identity.service.RoleService;
import tk.teemocode.module.identity.service.UserService;
import tk.teemocode.module.util.SystemEnv;
import tk.teemocode.session.UserSession;
import tk.teemocode.session.service.UserSessionCreator;
import tk.teemocode.session.service.UserSessionService;

public class UserSessionCreatorImpl implements UserSessionCreator {
	@Resource
	private UserService userService;

	@Resource
	private RoleService roleService;

	@SuppressWarnings("unchecked")
	@Override
	public UserSession createSession(String sessionId, UserInfo userInfo) {
		if(sessionId == null) {
			throw new NoPrivilegeException("sessionId cannot be a null");
		}
		if(userInfo == null) {
			throw new NoPrivilegeException("userInfo cannot be a null");
		}
		UserSessionService uss = SystemEnv.getBean("userSessionService");
		UserSession session = uss.createSession(sessionId, userInfo);
		String uid = userInfo.getUuid();
		List<String> auths = new ArrayList<String>();
		// init roles
		List<String> roles = userService.getUserRoles(uid);
		session.setRoles(roles);
		// init roles auths
		for(String rid : roles) {
			List<String> rAuths = roleService.getRoleAuths(rid);
			auths = ListUtils.sum(auths, rAuths);
		}

		session.setAuths(auths);

		List<String> urls = new ArrayList<String>();
		List<String> modules = new ArrayList<String>();

		session.setModules(modules);
		session.setUrls(urls);
		return session;
	}
}
