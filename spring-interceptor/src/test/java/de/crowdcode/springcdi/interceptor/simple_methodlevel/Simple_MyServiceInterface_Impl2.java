package de.crowdcode.springcdi.interceptor.simple_methodlevel;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.crowdcode.springcdi.interceptor.simple.ReturnValueAdopted;
import de.crowdcode.springcdi.interceptor.simple.Simple_MyServiceInterface;

@Component
@Qualifier("impl2")
public class Simple_MyServiceInterface_Impl2 implements Simple_MyServiceInterface {

	@Override
	@ReturnValueAdopted
	public String sayHello() {
		return "Hello";
	}

	@Override
	@ReturnValueAdopted
	public String sayHello(String what) {
		return what;
	}

	@Override
	public String sayGoodBye() {
		return "Good bye";
	}
	
}
