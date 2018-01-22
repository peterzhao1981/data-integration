package com.mode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mode.entity.GaSkuPerformance;
import com.mode.repository.GaSkuPerformanceRepository;

/**
 * Created by zhaoweiwei on 2017/11/28.
 */
@Service
public class GaStatisticsService {

    @Autowired
    private GaSkuPerformanceRepository gaSkuPerformanceRepository;

    public void process() {
        List<GaSkuPerformance> gaSkuPerformanceList = gaSkuPerformanceRepository.findAll();
        for (GaSkuPerformance gaSkuPerformance : gaSkuPerformanceList) {
            String skuName = gaSkuPerformance.getSkuName();
        }
    }

}
