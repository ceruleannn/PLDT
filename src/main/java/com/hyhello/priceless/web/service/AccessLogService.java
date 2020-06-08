package com.hyhello.priceless.web.service;

import com.hyhello.priceless.dataaccess.entity.AccessLog;
import com.hyhello.priceless.dataaccess.repository.AccessLogRepository;
import com.hyhello.priceless.utils.UrlUtils;
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
 *  关闭推送
 *
 *  OPTION <本地存储>
 *  服务器地址配置
 *  备用服务器地址配置
 *  identity配置
 *   地点配置
 *
 * *  白名单配置 [通配符正则] <云>
 *  background
 *  <去重>
 *
 *  书签管理
 *  onload (PING 协议) 检测服务器状态
 *  失败通知 队列,本地存储
 *
 *  install/uninstall 跳转
 *
 *  //推送流 通知
 *
 *  10月22日周 备案.去重.更好的记录监听.获取拓展
 */
@Service
public class AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    public void saveAccessLog(AccessLog accessLog){

        accessLog.setHost(UrlUtils.getHost(accessLog.getUrl()));
        accessLog.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        accessLog.setMark("test");
        accessLog.setLocation("hlx");

        accessLogRepository.save(accessLog);

    }
}
