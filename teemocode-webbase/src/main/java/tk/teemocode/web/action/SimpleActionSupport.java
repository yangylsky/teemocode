package tk.teemocode.web.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

import tk.teemocode.commons.component.json.JSONHiEncoder;
import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.exception.ProjectException;
import tk.teemocode.commons.util.lang.StringUtil;
import tk.teemocode.commons.util.reflect.PojoUtil;
import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.module.util.SystemEnv;
import tk.teemocode.session.UserSession;
import tk.teemocode.web.WebConstants;

public abstract class SimpleActionSupport {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemEnv.getInstance().getDateFormat());
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
	}

	/**
	 * <ul>toJson是否以datetime格式返回，默认为false
	 * <li>true - 'yyyy-MM-dd hh:mm:ss'
	 * <li>false - 'yyyy-MM-dd'
	 * @return
	 */
	protected boolean isListDatetime() {
		return false;
	}

	/**
	 * 绕过Template,直接输出内容的简便函数.
	 */
	protected void render(HttpServletResponse response, String text, String contentType) {
		try {
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch(IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出字符串.
	 */
	protected void renderText(HttpServletResponse response, String text) {
		render(response, text, "text/plain;charset=UTF-8");
	}

	protected String renderText(String text, ModelMap map) {
		map.put("text", text);
		return "text";
	}

	/**
	 * 直接输出HTML.
	 */
	protected void renderHtml(HttpServletResponse response, String html) {
		render(response, html, "text/html;charset=UTF-8");
	}

	/**
	 * 直接输出HTML.
	 */
	protected String renderFCKeditorHtml(String title, String text, ModelMap map) {
		map.put("title", title);
		map.put("text", text);
		return "fckeditor";
	}

	/**
	 * 直接输出XML.
	 */
	protected void renderXML(HttpServletResponse response, String xml) {
		render(response, xml, "text/xml;charset=UTF-8");
	}

	/**
	 * 直接输出json.
	 */
	protected void renderJson(HttpServletResponse response, String json) {
		render(response, json, "text/javascript;charset=UTF-8");
	}

	/**
	 * 直接输出byte file.
	 */
	protected void renderFileByte(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String fileName) {
		try {
			String mime = request.getSession(true).getServletContext().getMimeType(fileName);
			if(mime == null) {
				mime = "application/octet-stream";
			}
			mime += ";charset=utf-8";
			response.setContentType(mime);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
			if(bytes.length > 0) {
				response.getOutputStream().write(bytes, 0, bytes.length);
				response.flushBuffer();
			}
		} catch(IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected void renderFileByte(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String fileName,
			String mime) {
		try {
			if(StringUtils.isBlank(mime)) {
				mime = "application/octet-stream";
			}
			mime += ";charset=utf-8";
			response.setContentType(mime);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
			if(bytes.length > 0) {
				response.getOutputStream().write(bytes, 0, bytes.length);
				response.flushBuffer();
			}
		} catch(IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	protected String renderJsonMsg(String msg, ModelMap map) {
		map.put("msg", StringUtil.toHtmlString(msg, false));
		return "jsonmsg";
	}

	protected String renderJsonMsg(String msg, Map<?, ?> props, ModelMap map) {
		map.put("msg", StringUtil.toHtmlString(msg, false));
		map.put("props", JSONHiEncoder.encode(props));
		return "jsonmsg";
	}

	protected String renderJsonError(String msg, ModelMap map) {
		msg = StringUtil.toHtmlString(msg, false);
		msg = msg.replaceAll("\n", "<br/>");
		msg = msg.replaceAll("\r", "");
		map.put("msg", msg);
		map.put("props", "{}");
		return "jsonerror";
	}

	protected String renderJsonPage(Page<?> page, ModelMap map) throws JSONException {
		return renderJsonPage(page, map, null);
	}

	protected String renderJsonPage(Page<?> page, ModelMap map, Map<String, Object> encodeParams)
			throws JSONException {
		String json = JSONHiEncoder.encodeByParams(page, encodeParams);
		map.put("json", json);
		return "jsonlist";
	}

	protected String renderJsonList(List<?> list, ModelMap map) throws JSONException {
		return renderJsonList(list, map, null);
	}

	protected String renderJsonList(List<?> list, ModelMap map, Map<String, Object> encodeParams)
			throws JSONException {
		String json = JSONHiEncoder.encodeByParams(list, encodeParams);
		map.put("json", json);
		return "jsonlist";
	}

	protected String renderJsonObj(Object obj, ModelMap map) {
		return renderJsonObj(obj, map, null);
	}

	protected String renderJsonObj(Object obj, ModelMap map, Map<String, Object> encodeParams) {
		String json = JSONHiEncoder.encodeByParams(obj, encodeParams);
		map.put("json", json);
		return "jsonobj";
	}

	protected String renderSimpleJsonObj(Object obj, ModelMap map) {
		return renderJsonObj(obj, map, null);
	}

	protected String renderSimpleJsonObj(Object obj, ModelMap map, Map<String, Object> encodeParams) {
		Object model = null;
		if(obj instanceof Map) {
			model = obj;
		} else {
			try {
				model = obj.getClass().newInstance();
			} catch(InstantiationException e) {
				throw new ProjectException(e);
			} catch(IllegalAccessException e) {
				throw new ProjectException(e);
			}
		}

		PojoUtil.simpleObjToObj(model, obj);
		String json = JSONHiEncoder.encodeByParams(model, encodeParams);
		map.put("json", json);
		return "jsonobj";
	}

	protected UserInfo getUser(HttpServletRequest request) {
		UserSession userSession = (UserSession) request.getSession().getAttribute(WebConstants.USERSESSION);
		return userSession == null ? null : userSession.getUser();
	}
}
