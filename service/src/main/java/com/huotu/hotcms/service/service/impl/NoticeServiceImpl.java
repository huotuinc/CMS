package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Notice;
import com.huotu.hotcms.service.model.NoticeCategory;
import com.huotu.hotcms.service.model.thymeleaf.foreach.NormalForeachParam;
import com.huotu.hotcms.service.repository.NoticeRepository;
import com.huotu.hotcms.service.service.NoticeService;
import com.huotu.hotcms.service.util.PageData;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chendeyu on 2016/1/5.
 */
@Service
public class NoticeServiceImpl implements NoticeService {


    @Autowired
    NoticeRepository noticeRepository;


    @Override
    public PageData<NoticeCategory> getPage(Integer customerId, String title, int page, int pageSize) {
        PageData<NoticeCategory> data = null;
        Specification<Notice> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                predicates.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));
            }
            predicates.add(cb.equal(root.get("deleted").as(String.class), false));
            predicates.add(cb.equal(root.get("customerId").as(Integer.class), customerId));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Notice> pageData = noticeRepository.findAll(specification,new PageRequest(page - 1, pageSize));
        if (pageData != null) {
            List<Notice> notices =pageData.getContent();
            List<NoticeCategory> noticeCategoryList =new ArrayList<>();
            for(Notice notice : notices){
                NoticeCategory noticeCategory = new NoticeCategory();
                noticeCategory.setCategoryName(notice.getCategory().getName());
                noticeCategory.setCreateTime(notice.getCreateTime());
                noticeCategory.setCustomerId(notice.getCustomerId());
                noticeCategory.setId(notice.getId());
                noticeCategory.setContent(notice.getContent());
                noticeCategory.setTitle(notice.getTitle());
                noticeCategoryList.add(noticeCategory);
            }
            data = new PageData<NoticeCategory>();
            data.setPageCount(pageData.getTotalPages());
            data.setPageIndex(pageData.getNumber());
            data.setPageSize(pageData.getSize());
            data.setTotal(pageData.getTotalElements());
            data.setRows((NoticeCategory[])noticeCategoryList.toArray(new NoticeCategory[noticeCategoryList.size()]));
        }
        return  data;
    }

    @Override
    public Boolean saveNotice(Notice notice) {
        noticeRepository.save(notice);
        return true;
    }

    @Override
    public Notice findById(Long id) {
        Notice notice= noticeRepository.findOne(id);
        return notice ;
    }

    @Override
    public List<Notice> getSpecifyNotices(String[] specifyIds) {
        List<String> ids = Arrays.asList(specifyIds);
        List<Long> noticeIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        Specification<Link> specification = (root, query, cb) -> {
            List<Predicate> predicates = noticeIds.stream().map(id -> cb.equal(root.get("id").as(Long.class),id)).collect(Collectors.toList());
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        return noticeRepository.findAll(specification, new Sort(Sort.Direction.DESC,"orderWeight"));
    }

    @Override
    public List<Notice> getNoticeList(NormalForeachParam param) {
        Sort sort = new Sort(Sort.Direction.DESC,"orderWeight");
        Specification<Link> specification = (root, query, cb) -> {
            List<Predicate> predicates;
            if(!org.springframework.util.StringUtils.isEmpty(param.getExcludeids())) {
                List<String> ids = Arrays.asList(param.getExcludeids());
                List<Long> linkIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
                predicates = linkIds.stream().map(id -> cb.notEqual(root.get("id").as(Long.class),id)).collect(Collectors.toList());
            }else {
                predicates = new ArrayList<>();
            }
            predicates.add(cb.equal(root.get("category").get("id").as(Long.class),param.getCategoryid()));
            predicates.add(cb.equal(root.get("deleted").as(Boolean.class),false));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return noticeRepository.findAll(specification,new PageRequest(0,param.getSize(),sort)).getContent();
    }
}
