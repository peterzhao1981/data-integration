package com.mode.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mode.entity.Order;

/**
 * Created by zhaoweiwei on 2017/11/22.
 */
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderNoNotIn(List<String> orderNoList);

    List<Order> findByOrderNoIn(List<String> orderNoList);
}
