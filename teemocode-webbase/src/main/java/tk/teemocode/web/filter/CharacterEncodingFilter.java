package tk.teemocode.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import tk.teemocode.web.util.RequestUtil;

public class CharacterEncodingFilter implements Filter {
	
	private static final String ENCODING = "encoding";
	
	private static final String FORCE_ENCODING = "forceEncoding";
	
	private String encoding;
	
	private boolean forceEncoding = false;
	
	@Override
	public void destroy() {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if(encoding != null && (forceEncoding || request.getCharacterEncoding() == null)) {
			String requestCharacterEncoding = RequestUtil.getParameter(request, "requestCharacterEncoding");
			if(StringUtils.isBlank(requestCharacterEncoding)) {
				request.setCharacterEncoding(encoding);
			} else {
				request.setCharacterEncoding(requestCharacterEncoding);
			}
			String responseCharacterEncoding = RequestUtil.getParameter(request, "responseCharacterEncoding");
			if(StringUtils.isBlank(responseCharacterEncoding)) {
				if(forceEncoding) {
					response.setCharacterEncoding(encoding);
					response.setContentType("text/html;charset=" + encoding);
				}
			} else {
				response.setCharacterEncoding(responseCharacterEncoding);
				response.setContentType("text/html;charset=" + responseCharacterEncoding);
			}
		}
		chain.doFilter(request, response);
		
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter(CharacterEncodingFilter.ENCODING);
		forceEncoding = config.getInitParameter(CharacterEncodingFilter.FORCE_ENCODING).equalsIgnoreCase("true");
	}
}
