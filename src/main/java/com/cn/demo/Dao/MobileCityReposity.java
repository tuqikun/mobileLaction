package com.cn.demo.Dao;

import com.cn.demo.domain.MobileCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileCityReposity extends JpaRepository<MobileCity,Long>{

    /**
     * @param mobile
     * @return
     */
    MobileCity findByMobile(String mobile);
}
