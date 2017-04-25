package hello;

import javax.jws.WebService;

@WebService(endpointInterface = "hello.HelloWorldServer")
public class JavaServerImpl implements HelloWorldServer {

	public String sayHello(String name) {
		return "Hello " + name + " !, Hope you are doing well !!";
	}

}
