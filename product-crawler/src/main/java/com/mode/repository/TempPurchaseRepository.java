package com.mode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mode.entity.TempPurchase;

/**
 * Created by zhaoweiwei on 2017/12/13.
 */
public interface TempPurchaseRepository extends JpaRepository<TempPurchase, Long> {

    @Query("SELECT DISTINCT t.tempNo FROM TempPurchase t")
    List<String> findDistinctTempNo();

    List<TempPurchase> findByTempNo(String tempNo);
}
