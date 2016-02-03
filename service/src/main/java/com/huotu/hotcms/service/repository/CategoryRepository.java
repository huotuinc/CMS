package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/31.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>,JpaSpecificationExecutor {
   List<Category> findBySiteAndDeletedOrderByOrderWeightDesc(Site site, Boolean deleted);
   List<Category> findBySiteAndDeletedAndNameContainingOrderByOrderWeightDesc(Site site,Boolean deleted,String name);


   List<Category> findByCustomerIdAndSite_SiteIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(Integer customerId,Long siteId,boolean deleted);
   List<Category> findByCustomerIdAndSite_SiteIdAndIdAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(Integer customerId,Long siteId,Long categoryId,boolean deleted);
   Set<Category> findByCustomerIdAndModelId(Integer customerId,Integer modelType);
   List<Category> findBySiteAndDeletedAndModelIdNotNullOrderByOrderWeightDesc(Site site,boolean deleted);

   Category findByRoute(Route route);

   List<Category> findByParentOrderByOrderWeightDesc(Category superCategory);

   List<Category> findBySite_SiteIdAndRoute_RouteTypeAndDeletedAndParentOrderByOrderWeightDesc(Long siteid, RouteType routetype, boolean b, Category parent);

   List<Category> findByParent_Id(Long parenId);
   List<Category> findBySite_SiteIdAndDeletedAndRoute_RouteType(Long siteId,boolean b,RouteType routeType);
   List<Category> findBySite_SiteIdAndRoute_RouteTypeAndDeletedOrderByOrderWeightDesc(Long siteid, RouteType routetype, boolean b);

   List<Category> findByParentIdsContainingAndDeleted(String parentId,boolean deleted);
}
