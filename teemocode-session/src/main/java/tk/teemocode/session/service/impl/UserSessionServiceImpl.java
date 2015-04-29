package tk.teemocode.session.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.session.UserSession;
import tk.teemocode.session.service.UserSessionService;

public class UserSessionServiceImpl implements UserSessionService {
	private static final Log log = LogFactory.getLog(UserSessionService.class);

	private Map<String, UserSession> userSessions = new LinkedHashMap<String, UserSession>();

	private UserSessionServiceImpl() {
	}

	@Override
	public boolean deleteSession(String sessionId) {
		UserSession session = getSession(sessionId);
		if(session != null) {
			session.destroyed();
			userSessions.remove(sessionId);
			log.debug("Session(" + sessionId + ") 被删除.");
			return true;
		}
		return false;
	}

	@Override
	public UserSession createSession(String sessionId, UserInfo userInfo) {
		if(!userSessions.containsKey(sessionId)) {
			UserSession session = new UserSession(sessionId, userInfo);
			userSessions.put(sessionId, session);
			return session;
		} else {
			return getSession(sessionId);
		}
	}

	@Override
	public boolean hasSession(String sessionId) {
		return userSessions.containsKey(sessionId);
	}

	@Override
	public UserSession getSession(String sessionId) {
		UserSession us = userSessions.containsKey(sessionId) ? userSessions.get(sessionId) : null;
		if(us == null) {
			log.debug("用户(" + sessionId + ") Session不存在.");
			// throw new ProjectException("session不存在.请重新登录");
		}
		return us;
	}

	@Override
	public UserInfo getUser(String sessionId) {
		if(StringUtils.isBlank(sessionId)) {
			return null;
		}
		return userSessions.containsKey(sessionId) ? userSessions.get(sessionId).getUser() : null;
	}

	@Override
	public String getUserTempPath(String sessionId) throws IOException {
		return userSessions.containsKey(sessionId) ? userSessions.get(sessionId).getUserTempPath() : null;
	}
}
