package tk.teemocode.web.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import tk.teemocode.commons.exception.AccessDenyException;
import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.module.identity.service.UserService;
import tk.teemocode.session.UserSession;
import tk.teemocode.web.WebConstants;
import tk.teemocode.web.util.LoginVerification;
import tk.teemocode.web.util.RequestUtil;
import tk.teemocode.web.util.WebBaseUtil;
import tk.teemocode.web.util.WebUtil;

public abstract class SiteBaseAction extends SimpleActionSupport {
	@Resource
	protected UserService userService;

	@Override
	protected UserInfo getUser(HttpServletRequest request) {
		UserInfo userInfo = super.getUser(request);
		if(userInfo == null) {
			userInfo = getOrCreateUserSession(request).getUser();
		}
		return userInfo;
	}

	protected UserInfo setDefaultToMap(HttpServletRequest request, ModelMap map, Map<String, Object> props) {
		UserSession userSession = getOrCreateUserSession(request);
		props.put(WebBaseUtil.Key_User, userSession.getUser());
		return userSession.getUser();
	}

	protected UserSession getOrCreateUserSession(HttpServletRequest request) {
		UserSession userSession = getUserSession(request);
		if(userSession == null) {
			userSession = createUserSessionFromLoginVerification(request);
		}
		if(userSession == null) {
			throw new AccessDenyException("会话超时，请重新登录");
		}
		return userSession;
	}

	/**
	 * 如果客户端设置了自动登录，尝试从保存的登录密文里重建UserSession
	 * @param request
	 * @return
	 */
	private UserSession createUserSessionFromLoginVerification(HttpServletRequest request) {
		LoginVerification loginVerification = getLoginVerification(request);
		String encryptData = loginVerification.getVerifiedData();
		if(StringUtils.isNotBlank(encryptData)) {
			Object lock = WebBaseUtil.getLock(encryptData);
			synchronized(lock) {
				/**
				 * 再次检测UserSession是否已被其他线程创建，如果已存在，则直接返回
				 */
				UserSession userSession = getUserSession(request);
				if(userSession == null) {
					UserInfo userInfo = userService.loginFromVerifiedData(encryptData);
					userSession = initUserSession(request, userInfo);
				}
				return userSession;
			}
		}
		return null;
	}

	protected UserSession getUserSession(HttpServletRequest request) {
		return getUserSession(request.getSession());
	}

	protected UserSession getUserSession(HttpSession session) {
		return (UserSession) session.getAttribute(WebConstants.USERSESSION);
	}

	protected LoginVerification getLoginVerification(HttpServletRequest request) {
		Cookie verificationCookie = RequestUtil.getCookie(request, WebBaseUtil.Remember_Login_Cookie_Name);
		LoginVerification loginVerification = new LoginVerification();
		loginVerification.setVerificationCookie(verificationCookie);
		if(verificationCookie != null) {
			loginVerification.setVerifiedData(verificationCookie.getValue());
		}
		return loginVerification;
	}

	protected UserSession initUserSession(HttpServletRequest request, UserInfo userInfo) {
		String sessionId = RequestUtil.getRequestedSessionId(request);
		UserSession userSession = WebUtil.initUserSession(sessionId, userInfo);
		request.getSession().setAttribute(WebConstants.USERSESSION, userSession);
		return userSession;
	}

	/**
	 * 根据处理结果返回对应的提示信息
	 * @param result
	 * @return
	 */
	public String getResultMsg(boolean result) {
		return getResultMsg(result, null);
	}

	/**
	 * 根据处理结果返回对应的提示信息
	 * @param result
	 * @param actionType
	 * @return
	 */
	public String getResultMsg(boolean result, String actionType) {
		return result ? actionType + "成功" :
			actionType + "失败!原因可能是：1)无效或重复" + actionType + "；2)" + actionType + "失败";
	}

	/**
	 * 根据批量处理结果(操作成功的uuids)返回对应的提示信息
	 *
	 * @param processNum
	 *            - 操作的数量
	 * @param successNum
	 *            - 操作成功的数量
	 * @return msg
	 */
	protected String getResultMsg(int processNum, int successNum) {
		return getResultMsg(processNum, successNum, "操作");
	}

	/**
	 * 根据批量处理结果(操作成功的uuids)返回对应的提示信息
	 *
	 * @param processNum
	 *            - 操作的数量
	 * @param successNum
	 *            - 操作成功的数量
	 * @param actionType
	 *            - 操作类型描述
	 * @return msg
	 */
	protected String getResultMsg(int processNum, int successNum, String actionType) {
		String resultDesc = "无效的" + actionType + "信息";
		if(processNum > 0 && successNum >= 0 && processNum >= successNum) {
			if(successNum == 0) {
				resultDesc = "失败";
			} else if(successNum == processNum) {
				resultDesc = "成功";
			} else if(0 < successNum && successNum < processNum) {
				resultDesc = "部分成功。未" + actionType + "成功的原因可能是：1)无效或重复" + actionType + "；2)" + actionType + "失败";
			}
		}
		return actionType + resultDesc;
	}
}
