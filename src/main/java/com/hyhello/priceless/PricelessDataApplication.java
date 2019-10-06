package com.hyhello.priceless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.hyhello.priceless.dataaccess.repository")
@ComponentScan("com.hyhello")
public class PricelessDataApplication {


    public static void main(String[] args) {

        SpringApplication.run(PricelessDataApplication.class, args);
    }

}
