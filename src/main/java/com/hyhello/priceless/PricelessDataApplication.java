package com.hyhello.priceless;

import com.hyhello.priceless.config.TokenConfig;
import com.hyhello.priceless.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories("com.hyhello.priceless.dataaccess.repository")
@ComponentScan("com.hyhello")
public class PricelessDataApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(PricelessDataApplication.class, args);
        NoteService service = context.getBean(NoteService.class);
        System.out.println(service.getNote(12).getText());
    }

}
