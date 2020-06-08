package com.hyhello.priceless.web.controller;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import com.hyhello.priceless.dto.resp.AccessLogWhiteListResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.web.service.AccessLogWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */

@RestController
@RequestMapping("/accessLog/whiteList")
public class AccessLogWhiteListController {
    @Autowired
    public AccessLogWhiteListService accessLogWhiteListService;

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response deleteWhiteList(@PathVariable int id){

        accessLogWhiteListService.delete(id);
        return new Response(200, "delete white list successfully: " + id);
    }


    @GetMapping
    @ResponseBody
    public AccessLogWhiteListResponse selectWhiteList(){

        List<AccessLogWhiteList> list = accessLogWhiteListService.selectAll();
        return new AccessLogWhiteListResponse(200, "查找记录成功", list);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Response setStatus(@PathVariable int id, int status){

        accessLogWhiteListService.setStatus(id, status);
        return new Response(200, "修改状态成功!");
    }

    @PostMapping
    @ResponseBody
    public Response addWhiteList(AccessLogWhiteList accessLogWhiteList){

        accessLogWhiteListService.add(accessLogWhiteList);
        return new Response(200, "增加访问记录白名单成功!");
    }

}
