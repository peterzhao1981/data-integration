package com.mode.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mode.entity.Statement;

/**
 * Created by zhaoweiwei on 2017/11/22.
 */
@Transactional
public interface StatementRepository extends JpaRepository<Statement, Long> {
}
