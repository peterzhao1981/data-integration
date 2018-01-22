package com.mode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mode.entity.DxmInventory;

/**
 * Created by zhaoweiwei on 2017/12/12.
 */
public interface DxmInventoryRepository extends JpaRepository<DxmInventory, Long> {
}
