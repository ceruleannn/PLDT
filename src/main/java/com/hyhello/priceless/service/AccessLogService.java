package com.hyhello.priceless.service;

import com.hyhello.priceless.dataaccess.entity.AccessLogEntity;
import com.hyhello.priceless.dataaccess.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *  TODO 失败通知 / 本地存储 /
 *  前端屏蔽前缀例如 chrome://
 *  存储host
 *  无痕模式
 *  //文档快照? 截图?
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
