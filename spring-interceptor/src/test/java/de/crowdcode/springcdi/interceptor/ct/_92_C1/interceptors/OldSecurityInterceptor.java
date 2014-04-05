package de.crowdcode.springcdi.interceptor.ct._92_C1.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import de.crowdcode.springcdi.interceptor.ct._92_C1.bindingtypes.Secured;

@Interceptor @Secured
public class OldSecurityInterceptor {

	@AroundInvoke
	public Object manageTransaction(InvocationContext ctx) throws Exception {
		String result = null;
		ctx.getContextData().put(this.getClass().getName(), "");
		try {
			result = (String)ctx.proceed();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result + "_oldinterceptor_";
	}
	
}
