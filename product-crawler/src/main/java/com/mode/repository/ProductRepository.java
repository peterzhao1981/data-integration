package com.mode.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mode.entity.Product;

/**
 * Created by zhaoweiwei on 2017/11/10.
 */
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(String status, Pageable pageable);

}
