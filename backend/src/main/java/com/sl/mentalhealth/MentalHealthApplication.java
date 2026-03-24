package com.sl.mentalhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class MentalHealthApplication {

  public static void main(String[] args) {
    SpringApplication.run(MentalHealthApplication.class, args);
  }
}