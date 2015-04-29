package tk.teemocode.session.service;

import java.io.IOException;

import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.session.UserSession;

public interface UserSessionService {
	public String getUserTempPath(String sessionId) throws IOException;

	public UserInfo getUser(String sessionId);

	public UserSession getSession(String sessionId);

	public boolean hasSession(String sessionId);

	public UserSession createSession(String sessionId, UserInfo userInfo);

	public boolean deleteSession(String sessionId);
}
