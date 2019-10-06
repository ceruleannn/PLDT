package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLogEntity, Long> {

}
