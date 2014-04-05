package de.crowdcode.springcdi.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import de.crowdcode.springcdi.interceptor.model.InterceptorInfo;
import de.crowdcode.springcdi.interceptor.model.InterceptorMetaDataBean;
import de.crowdcode.springcdi.interceptor.processor.InterceptorAwareBeanPostProcessor;
import de.crowdcode.springcdi.interceptor.strategies.InterceptorOrderingStrategy;
import de.crowdcode.springcdi.interceptor.strategies.InterceptorResolutionStrategy;
import de.crowdcode.springcdi.interceptor.strategies.impl.SimpleInterceptorOrderingStrategy;
import de.crowdcode.springcdi.interceptor.strategies.impl.SimpleInterceptorResolutionStrategy;

/**
 * {@link BeanFactoryPostProcessor} that creates and registers the {@link InterceptorMetaDataBean} and the
 * {@link InterceptorAwareBeanPostProcessor}.
 * 
 * @author Niklas Schlimm
 * 
 */
@SuppressWarnings("rawtypes")
public class InterceptorAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {

	private static final String INTERCEPTOR_META_DATA_BEAN = "interceptorMetaDataBean";

	/**
	 * Strategy to resolve available interceptors
	 */
	private InterceptorResolutionStrategy interceptorResolutionStrategy;

	/**
	 * Strategy to order the available interceptors
	 */
	private InterceptorOrderingStrategy interceptorOrderingStrategy;

	private List<Class> interceptorOrder = new ArrayList<Class>();

	public InterceptorAwareBeanFactoryPostProcessor() {
		super();
	}

	public InterceptorAwareBeanFactoryPostProcessor(InterceptorResolutionStrategy interceptorResolutionStrategy) {
		super();
		this.interceptorResolutionStrategy = interceptorResolutionStrategy;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("_interceptorPostProcessor", BeanDefinitionBuilder.rootBeanDefinition(InterceptorAwareBeanPostProcessor.class)
				.getBeanDefinition());
		if (beanFactory.getBeanNamesForType(InterceptorAwareBeanPostProcessor.class) == null) {
			throw new InterceptorAwareBeanFactoryPostProcessorException("Spring-CDI interceptor module requires DecoratorAwareBeanPostProcessor registered!");
		}
		createAndRegisterMetaDataBean(beanFactory);
	}

	public InterceptorMetaDataBean createAndRegisterMetaDataBean(ConfigurableListableBeanFactory beanFactory) {
		List<InterceptorInfo> interceptors = interceptorResolutionStrategy.resolveRegisteredInterceptors(beanFactory);
		interceptors = interceptorOrderingStrategy.orderInterceptors(beanFactory, interceptors, getInterceptorOrder());
		InterceptorMetaDataBean metaDataBean = new InterceptorMetaDataBean(interceptors);
		beanFactory.registerSingleton(INTERCEPTOR_META_DATA_BEAN, metaDataBean);
		return metaDataBean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (interceptorResolutionStrategy == null) {
			interceptorResolutionStrategy = new SimpleInterceptorResolutionStrategy();
		}
		if (interceptorOrderingStrategy == null) {
			interceptorOrderingStrategy = new SimpleInterceptorOrderingStrategy();
		}
	}

	public void setInterceptorOrder(List<Class> interceptorOrder) {
		this.interceptorOrder = interceptorOrder;
	}

	public List<Class> getInterceptorOrder() {
		return interceptorOrder;
	}

}
