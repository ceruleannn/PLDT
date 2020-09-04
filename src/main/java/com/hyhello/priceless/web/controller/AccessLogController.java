package com.hyhello.priceless.web.controller;

import com.hyhello.priceless.dataaccess.entity.AccessLog;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.web.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accessLog")
public class AccessLogController {

    @Autowired
    public AccessLogService accessLogService;

    @PostMapping("")
    @ResponseBody
    public Response logAccess(AccessLog accessLog){

        accessLogService.saveAccessLog(accessLog);
        return new Response(200, "访问记录成功: " + accessLog.getUrl() + " " + accessLog.getTitle());
    }


}
