package com.hyhello.priceless.dataaccess.repository;

import com.hyhello.priceless.dataaccess.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

}
