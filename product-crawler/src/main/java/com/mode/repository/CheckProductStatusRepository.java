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

    // findListID1与findListID2都是为了删除重复记录的操作
    @Query(value = "select id from check_product_status  where product_url is not null and product_url <>'' group by product_url", nativeQuery = true)
    List<Integer> findListID1();

    @Query(value = "select id from check_product_status where product_url is null or product_url ='' group by spu", nativeQuery = true)
    List<Integer> findListID2();

    // 查询出status不为success的url记录，然后进行爬虫。
    @Query(value = "select * from check_product_status where status <> ?1 or status is null", nativeQuery = true)
    List<CheckProductStatus> findCrawlerResult(String StatusParam);

    // 查询出status不为success的url记录但是1688的网页，然后进行爬虫。
    @Query(value = "select * from check_product_status where product_url like '%1688.com%'  and (status not in (?1,?2,?3)  or status is null or status='')", nativeQuery = true)
    List<CheckProductStatus> findCrawler1688Result(String exist, String invalid, String errorURL);

    // 重载上一个方法，由于没有去重，所以导出唯一的url，防止多次爬取，查询出status不为success的url记录但是1688的网页，然后进行爬虫。
    @Query(value = "select distinct product_url from check_product_status where product_url like '%1688.com%'  and (status not in (?1,?2)  or status is null or status='')", nativeQuery = true)
    List<String> findCrawler1688Result_1(String exist, String invalid);

    // 查询出status不为success的url记录但不是是1688的网页，然后进行爬虫。
    @Query(value = "select * from check_product_status where (product_url not like '%1688.com%'  or product_url is null)  and (status not in (?1,?2,?3) or status is null or status='') ", nativeQuery = true)
    List<CheckProductStatus> findCrawlerNot1688Result(String StatusParam, String productLack,
            String errorURL);

    // 重载上一个方法，查询出status不为success的url记录但不是是1688的网页，然后进行爬虫。
    @Query(value = "select distinct product_url from check_product_status where (product_url not like '%1688.com%'  or product_url is null )  and (status not in (?1,?2,?3) or status is null or status='') ", nativeQuery = true)
    List<String> findCrawlerNot1688Result_1(String StatusParam, String productLack,
            String productInvalid);

    // 将爬取到的，状态信息update到和数据库中。？1是占位符,根据id来插入
    @Modifying
    @Transactional
    @Query(value = "update check_product_status u set u.status=?1 where u.id=?2", nativeQuery = true)
    int updateStatus(String status, Long id);

    // 将爬取到的，状态信息update到和数据库中。方法重载，根据url来匹配商品，从而update，status字段，？1是占位符，0524修改
    @Modifying
    @Transactional
    @Query(value = "update check_product_status u set u.status=?1 where u.product_url =?2", nativeQuery = true)
    int updateStatus(String status, String url);

    // 导出查询结果
    @Query(value = "select * from check_product_status where (status <> ?1) ", nativeQuery = true)
    List<CheckProductStatus> getResult(String StatusParam);

    // 将断码信息更新到数据库中
    @Modifying
    @Transactional
    @Query(value = "update check_product_status u set u.lack_info=?1 where u.product_url=?2", nativeQuery = true)
    void updateLackInfo(String shortSize, String orignUrl);

}
