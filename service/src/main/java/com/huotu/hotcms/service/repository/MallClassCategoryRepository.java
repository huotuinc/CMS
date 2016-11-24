package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.MallClassCategory;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 商城类目数据源持久化类
 * Created by lhx on 2016/10/8.
 */

public interface MallClassCategoryRepository extends JpaRepository<MallClassCategory, Long>
        , JpaSpecificationExecutor<MallClassCategory> {

    List<MallClassCategory> findBySiteAndDeletedFalse(Site site);

    List<MallClassCategory> findByParent_SerialAndDeletedFalse(String serial);

}
