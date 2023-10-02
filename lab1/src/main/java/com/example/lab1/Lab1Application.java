package com.example.lab1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab1Application {

  public static void main(String[] args) {
    System.out.println("Begin of main");
    SpringApplication.run(Lab1Application.class, args);
    System.out.println("End of main");
  }

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> System.out.println("Hello from Spring Boot!");
  }

}
