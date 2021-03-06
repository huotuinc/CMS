/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import com.huotu.cms.manage.page.support.AbstractCRUDPage;
import com.huotu.cms.manage.page.support.BodyId;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.MallClassCategory;
import com.huotu.hotcms.service.entity.MallProductCategory;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@BodyId("fa-bars")
public class CategoryPage extends AbstractCRUDPage<Category> {

    public CategoryPage(WebDriver webDriver) {
        super("categoryForm", webDriver);
    }

    @Override
    protected void fillValueToForm(Category category) {
        WebElement form = getForm();
        assertThat(form.isDisplayed())
                .isTrue();
        inputText(form, "name", category.getName());
        inputSelect(form, "contentType", category.getContentType().getValue().toString());
        if (category.getParent() != null) {
            inputSelect(form, "extra", category.getParent().getName());
        } else {
            inputSelect(form, "extra", "无");
        }
        if (category instanceof MallProductCategory) {
            inputText(form, "goodTitle", ((MallProductCategory) category).getGoodTitle());
            inputText(form, "minPrice", ((MallProductCategory) category).getMinPrice().toString());
            inputText(form, "salesCount", ((MallProductCategory) category).getSalesCount().toString());
            inputText(form, "maxPrice", ((MallProductCategory) category).getMaxPrice().toString());
            if (((MallProductCategory) category).getMallCategoryId() != null)
                for (Long id : ((MallProductCategory) category).getMallCategoryId()) {
                    selectOption(form, "mallCategoryId", id + "");
                }
            if (((MallProductCategory) category).getMallBrandId() != null)
                for (Long id : ((MallProductCategory) category).getMallBrandId()) {
                    selectOption(form, "mallBrandId", id + "");
                }
            if (((MallProductCategory) category).getGallery() != null) {
                selectOption(form, "gallery", ((MallProductCategory) category).getGallery().getId() + "");
            }
        } else if (category instanceof MallClassCategory) {
            if (((MallClassCategory) category).getRecommendCategory() != null) {
                inputSelect(form, "recommendCategory", ((MallClassCategory) category).getRecommendCategory().getTitle());
            }
            if (((MallClassCategory) category).getCategories() != null && !((MallClassCategory) category).getCategories().isEmpty()) {
                for (Category c : ((MallClassCategory) category).getCategories()) {
                    inputSelect(form, "categories", c.getTitle());
                }

            }
        }
    }

    @Override
    protected void fillValueToFormForUpdate(Category category) {
        WebElement form = getForm();
        assertThat(form.isDisplayed())
                .isTrue();
        inputText(form, "name", category.getName());
        if (category instanceof MallProductCategory) {
            inputText(form, "goodTitle", ((MallProductCategory) category).getGoodTitle());
            inputText(form, "minPrice", ((MallProductCategory) category).getMinPrice().toString());
            inputText(form, "salesCount", ((MallProductCategory) category).getSalesCount().toString());
            inputText(form, "maxPrice", ((MallProductCategory) category).getMaxPrice().toString());
            if (((MallProductCategory) category).getMallCategoryId() != null)
                for (Long id : ((MallProductCategory) category).getMallCategoryId()) {
                    selectOption(form, "mallCategoryId", id + "");
                }
            if (((MallProductCategory) category).getMallBrandId() != null)
                for (Long id : ((MallProductCategory) category).getMallBrandId()) {
                    selectOption(form, "mallBrandId", id + "");
                }
            if (((MallProductCategory) category).getGallery() != null) {
                selectOption(form, "gallery", ((MallProductCategory) category).getGallery().getId() + "");
            }
        } else if (category instanceof MallClassCategory) {
            if (((MallClassCategory) category).getRecommendCategory() != null) {
                inputSelect(form, "recommendCategory", ((MallClassCategory) category).getRecommendCategory().getTitle());
            }

            if (((MallClassCategory) category).getCategories() != null) {
                for (Category c : ((MallClassCategory) category).getCategories()) {
                    inputSelect(form, "categories", c.getTitle() + "");
                }
            }
        }
    }

    @Override
    public Predicate<? super WebElement> findRow(Category value) {
        return row -> {
            String id = row.getAttribute("data-id");
            return value.getId().toString().equals(id);
        };
    }

    @Override
    protected Predicate<WebElement> rowPredicate(Category value) {
        return row -> {
            List<WebElement> tds = row.findElements(By.tagName("td"));

            assertThat(tds)
                    .haveAtLeastOne(new Condition<>(td -> td.getText().contains(value.getName()), "需显示数据源名称"));
            assertThat(tds)
                    .haveAtLeastOne(new Condition<>(td
                            -> td.getText()
                            .contains(value.getContentType() == null ? "" : value.getContentType().getValue().toString()), "需显示数据源模型"));
            if (value.getParent() != null)
                assertThat(tds)
                        .haveAtLeastOne(new Condition<>(td
                                -> td.getText().contains(value.getParent().getName()), "需显示数据源父级"));

            return true;
        };
    }

    public void assertMallDisabled() {
        // 一般页面都应该没有 mallProductCategory mallClassCategory
        // 也无法选择 8 9
        assertThat(webDriver.findElements(By.className("mallProductCategory")))
                .isEmpty();
        assertThat(webDriver.findElements(By.className("mallClassCategory")))
                .isEmpty();

        openPanelBody();
        assertThat(webDriver.findElement(By.name("contentType")).findElements(By.tagName("option"))
                .stream()
                .filter(webElement -> webElement.getText().contains("伙伴商城"))
                .count())
                .isEqualTo(0);
    }

    public void assertMallEnable() {
        assertThat(webDriver.findElements(By.className("mallProductCategory")))
                .isNotEmpty();
        assertThat(webDriver.findElements(By.className("mallClassCategory")))
                .isNotEmpty();

        openPanelBody();
        assertThat(webDriver.findElement(By.name("contentType")).findElements(By.tagName("option"))
                .stream()
                .filter(webElement -> webElement.getText().contains("伙伴商城"))
                .count())
                .isGreaterThan(0);
    }
}
