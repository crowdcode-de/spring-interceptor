package de.crowdcode.springcdi.interceptor.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.crowdcode.springcdi.interceptor.model.InterceptorMetaDataBean;
import de.crowdcode.springcdi.interceptor.simple.Simple_MyServiceInterface_Impl;
import de.crowdcode.springcdi.interceptor.simple_methodlevel.Simple_MyServiceInterface_Impl2;
import de.crowdcode.springcdi.interceptor.strategies.impl.SimpleInterceptorResolutionStrategy;


@ContextConfiguration("/test-context-interceptor-simple_methodlevel.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class InterceptorMetaDataBeanTest {

	@Autowired
	private BeanFactory beanFactory;
	
	private SimpleInterceptorResolutionStrategy interceptorResolutionStrategy;

	private InterceptorMetaDataBean metaDataBean;
	
	@Before
	public void setUp() {
		interceptorResolutionStrategy = new SimpleInterceptorResolutionStrategy();
		metaDataBean = new InterceptorMetaDataBean(interceptorResolutionStrategy.resolveRegisteredInterceptors(beanFactory));
	}
	
	/**
	 * Class level interceptor found
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void testGetMatchingInterceptors_beanName() throws SecurityException, NoSuchMethodException {
		Assert.assertTrue(metaDataBean.getMatchingInterceptors("simple_MyServiceInterface_Impl", this.getClass().getDeclaredMethods()[0]).size()==1);
	}
	
	/**
	 * No class level interceptor, but a method level declaration
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void testGetMatchingInterceptors_methodName() throws SecurityException, NoSuchMethodException {
		Assert.assertTrue(metaDataBean.getMatchingInterceptors("", Simple_MyServiceInterface_Impl2.class.getDeclaredMethod("sayHello", new Class[]{})).size()==1);
	}
	
	/**
	 * There is no class level interceptor for the bean impl2
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void testGetMatchingInterceptors_beanName_negative() throws SecurityException, NoSuchMethodException {
		Assert.assertTrue(metaDataBean.getMatchingInterceptors("simple_MyServiceInterface_Impl2", this.getClass().getDeclaredMethods()[0]).size()==0);
	}
	
	/**
	 * Class level interceptor bindings apply to all methods
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void testGetMatchingInterceptors_methodName_negative() throws SecurityException, NoSuchMethodException {
		Assert.assertTrue(metaDataBean.getMatchingInterceptors("", Simple_MyServiceInterface_Impl.class.getDeclaredMethod("sayHello", new Class[]{})).size()==1);
	}
	
}
