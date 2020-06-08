package com.hyhello.priceless.web.controller;

import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.web.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService service) {
        this.favoriteService = service;
    }

    @PutMapping
    @ResponseBody
    public Response addFavorite(String url) throws IOException {
        favoriteService.addFavorite(url);
        return new Response("");
    }
}