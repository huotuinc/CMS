/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long>, JpaSpecificationExecutor<GalleryItem> {
    List<GalleryItem> findByGallery(Gallery gallery);

    Long deleteByGallery(Gallery gallery);

    Page<GalleryItem> findByGallery_Category_Site(Site site, Pageable pageable);
}
