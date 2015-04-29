package tk.teemocode.module.test;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import tk.teemocode.module.base.service.BaseService;
import tk.teemocode.module.index.convertor.ConvertorCache;
import tk.teemocode.module.util.CommonUtil;
import tk.teemocode.module.util.SystemEnv;

@TransactionConfiguration(transactionManager="txManager", defaultRollback = false)
@ContextConfiguration(locations={"classpath*:/applicationContext.xml"})
public abstract class SpringTestNGBase extends AbstractTransactionalTestNGSpringContextTests {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	protected SessionFactory sessionFactory;

	protected Session session;

	@Resource
	protected ApplicationContext ctx;

	@Resource(name = "businessServiceImpl")
	protected BaseService bs;

	@BeforeClass
	protected void init() {
		Assert.assertNotNull(ctx);
		Assert.assertNotNull(sessionFactory);

		SystemEnv env = SystemEnv.getInstance();
		env.setSpringCtx(ctx);

		CommonUtil.init(ctx);

		//需要在此处激活systemEnv bean，这个问题比较奇怪
		SystemEnv.getBean("systemEnv");

		if(env.isInitConvertorCache()) {
			ConvertorCache.init();
		}
	}

	@BeforeMethod(alwaysRun=true)
	protected void setUp() throws Exception {
		logger.debug("SpringTestNGBase set up ...");
		try {
			//			session = HibernateUnitils.getSession();
			//			if(session != null) {
			//				session.setFlushMode(FlushMode.AUTO);
			//			}
			prepareTestInstance();
			onSetUp();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@AfterTest(alwaysRun=true)
	protected void afterTestTearDown() {
		try {
			onTearDown();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		//		Assert.assertNotNull(TransactionSynchronizationManager.getResource(sessionFactory));
		//		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
		logger.debug("SpringTestNGBase tear down ...");
	}

	protected void prepareTestInstance() throws Exception {
	}

	protected void onTearDown() throws Exception {
	}

	protected void onSetUp() throws Exception {
	}

	protected void login(String name) {
	}
}
