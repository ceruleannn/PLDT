package com.hyhello.priceless.controller;

import com.hyhello.priceless.dataaccess.entity.Note;
import com.hyhello.priceless.dto.resp.NoteResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.service.FavoriteService;
import com.hyhello.priceless.service.NoteService;
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