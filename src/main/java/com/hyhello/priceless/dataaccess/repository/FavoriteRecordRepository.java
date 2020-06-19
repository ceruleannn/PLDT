package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.FavoriteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRecordRepository extends JpaRepository<FavoriteRecord, Integer> {

}
