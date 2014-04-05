package de.crowdcode.springcdi.interceptor.ct._91_C1.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import de.crowdcode.springcdi.interceptor.ct._91_C1.bindingtypes.Transactional;

@Interceptor @Transactional
public class TransactionInterceptor {

	@AroundInvoke
	public Object manageTransaction(InvocationContext ctx) throws Exception {
		String result = null;
		ctx.getContextData().put(this.getClass().getName(), "");
		try {
			result = (String)ctx.proceed();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result + "_transactioninterceptor_";
	}
	
}
