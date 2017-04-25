package trab08chat;

import javax.jws.WebService;

@WebService(endpointInterface = "trab08chat.JavaServer")
public class JavaServerImpl implements JavaServer {

	public String sayHello(String name) {
		return "Hello " + name + " !, Hope you are doing well !!";
	}

}
