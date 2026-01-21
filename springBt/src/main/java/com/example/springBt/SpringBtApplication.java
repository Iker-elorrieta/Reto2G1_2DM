package com.example.springBt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import serverSocket.Servidor;

@SpringBootApplication
public class SpringBtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBtApplication.class, args);
		Servidor srvdr = new Servidor();
		srvdr.start();
	}

}
