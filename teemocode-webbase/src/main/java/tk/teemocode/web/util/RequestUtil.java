package tk.teemocode.web.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.UrlPathHelper;

import tk.teemocode.web.WebConstants;

/**
 * Convenience class for setting and retrieving cookies.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RequestUtil {
	private static Log log = LogFactory.getLog(RequestUtil.class);

	/**
	 * Convenience method to set a cookie
	 *
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path) {
		if(log.isDebugEnabled()) {
			if(log.isDebugEnabled()) {
				log.debug("Setting cookie '" + name + "' on path '" + path + "'");
			}
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		cookie.setPath(path);
		cookie.setMaxAge(3600 * 24 * 30); // 30 days

		response.addCookie(cookie);
	}

	/**
	 * Convenience method to get a cookie by name
	 *
	 * @param request
	 *            the current request
	 * @param name
	 *            the name of the cookie to find
	 * @return the cookie (if found), null if not found
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		Cookie returnCookie = null;

		if(cookies == null) {
			return returnCookie;
		}

		for(Cookie cookie : cookies) {
			Cookie thisCookie = cookie;

			if(thisCookie.getName().equals(name)) {
				// cookies with no value do me no good!
				if(!thisCookie.getValue().equals("")) {
					returnCookie = thisCookie;

					break;
				}
			}
		}

		return returnCookie;
	}

	/**
	 * Convenience method for deleting a cookie by name
	 *
	 * @param response
	 *            the current web response
	 * @param cookie
	 *            the cookie to delete
	 * @param path
	 *            the path on which the cookie was set (i.e. /appfuse)
	 */
	public static void deleteCookie(HttpServletResponse response, Cookie cookie, String path) {
		if(cookie != null) {
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			cookie.setPath(path);
			response.addCookie(cookie);
		}
	}

	/**
	 * Convenience method to get the application's URL based on request variables.
	 */
	public static String getAppURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
		int port = request.getServerPort();
		if(port < 0) {
			port = 80; // Work around java.net.URL bug
		}
		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		return url.toString();
	}

	/**
     *
     */
	public static Map getParameters(HttpServletRequest request) {
		HashMap params = new HashMap();
		Enumeration rParams = request.getParameterNames();
		while(rParams.hasMoreElements()) {
			String key = (String) rParams.nextElement();
			String value = request.getParameter(key);
			params.put(key, value);
		}

		Enumeration rAttributes = request.getAttributeNames();
		while(rAttributes.hasMoreElements()) {
			String key = (String) rAttributes.nextElement();
			// if ( !params.containsKey(key) )
			// {
			String value = (String) request.getAttribute(key);
			params.put(key, value);
			// }
		}

		return params;
	}

	public static Map getAttributes(HttpServletRequest request) {
		Map atrr = new Hashtable();
		Enumeration names = request.getAttributeNames();
		if(names != null) {
			while(names.hasMoreElements()) {
				String name = (String) names.nextElement();
				atrr.put(name, request.getAttribute(name));
			}
		}
		return atrr;
	}

	public static String getParameter(HttpServletRequest request, String param) {
		return request.getAttribute(param) != null ? (String) request.getAttribute(param) : request.getParameter(param);
	}

	public static RequestDispatcher getRequestDispatcher(HttpServletRequest request, String url) {
		ServletContext context = request.getSession(true).getServletContext();
		if(context != null) {
			return context.getRequestDispatcher(url);
		} else {
			return request.getRequestDispatcher(url);
		}
	}

	public static void setRequestAttributes(HttpServletRequest request, Map map) {
		Set set = map.keySet();
		Iterator keys = set.iterator();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			Object value = map.get(key);

			if(key != null) {
				request.setAttribute(key, value);
			}
		}
	}

	/**
	 * Retrieves the current request servlet path. Deals with differences between servlet specs (2.2 vs 2.3+)
	 *
	 * @param request
	 *            the request
	 * @return the servlet path
	 */
	public static String getServletPath(HttpServletRequest request) {
		String servletPath = request.getServletPath();

		if(servletPath != null && !servletPath.equals("")) {
			return servletPath;
		}

		String requestUri = request.getRequestURI();
		int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
		int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

		if(startIndex > endIndex) { // this should not happen
			endIndex = startIndex;
		}

		return requestUri.substring(startIndex, endIndex);
	}

	public static boolean isMultiPart(HttpServletRequest request) {
		String content_type = request.getContentType();
		return content_type != null && content_type.indexOf("multipart/form-data") != -1;
	}

	/**
	 * 获取访问者IP
	 *
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 *
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)， 如果还不存在则调用Request .getRemoteAddr()。
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if(!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if(!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if(index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	/**
	 * 获得当的访问路径
	 *
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 *
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if(queryString != null) {
			buff.append("?").append(queryString);
		}
		return buff.toString();
	}

	/**
	 * 获得请求的session id，但是HttpServletRequest#getRequestedSessionId()方法有一些问题。 当存在部署路径的时候，会获取到根路径下的jsessionid。
	 *
	 * @see HttpServletRequest#getRequestedSessionId()
	 *
	 * @param request
	 * @return
	 */
	public static String getRequestedSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sid = request.getRequestedSessionId();
		if(StringUtils.isBlank(sid)) {
			sid = session.getId();
		}
		String ctx = request.getContextPath();
		// 如果session id是从url中获取，或者部署路径为空，那么是在正确的。
		if(request.isRequestedSessionIdFromURL() || StringUtils.isBlank(ctx)) {
			return sid;
		} else {
			// 手动从cookie获取
			Cookie cookie = CookieUtils.getCookie(request, WebConstants.JSESSION_COOKIE);
			if(cookie != null) {
				return cookie.getValue();
			} else {
				return sid;
			}
		}
	}
}
