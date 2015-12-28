package com.huotu.hotcms.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 站点
 * Created by cwb on 2015/12/21.
 */
@Entity
@Table(name = "cms_site")
@Setter
@Getter
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siteId;

    /**
     * 商户ID
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 站点名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 标题，填写有助于搜索引擎优化
     */
    @Column(name = "title")
    private String title;

    /**
     * 关键字，填写有助于搜索引擎优化
     */
    @Column(name = "keywords")
    private String keywords;

    /**
     * 描述，填写有助于搜索引擎优化
     */
    @Column(name = "description")
    private String description;

    /**
     * 站点logo
     */
    @Column(name = "logoUri")
    private String logoUri;

    /**
     * 版权信息
     */
    @Column(name = "copyright")
    @Lob
    private String copyright;

    /**
     * 是否自定义模板
     */
    @Column(name = "custom")
    private boolean custom = false;

    /**
     * 自定义模板根路径
     */
    @Column(name = "customViewUrl")
    private String customTemplateUrl;


    /**
     * 对应域名
     */
    @ManyToMany
    @JoinTable(name = "cms_site_host",
            joinColumns = {@JoinColumn(name = "siteId",referencedColumnName = "siteId")},
            inverseJoinColumns = {@JoinColumn(name = "hostId",referencedColumnName = "hostId")}
    )
    private List<Host> hosts;

    /**
     * 站点创建时间
     */
    @Column(name = "createTime")
    private LocalDateTime createTime;

    /**
     * 站点更新时间
     */
    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    /**
     * 所属地区
     */
    @OneToOne
    @JoinColumn(name = "regionId")
    private Region region;

    public void addHost(Host host) {
        if(!this.hosts.contains(host)){
            this.hosts.add(host);
        }
    }

    public Site() {
        this.hosts = new ArrayList<>();
    }

    public void removeHost(Host host) {
        this.hosts.remove(host);
    }

}
