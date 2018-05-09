package com.mode.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mode.entity.CheckProductStatus;

/*
 * @author maxiaodong on 2018/05/07
 * @version 0.0.1
 */
@Transactional
public interface CheckProductStatusRepository extends JpaRepository<CheckProductStatus, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from check_product_status where  id not in (:listParam)", nativeQuery = true)
    void deleteCheckProductStatus(@Param("listParam") List<Integer> listParam);

    @Query(value = "select id from check_product_status  where product_url is not null and product_url <>'' group by product_url", nativeQuery = true)
    List<Integer> findListID1();

    @Query(value = "select id from check_product_status where product_url is null or product_url ='' group by spu", nativeQuery = true)
    List<Integer> findListID2();

    // 查询出status不为success的url记录，然后进行爬虫。
    @Query(value = "select * from check_product_status where product_url is not null and product_url <>'' and status <> 'product exist'", nativeQuery = true)
    List<CheckProductStatus> findCrawlerResult();

    // 将爬取到的，状态信息update到和数据库中。？1是占位符
    @Modifying
    @Transactional
    @Query("update check_product_status u set u.status=?1 where u.id=?2")
    int setCheckProductStatusStatus(String status, Long id);

}
