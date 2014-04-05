package de.crowdcode.springcdi.interceptor.ct._91_C1.test1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.crowdcode.springcdi.interceptor.ct._91_C1.bindingtypes.Transactional;

@Component
@Qualifier("impl3Scoped")
@Transactional // must apply transaction interceptor
@Scope("session")
public class ScopedCT91_TrialService_Impl3 implements CT91_TrialService_Interface {

	@Override
	public String sayHello() {
		return "Hello";
	}

	@Override
	public String sayHello(String what) {
		return what;
	}

	@Override
	public String sayGoodBye() {
		return "Good bye";
	}

}
