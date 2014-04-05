package de.crowdcode.springcdi.interceptor.strategies.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.crowdcode.springcdi.interceptor.InterceptorModuleUtils;
import de.crowdcode.springcdi.interceptor.ct._92_C1.interceptors.SecurityInterceptor;
import de.crowdcode.springcdi.interceptor.model.InterceptorInfo;
import de.crowdcode.springcdi.interceptor.model.MethodInterceptorInfo;
import de.crowdcode.springcdi.interceptor.strategies.impl.InterceptorInfoVisitor;
import de.crowdcode.springcdi.interceptor.strategies.impl.MethodLevelBindingsVisitor;


@ContextConfiguration("/test-context-interceptor-ct-92-C1.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class MethodLevelBindingsVisitorTest_CT_92 {

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	private InterceptorInfo transactionInterceptor;

	private InterceptorInfo securityInterceptor;
	
	private InterceptorInfo oldSecurityInterceptor;
	
	private InterceptorInfo vipInterceptor;
	
	private InterceptorInfoVisitor visitor = new MethodLevelBindingsVisitor();

	private BeanDefinition impl3Definition;
	
	private BeanDefinition impl4Definition;
	
	@Before
	public void setUp() {
		transactionInterceptor = new MethodInterceptorInfo(new BeanDefinitionHolder(((DefaultListableBeanFactory) beanFactory).getBeanDefinition("transactionInterceptor"), "transactionInterceptor"));
		securityInterceptor = new MethodInterceptorInfo(new BeanDefinitionHolder(((DefaultListableBeanFactory) beanFactory).getBeanDefinition("securityInterceptor"), "securityInterceptor"));
		oldSecurityInterceptor = new MethodInterceptorInfo(new BeanDefinitionHolder(((DefaultListableBeanFactory) beanFactory).getBeanDefinition("oldSecurityInterceptor"), "oldSecurityInterceptor"));
		vipInterceptor = new MethodInterceptorInfo(new BeanDefinitionHolder(((DefaultListableBeanFactory) beanFactory).getBeanDefinition("VIPSecurityInterceptor"), "VIPSecurityInterceptor"));
		impl3Definition = beanFactory.getBeanDefinition("CT92_TrialService_Impl3");
		impl4Definition = beanFactory.getBeanDefinition("CT92_TrialService_Impl4");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testVisit_Impl3_sayHello_ExpectSecurtityInterceptor() throws Exception {
		visitor.visit(securityInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		Assert.assertTrue(securityInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{})));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testVisit_Impl3_sayHelloString_DontExpectSecurtityInterceptor() throws Exception {
		visitor.visit(securityInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		Assert.assertTrue(!securityInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{String.class})));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testVisit_Impl3_sayHelloString_ExpectVIPAndOldSecuirityInterceptor() throws Exception {
		visitor.visit(vipInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		visitor.visit(oldSecurityInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		Assert.assertTrue(vipInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{String.class})));
		Assert.assertTrue(oldSecurityInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{String.class})));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testVisit_Impl3_sayHello_ExpectVIPAndOldSecuirityAndTransactionalInterceptor() throws Exception {
		visitor.visit(vipInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		visitor.visit(oldSecurityInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		visitor.visit(transactionInterceptor, new BeanDefinitionHolder(impl3Definition, "CT92_TrialService_Impl3"));
		Assert.assertTrue(vipInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{String.class})));
		Assert.assertTrue(oldSecurityInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{})));
		Assert.assertTrue(transactionInterceptor.matches("CT92_TrialService_Impl3", InterceptorModuleUtils.getClass_forName(impl3Definition.getBeanClassName()).getMethod("sayHello", new Class[]{})));
	}

	/**
	 * Joint class level @Transactional and method level @RuleSecured binding results in {@link SecurityInterceptor} application
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testVisit_Impl4_sayHello_ExpectSecurtityInterceptors() throws Exception {
		visitor.visit(securityInterceptor, new BeanDefinitionHolder(impl4Definition, "CT92_TrialService_Impl4"));
		Assert.assertTrue(securityInterceptor.matches("CT92_TrialService_Impl4", InterceptorModuleUtils.getClass_forName(impl4Definition.getBeanClassName()).getMethod("sayHello", new Class[]{})));
	}
	
}
