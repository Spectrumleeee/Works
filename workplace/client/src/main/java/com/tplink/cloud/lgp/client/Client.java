package com.tplink.cloud.lgp.client;
import com.tplink.cloud.lgp.Observer.*;

public class Client {

	public Client(String hostname, int port) {
		
	}
	
	public static void main(String[] args) {
		
		Subject subject = new ConcreteSubject("AA");
		subject.SayHello();
	}
}