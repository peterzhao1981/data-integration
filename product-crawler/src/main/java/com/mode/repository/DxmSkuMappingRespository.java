package com.mode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mode.entity.DxmSkuMapping;

/**
 * Created by zhaoweiwei on 2017/12/16.
 */
@Transactional
public interface DxmSkuMappingRespository extends JpaRepository<DxmSkuMapping, Long> {

}
