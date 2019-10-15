package com.hyhello.priceless.service;

import com.hyhello.priceless.dataaccess.entity.AccessLog;
import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import com.hyhello.priceless.dataaccess.repository.AccessLogWhiteListRepository;
import com.hyhello.priceless.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 */

@Service
public class AccessLogWhiteListService {

    @Autowired
    private AccessLogWhiteListRepository repository;

    public void add(AccessLogWhiteList accessLogWhiteList){
        accessLogWhiteList.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        repository.save(accessLogWhiteList);
    }

    public List<AccessLogWhiteList> selectAll(){
        return repository.findAll();
    }

    public void delete(int id){
        repository.deleteById(id);
    }

    public void setStatus(int id, int status){
        Optional<AccessLogWhiteList> op = repository.findById(id);
        if (op.isPresent()){
            AccessLogWhiteList white = op.get();
            white.setStatus((byte) status);
            repository.save(white);
        }

    }

}
