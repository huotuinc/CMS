/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.entity.login;

import com.huotu.hotcms.service.Enabled;
import com.huotu.hotcms.service.entity.AbstractContent;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Site;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * cms 的owner,拥有一个可选的商户号对应伙伴商城
 *
 * @author CJ
 */
@Entity
@Table(name = "cms_owner", uniqueConstraints = {@UniqueConstraint(columnNames = {"customerId"})})
@Getter
@Setter
public class Owner extends AbstractLogin implements Serializable, Enabled {

    private static final long serialVersionUID = 4886150755976285080L;
    /**
     * 可选商户号
     */
    @Column(name = "customerId", unique = true)
    private Integer customerId;

    @Transient
    private transient Long siteId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        if (!super.equals(o)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(customerId, owner.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId);
    }

    @Override
    public boolean siteManageable(Site site) {
        if (site.getOwner() == null)
            return false;
        return site.getOwner().equals(this);
    }

    @Override
    public boolean hostManageable(Host host) {
        return host.getOwner().equals(this);
    }

    @Override
    public boolean contentManageable(AbstractContent content) {
        return categoryManageable(content.getCategory());
    }

    @Override
    public boolean categoryManageable(Category category) {
        return siteManageable(category.getSite());
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public String getUsername() {
        if (getLoginName() == null)
            return String.valueOf(getCustomerId());
        if (getCustomerId() == null)
            return super.getLoginName();
        return getLoginName() + "/" + getCustomerId();
    }

    @Override
    public Long currentOwnerId() {
        return getId();
    }

    @Override
    public Long currentSiteId() {
        return siteId;
    }

    @Override
    public void updateSiteId(Long siteId) {
        this.siteId = siteId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(Role_Manage));
//        return CollectionUtils.mutliSet(
////                new SimpleGrantedAuthority("ROOT")
//                , new SimpleGrantedAuthority(Role_Manage)
//        );
    }
}
