package com.huotu.hotcms.service.repository;

import com.huotu.hotcms.service.entity.RoutRuled;
import com.huotu.hotcms.service.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Created by xhl on 2016/1/5.
 */
public interface RoutRuleRepository  extends JpaRepository<RoutRuled, Long>,JpaSpecificationExecutor {
    Set<RoutRuled> findBySite(Site site);
}
