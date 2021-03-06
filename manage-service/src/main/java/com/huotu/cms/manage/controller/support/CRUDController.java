/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.support;

import com.huotu.cms.manage.MethodParameterFixed;
import com.huotu.cms.manage.bracket.GritterUtils;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.Auditable;
import com.huotu.hotcms.service.Enabled;
import com.huotu.hotcms.service.ResourcesOwner;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.service.CommonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 更好的支持{@link com.huotu.hotcms.service.Enabled}以及{@link com.huotu.hotcms.service.Auditable}
 *
 * @param <T>  资源类型
 * @param <ID> 资源主键类型
 * @param <PD> 持久时共同请求的额外数据
 * @param <MD> 修改时共同请求的额外数据
 * @author CJ
 */
public abstract class CRUDController<T, ID extends Serializable, PD, MD> {

    private static final Log log = LogFactory.getLog(CRUDController.class);
    @Autowired
    protected CommonService commonService;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected JpaRepository<T, ID> jpaRepository;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String add(HttpServletRequest request, @AuthenticationPrincipal Login login, @MethodParameterFixed T data
            , @MethodParameterFixed PD extra, Model model, RedirectAttributes attributes) throws RedirectException {
        try {
            data = preparePersist(request, login, data, extra, attributes);

            if (data instanceof Auditable) {
                ((Auditable) data).setCreateTime(LocalDateTime.now());
            }

            data = jpaRepository.save(data);

            return postPersist(login, request, data, model, attributes);
        } catch (IllegalArgumentException ex) {
            throw new RedirectException(rootUri(), ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("Unknown Exception on Add", ex);
            throw new RedirectException(rootUri(), ex);
        }

    }

    /**
     * 添加一个数据以后返回的页面
     *
     * @param login      登录
     * @param request    请求实体
     * @param data
     * @param model
     * @param attributes @return
     */
    @SuppressWarnings("WeakerAccess")
    protected String postPersist(Login login, HttpServletRequest request, T data, Model model, RedirectAttributes attributes) {
        GritterUtils.AddFlashSuccess("成功添加", attributes);

        return redirectIndexViewName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void doDelete(@AuthenticationPrincipal Login login, @PathVariable("id") ID id) throws RedirectException {
        T entity = jpaRepository.getOne(id);
        doDelete(login, entity);
    }

    private void doDelete(Login login, T entity) throws RedirectException {
        prepareRemove(login, entity);
        jpaRepository.delete(entity);
        if (entity instanceof ResourcesOwner) {
            try {
                commonService.deleteResource((ResourcesOwner) entity);
            } catch (IOException e) {
                throw new RedirectException(rootUri(), e.getLocalizedMessage(), e);
            }
        }
    }

    @RequestMapping(value = "/{id}/enabled", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void toggleEnable(@PathVariable("id") ID id) {
        T data = jpaRepository.getOne(id);
        if (data instanceof Enabled) {
            ((Enabled) data).setEnabled(!((Enabled) data).isEnabled());
            log.info("" + data + " Toggle to " + ((Enabled) data).isEnabled());
        } else
            throw new NoSuchMethodError();
    }

    @RequestMapping(value = "/{id}/enabled", method = RequestMethod.GET)
    @Transactional
    public String toggleEnableGet(@PathVariable("id") ID id) {
        toggleEnable(id);
        return redirectIndexViewName();
    }

    // 用这种方式,必然是需要302回主界面
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @Transactional
    public String delete(@AuthenticationPrincipal Login login, @PathVariable("id") ID id
            , RedirectAttributes attributes) throws RedirectException {
        doDelete(login, id);
        return redirectIndexViewName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String open(@AuthenticationPrincipal Login login, HttpServletRequest request, @PathVariable("id") ID id
            , Model model
            , RedirectAttributes attributes) throws RedirectException {
        T data = jpaRepository.getOne(id);
        model.addAttribute("object", data);
        prepareOpen(login, request, data, model, attributes);
        return openViewName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @Transactional
    public String save(@AuthenticationPrincipal Login login, HttpServletRequest request, @PathVariable("id") ID id, @MethodParameterFixed T data
            , @MethodParameterFixed MD extra, Model model, RedirectAttributes attributes) throws RedirectException {
        T entity = jpaRepository.getOne(id);
        try {
            prepareUpdate(login, entity, data, extra, attributes, request);
            if (entity instanceof Auditable) {
                ((Auditable) entity).setUpdateTime(LocalDateTime.now());
            }
            jpaRepository.save(entity);

            return postUpdate(login, request, entity, data, model, attributes);
        } catch (IllegalArgumentException ex) {
            throw new RedirectException(rootUri(), ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.info("unknown exception on save", ex);
            throw new RedirectException(rootUri(), ex);
        }

    }

    /**
     * 更新成功以后打开的页面
     *
     * @param login
     * @param request
     * @param entity
     * @param data
     * @param model
     * @param attributes @return
     */
    @SuppressWarnings("WeakerAccess")
    protected String postUpdate(Login login, HttpServletRequest request, T entity, T data, Model model, RedirectAttributes attributes) {
        GritterUtils.AddFlashSuccess("保存成功", attributes);
        return redirectIndexViewName();
    }

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, "*/*"})
    @Transactional(readOnly = true)
    public String index(@AuthenticationPrincipal Login login, HttpServletRequest request, Model model, RedirectAttributes attributes) throws RedirectException {
        try {
            Specification<T> specification = prepareIndex(login, request, model, attributes);
            if (specification == null) {
                if (request.getAttribute("sort") != null) {
                    model.addAttribute("list", jpaRepository.findAll((Sort) request.getAttribute("sort")));
                } else {
                    model.addAttribute("list", jpaRepository.findAll());
                }
            } else {
                if (request.getAttribute("sort") != null) {
                    model.addAttribute("list", jpaSpecificationExecutor.findAll(specification, (Sort) request.getAttribute("sort")));
                } else {
                    model.addAttribute("list", jpaSpecificationExecutor.findAll(specification));
                }
            }

        } catch (IllegalArgumentException ex) {
            throw new RedirectException(rootUri(), ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.info("unknown exception on index", ex);
            // 这里可不能返回root 不是就死循环了,  应该返回
            throw new RedirectException("/manage", ex);
        }

//        if (searchText != null) {
//            String toSearch = "%" + searchText + "%";
//
//            model.addAttribute("list", ownerRepository.findAll((root, query, cb) -> {
//                return cb.or(cb.equal(root.get("customerId"), toSearch), cb.like(root.get("loginName"), toSearch));
//            }, pageable));
//        } else
//            model.addAttribute("list", ownerRepository.findAll(pageable));

        return indexViewName();
    }

    /**
     * @param login      当前操作者的身份
     * @param request    实际请求
     * @param model      模型
     * @param attributes 空间  @return 搜索规格, null表示无规格要求
     * @throws RedirectException 需要转发到其他地址
     */
    @SuppressWarnings("WeakerAccess")
    protected Specification<T> prepareIndex(Login login, HttpServletRequest request, Model model, RedirectAttributes attributes) throws RedirectException {
        return null;
    }

    /**
     * @return index的视图名称
     */
    protected abstract String indexViewName();

    /**
     * 在新增持久一个资源之前
     *
     * @param request    当前请求
     * @param login      当前操作者的身份
     * @param data       来自用户的数据
     * @param extra      额外数据
     * @param attributes 空间
     * @return 提交到持久层的数据
     * @throws RedirectException 让视图转向
     */
    protected abstract T preparePersist(HttpServletRequest request, Login login, T data, PD extra, RedirectAttributes attributes)
            throws RedirectException;

    /**
     * @return 重定向到索引界面的视图名称
     */
    @SuppressWarnings("WeakerAccess")
    protected String redirectIndexViewName() {
        return "redirect:" + rootUri();
    }

    protected String rootUri() {
        return AnnotationUtils.findAnnotation(getClass(), RequestMapping.class).value()[0];
    }

    /**
     * 在删除某一个资源之前
     *
     * @param login  当前操作者的身份
     * @param entity 主键
     */
    @SuppressWarnings("WeakerAccess")
    protected void prepareRemove(Login login, T entity) throws RedirectException {

    }

    /**
     * 准备展示一个资源
     *
     * @param login   当前操作者的身份
     * @param request
     * @param data    资源
     * @param model   模型
     */
    @SuppressWarnings("WeakerAccess")
    protected void prepareOpen(Login login, HttpServletRequest request, T data, Model model, RedirectAttributes attributes)
            throws RedirectException {

    }

    /**
     * 更新之前
     *
     * @param login      当前操作者的身份
     * @param entity     数据
     * @param data       用户请求的数据
     * @param extra      额外数据
     * @param attributes 空间
     * @param request
     */
    protected abstract void prepareUpdate(Login login, T entity, T data, MD extra, RedirectAttributes attributes, HttpServletRequest request)
            throws RedirectException;

    /**
     * @return 打开一个资源的视图名称
     */
    protected abstract String openViewName();


}
