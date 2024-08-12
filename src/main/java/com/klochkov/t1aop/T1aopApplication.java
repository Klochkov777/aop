package com.klochkov.t1aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy()
public class T1aopApplication {

	public static void main(String[] args) {
		SpringApplication.run(T1aopApplication.class, args);
	}

}
