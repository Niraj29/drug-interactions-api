package com.example.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackages = {
      "com.example.app",
      "com.example.domain",
      "com.example.adapters.memory",
      "com.example.adapters.openfda"
    })
public class ApplicationConfig {}
