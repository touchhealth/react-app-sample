package br.com.touchhealth.reactapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableWebMvc
@EnableSpringDataWebSupport
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
