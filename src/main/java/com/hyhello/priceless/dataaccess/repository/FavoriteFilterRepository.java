package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.AccessLogWhiteList;
import com.hyhello.priceless.dataaccess.entity.FavoriteFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FavoriteFilterRepository extends JpaRepository<FavoriteFilter, Integer> {

}