package com.mode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mode.entity.Purchase;

/**
 * Created by zhaoweiwei on 2017/12/5.
 */
@Transactional
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
