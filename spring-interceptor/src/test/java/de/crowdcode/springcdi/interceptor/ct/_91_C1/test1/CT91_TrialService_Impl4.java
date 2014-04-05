package de.crowdcode.springcdi.interceptor.ct._91_C1.test1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.crowdcode.springcdi.interceptor.ct._91_C1.bindingtypes.DataAccess;
import de.crowdcode.springcdi.interceptor.ct._91_C1.bindingtypes.Transactional;

@Component
@Qualifier("impl4")
public class CT91_TrialService_Impl4 implements CT91_TrialService_Interface {

	@Override
	@Transactional
	public String sayHello() {
		return "Hello";
	}

	@Override
	@DataAccess
	public String sayHello(String what) {
		return what;
	}

	@Override
	public String sayGoodBye() {
		return "Good bye";
	}

}
