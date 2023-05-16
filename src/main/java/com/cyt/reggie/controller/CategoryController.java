package com.cyt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyt.reggie.common.R;
import com.cyt.reggie.entity.Category;
import com.cyt.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cyt
 * @version 1.0
 */
@RestController
@RequestMapping("/category")
@SuppressWarnings({"all"})
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("菜+{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("page = {}, pageisze = {}", page, pageSize);
        // 构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 构建条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper();
        // 添加排序条件
        lqw.orderByDesc(Category::getSort);
        // 执行查询
        categoryService.page(pageInfo, lqw);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类  ids = {}", ids);
        categoryService.remove(ids);
        return R.success("删除分类成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 获取菜品分类
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getList(Category category) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        // 查询条件： type=1 菜品分类
        lqw.eq(category.getType() != null, Category::getType, category.getType());
        // 添加排序条件
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }
}
