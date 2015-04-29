package tk.teemocode.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.teemocode.session.UserSession;
import tk.teemocode.web.WebConstants;
import tk.teemocode.web.util.WebUtil;

public class UserSessionListener implements HttpSessionListener {
	private static final Log log = LogFactory.getLog(UserSessionListener.class);

//	 public void sessionCreated(HttpSessionEvent event) {
//	 super.sessionCreated(event);
//	 // SpringWebUtil.initSessionByPrincipal(event.getSession());
//	 // log.debug("create session,sessionid:" + event.getSession().getId() + ",userName=" + SpringWebUtil.getCurUserName());
//	 }
//
//	 public void sessionDestroyed(HttpSessionEvent event) {
//	 super.sessionDestroyed(event);
//	 // log.debug("destroyed session,sessionid:" + event.getSession().getId() + ",userName=" +
//	 SpringWebUtil.getCurUserName());
//	 // SpringWebUtil.destroyedSessionByPrincipal(event.getSession());
//	 }

	/* Session创建事件 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	/* Session失效事件 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession httpSession = se.getSession();
		UserSession userSession = (UserSession) httpSession.getAttribute(WebConstants.USERSESSION);
		if(userSession != null) {
			String sessionId = userSession.getSessionId();
			WebUtil.destroyedSession(httpSession);
			log.debug("session destoroyed,sessionid:" + sessionId + ",userName=" + userSession.getUserName());
		}
	}
}
