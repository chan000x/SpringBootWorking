package com.chandana.springTest;

import org.springframework.boot.SpringApplication;

public class TestSpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringTestApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
