package com.mode.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/*
 * @author maxiaodong on 2018/05/010
 * @version 0.0.1
 */
@Transactional
public interface ProductPriceStore extends JpaRepository<com.mode.entity.ProductPriceStore, Long> {

    // 将爬取到的价格，状态信息update到和数据库中。
    @Modifying
    @Transactional
    @Query(value = "insert ignore into product_price_store (spu,price) values(?1,?2)", nativeQuery = true)
    int setCheckProductStatusStatus(String spu, BigDecimal price);
}
