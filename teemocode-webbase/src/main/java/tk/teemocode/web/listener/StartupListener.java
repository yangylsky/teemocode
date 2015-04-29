package tk.teemocode.web.listener;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tk.teemocode.module.index.convertor.ConvertorCache;
import tk.teemocode.module.util.CommonUtil;
import tk.teemocode.module.util.SystemEnv;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 *
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the
 * Dao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 */
public class StartupListener extends ContextLoaderListener {
	private SystemEnv env;

	private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);

    @Override
	public void contextInitialized(ServletContextEvent event) {
    	ServletContext context = event.getServletContext();
    	env = SystemEnv.getInstance();
    	logger.info("初始化应用程序......");
        try {
        	// call Spring's context ContextLoaderListener to initialize all the context files specified in web.xml
            super.contextInitialized(event);

            // Orion starts Servlets before Listeners, so check if the config object already exists
			ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

			env.setSpringCtx(ctx);
			CommonUtil.init(ctx);

			//需要在此处激活systemEnv bean，这个问题比较奇怪
			SystemEnv.getBean("systemEnv");

			if(env.isInitConvertorCache()) {
				ConvertorCache.init();
			}

			String language = context.getInitParameter("language");
			String country = context.getInitParameter("country");
			Locale locale = new Locale(language, country);
			env.setLocale(locale);

			initSystemEnv(context);

	        logger.info("初始化应用程序成功");
		} catch(Exception e) {
			logger.error("初始化应用程序失败", e);
		}
    }

	public void initSystemEnv(ServletContext context) {
    	env.setHomePath(context.getRealPath("/"));
    	env.setTempPath(System.getProperty("java.io.tmpdir"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if(env != null) {
			env.destroyed();
		}
		super.contextDestroyed(event);
	}
}
