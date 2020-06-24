package com.hyhello.priceless.web.service;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import com.hyhello.priceless.dataaccess.entity.FavoriteFilter;
import com.hyhello.priceless.dataaccess.repository.AccessLogWhiteListRepository;
import com.hyhello.priceless.dataaccess.repository.FavoriteFilterRepository;
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
public class FavoriteFilterService {

    private final FavoriteFilterRepository repository;

    @Autowired
    public FavoriteFilterService(FavoriteFilterRepository repository) {
        this.repository = repository;
    }

    public List<FavoriteFilter> selectAll(){
        return repository.findAll();
    }
}
