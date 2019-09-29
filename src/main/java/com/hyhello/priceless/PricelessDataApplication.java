package com.hyhello.priceless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.hyhello.*")
public class PricelessDataApplication {


    public static void main(String[] args) {

        SpringApplication.run(PricelessDataApplication.class, args);
    }

}
