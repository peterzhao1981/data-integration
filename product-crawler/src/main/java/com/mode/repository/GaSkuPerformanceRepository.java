package com.mode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mode.entity.GaSkuPerformance;

/**
 * Created by zhaoweiwei on 2017/11/28.
 */
@Transactional
public interface GaSkuPerformanceRepository extends JpaRepository<GaSkuPerformance, Long> {
}
