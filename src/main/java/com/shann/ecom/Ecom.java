package com.shann.ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class Ecom {

  public static void main(String[] args) {
    SpringApplication.run(Ecom.class, args);
  }

}
