package tk.teemocode.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import tk.teemocode.session.UserSession;
import tk.teemocode.web.WebConstants;

/**
 * spring mvc interceptor
 *
 */
public class MVCInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserSession userSession = (UserSession) request.getSession().getAttribute(WebConstants.USERSESSION);
		if(userSession == null) {
			//TODO
			boolean isNoCheckWebUrlAuth = true;
			if(isNoCheckWebUrlAuth) {
				return true;
			} else {
				request.setAttribute("errormsg", "无用户session");
				request.setAttribute("redirect", "http://localhost:8088/webresource/");// TODO:读取配置
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无用户session"); // 401
				return false;
			}
		} else {
			//TODO
			boolean hasUrlAuth = true;
			if(!hasUrlAuth) {
				String servletPath = request.getServletPath();
				String errorMsg = "当前用户禁止访问地址:[" + servletPath + "]";
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMsg);
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
