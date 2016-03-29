package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
public interface CustomPagesRepository extends JpaRepository<CustomPages, Long>,JpaSpecificationExecutor {
    List<CustomPages> findByHome(Boolean home);
}
