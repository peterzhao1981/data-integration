package com.mode.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mode.entity.Parcel;

/**
 * Created by zhaoweiwei on 2017/11/22.
 */
@Transactional
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    Parcel findOneByParcelNo(String parcelNo);
}
