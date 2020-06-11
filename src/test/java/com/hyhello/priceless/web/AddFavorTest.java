package com.hyhello.priceless.web;

import com.hyhello.priceless.support.BeanSupport;
import com.hyhello.priceless.web.service.FavoriteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AddFavorTest {

    @Test
    public void contextLoads() {
        FavoriteService service = BeanSupport.getApplicationContext().getBean(FavoriteService.class);
        service.addFavorite("https://www.bilibili.com/video/av3050");
    }
}
