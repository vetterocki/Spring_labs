package com.example.lab1.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class Second implements CommandLineRunner {
  @Override
  public void run(String... args) {
    System.out.println("Second");
  }
}

