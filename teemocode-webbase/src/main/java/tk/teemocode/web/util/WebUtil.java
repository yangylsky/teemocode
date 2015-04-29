package tk.teemocode.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.module.util.SystemEnv;
import tk.teemocode.session.UserSession;
import tk.teemocode.session.service.UserSessionCreator;
import tk.teemocode.session.service.UserSessionService;

public class WebUtil {
	private static final Log log = LogFactory.getLog(WebUtil.class);

	public static WebApplicationContext getSpringWebApplicationContext(HttpServletRequest request) {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());
		return context;
	}

	public static UserSessionService getUserSessionService() {
		return WebUtil.getBean("userSessionService");
	}

	public static UserSession initUserSession(String sessionId, UserInfo userInfo) {
		UserSessionCreator creator = WebUtil.getBean("userSessionCreator");
		UserSession userSession = null;
		if(creator != null) {
			userSession = creator.createSession(sessionId, userInfo);
		} else {
			userSession = WebUtil.getUserSessionService().createSession(sessionId, userInfo);
		}

		log.error("用户(" + userSession.getUserName() + ") Session被创建.sessionId=" + sessionId);

		return userSession;
	}

	public static void destroyedSession(HttpSession session) {
		Object obj = session.getAttribute("userSession");
		if(obj != null) {
			String sessionId = ((UserSession) obj).getSessionId();
			WebUtil.getUserSessionService().deleteSession(sessionId);
			session.removeAttribute("userSession");
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SystemEnv.getInstance().setSpringCtx(applicationContext);
	}

	public static ApplicationContext getApplicationContext() {
		return SystemEnv.getInstance().getSpringCtx();
	}

	public static <T> T getBean(String name) {
		return SystemEnv.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return SystemEnv.getBeansOfType(clazz);
	}

	public static boolean checkCurUser(String sessionId) {
		return WebUtil.getUserSessionService().hasSession(sessionId);

	}

	public static UserInfo getCurUser(String sessionId) {
		UserSession session = WebUtil.getUserSessionService().getSession(sessionId);
		if(session != null) {
			return session.getUser();
		}
		return null;
	}

	public static UserSession getCurUserSession(String sessionId) {
		UserSession session = WebUtil.getUserSessionService().getSession(sessionId);
		return session;
	}
}
