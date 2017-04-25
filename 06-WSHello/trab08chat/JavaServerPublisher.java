package trab08chat;

import javax.xml.ws.Endpoint;

public class JavaServerPublisher {

	public static void main(String[] args) {

		System.out.println("Beginning to publish JavaService now");
		Endpoint.publish("http://127.0.0.1:9876/hw", new JavaServerImpl());
		System.out.println("Done publishing");
	}

}
