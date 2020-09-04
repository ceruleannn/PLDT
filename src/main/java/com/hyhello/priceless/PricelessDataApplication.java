package com.hyhello.priceless;

import com.hyhello.priceless.fileconfig.CommonConfig;
import com.hyhello.priceless.web.service.BilibiliFavoriteListService;
import com.hyhello.priceless.web.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(CommonConfig.class)
@EnableJpaRepositories("com.hyhello.priceless.dataaccess.repository")
@ComponentScan("com.hyhello")
@Slf4j
public class PricelessDataApplication {

    public static void main(String[] args) throws IOException {

        ConfigurableApplicationContext context = SpringApplication.run(PricelessDataApplication.class, args);
        FavoriteService service = context.getBean(FavoriteService.class);
       //service.addFavorite("https://www.youtube.com/watch?v=2eYsztRFjjs");
       //service.addFavorite("https://www.bilibili.com/video/BV1qK4y1t7uK/?spm_id_from=333.788.videocard.7");
       //service.addFavorite("https://www.bilibili.com/video/av88329107/");

        BilibiliFavoriteListService bilibiliFavoriteListService = context.getBean(BilibiliFavoriteListService.class);
        bilibiliFavoriteListService.execute(0);
        //Map<String, TokenConfig> map = context.getBeansOfType(TokenConfig.class);
        //map.values().forEach(System.out::println);


//        YouGetHandler handler = (url, file) -> {
//            System.out.println(file.getName());
//        };
//        YouGetService service = context.getBean(YouGetService.class);
//        service.offerTask("https://www.bilibili.com/video/av301", handler);


        //log.info(MarkerFactory.getMarker("m1"), "debug marker");
    }

}
