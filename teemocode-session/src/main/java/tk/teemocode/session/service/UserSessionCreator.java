package tk.teemocode.session.service;

import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.session.UserSession;

public interface UserSessionCreator {
	public  UserSession createSession(String sessionId, UserInfo userInfo);
}
