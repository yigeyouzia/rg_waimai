package com.cyt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyt.reggie.common.R;
import com.cyt.reggie.dto.SetmealDto;
import com.cyt.reggie.entity.Category;
import com.cyt.reggie.entity.Employee;
import com.cyt.reggie.entity.Setmeal;
import com.cyt.reggie.service.CategoryService;
import com.cyt.reggie.service.SetmealDishService;
import com.cyt.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cyt
 * @version 1.0
 */

/**
 * 套餐管理
 */
@SuppressWarnings({"all"})
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true) // 清除setmeal下的所有缓存
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageisze = {}, name = {}", page, pageSize, name);
        // 构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        // 构建条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        // 添加排序条件
        lqw.orderByDesc(Setmeal::getUpdateTime);
        // 执行查询
        setmealService.page(pageInfo, lqw);

        // 除了records拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        // 遍历records
        List<SetmealDto> setmealDtoList = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto(); // 目标对象
            BeanUtils.copyProperties(item, setmealDto);
            // 查找套餐分类名称name
            Category category = categoryService.getById(item.getCategoryId()); // 分类对象
            if (category != null) {
                String categoryName = category.getName(); // 分类名称
                setmealDto.setCategoryName(categoryName); // 设置分类名称
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true) // 清除setmeal下的所有缓存
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除ids：{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        lqw.eq(setmeal.getStatus() != null, Setmeal::getStatus, 1);
        List<Setmeal> list = setmealService.list(lqw);
        return R.success(list);
    }
}
