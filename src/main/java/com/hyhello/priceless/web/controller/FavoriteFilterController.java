package com.hyhello.priceless.web.controller;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import com.hyhello.priceless.dataaccess.entity.FavoriteFilter;
import com.hyhello.priceless.dto.resp.AccessLogWhiteListResponse;
import com.hyhello.priceless.dto.resp.FavoriteFilterResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.web.service.AccessLogWhiteListService;
import com.hyhello.priceless.web.service.FavoriteFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */

@RestController
@RequestMapping("/favorite/filter")
public class FavoriteFilterController {
    @Autowired
    public FavoriteFilterService favoriteFilterService;

    @GetMapping
    @ResponseBody
    public FavoriteFilterResponse select(){

        List<FavoriteFilter> list = favoriteFilterService.selectAll();
        return new FavoriteFilterResponse(200, "查找记录成功", list);
    }
}
