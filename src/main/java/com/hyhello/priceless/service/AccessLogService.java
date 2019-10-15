package com.hyhello.priceless.service;

import com.hyhello.priceless.dataaccess.entity.AccessLog;
import com.hyhello.priceless.dataaccess.repository.AccessLogRepository;
import com.hyhello.priceless.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *  TODO
 *  //文档快照? 截图?
 *
 *
 *  POPUP
 *  是否无痕模式√  默认否
 *  是否全局跨域√  默认否
 *  关闭通知√     默认否
 *
 *  OPTION
 *  服务器地址配置
 *  备用服务器地址配置
 *  白名单配置 [通配符正则] <云>
 *  identity配置
 *  地点配置
 *
 *  background
 *  <去重>
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

    public void saveAccessLog(AccessLog accessLog){

        accessLog.setHost(StringUtils.getHost(accessLog.getUrl()));
        accessLog.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        accessLog.setMark("test");
        accessLog.setLocation("hlx");

        accessLogRepository.save(accessLog);

    }
}
