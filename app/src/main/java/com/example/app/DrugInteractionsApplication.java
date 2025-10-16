package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class DrugInteractionsApplication {
  public static void main(String[] args) {
    SpringApplication.run(DrugInteractionsApplication.class, args);
  }
}
