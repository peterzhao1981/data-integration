package com.mode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mode.entity.DxmProduct;

/**
 * Created by zhaoweiwei on 2017/11/28.
 */
@Transactional
public interface DxmProductRepository extends JpaRepository<DxmProduct, Long> {

    DxmProduct findOneBySku(String sku);

    List<DxmProduct> findByProductNameCnIsNull();
}
