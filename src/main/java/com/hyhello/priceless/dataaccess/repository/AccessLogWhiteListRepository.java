package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccessLogWhiteListRepository extends JpaRepository<AccessLogWhiteList, Integer> {

}