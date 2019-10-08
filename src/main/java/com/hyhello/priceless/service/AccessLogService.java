package com.hyhello.priceless.service;

import com.hyhello.priceless.dataaccess.entity.AccessLogEntity;
import com.hyhello.priceless.dataaccess.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *  TODO
 *  存储host
 *  无痕模式
 *  //文档快照? 截图?
 *
 *
 *  POPUP
 *  是否无痕模式√
 *  是否全局跨域√
 *  关闭通知√
 *
 *  OPTION
 *  服务器地址配置
 *  备用服务器地址配置
 *  白名单配置
 *  identity配置
 *  地点配置
 *
 *  background
 *  队列,本地存储
 *  书签管理
 *  onload (PING 协议) 检测服务器状态
 *  失败通知
 *
 */
@Service
public class AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    public void saveAccessLog(String url, String title){
        AccessLogEntity accessLog = new AccessLogEntity();
        accessLog.setUrl(url);
        accessLog.setTitle(title);
        accessLog.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));

        accessLogRepository.save(accessLog);

    }
}
