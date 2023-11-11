package com.cyt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyt.reggie.common.R;
import com.cyt.reggie.dto.DishDto;
import com.cyt.reggie.entity.Category;
import com.cyt.reggie.entity.Dish;
import com.cyt.reggie.entity.DishFlavor;
import com.cyt.reggie.service.CategoryService;
import com.cyt.reggie.service.DishFlavorService;
import com.cyt.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author cyt
 * @version 1.0
 */
@RestController
@RequestMapping("/dish")
@SuppressWarnings({"all"})
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/hi")
    public void test() {
        System.out.println(11);
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dish 保存 {}", dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }

    /**
     * 菜品分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageisze = {}", page, pageSize);
        // 构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();  // 真正需要的对象
        // 构建条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        // 添加过滤条件
        lqw.like(name != null, Dish::getName, name);
        lqw.orderByDesc(Dish::getUpdateTime); // 更新时间降序排
        // 执行查询
        dishService.page(pageInfo, lqw);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records"); // 第三个参数忽略records属性
        List<Dish> records = pageInfo.getRecords();
        // 数据处理
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); // 分类id
            // 查找分类
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("dish 保存 {}", dishDto);
        dishService.updateWithFlavor(dishDto);
        // 清理所有菜品缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        // 精确清理某个菜品
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("添加菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        // 构造查询条件
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus, 1);
        // 添加排序条件
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lqw);
        return R.success(list);
    }*/

    /**
     * 根据条件查询对应的菜品数据
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        // 先从redis缓存查数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList != null) {
            // 如果存在，直接返回 无需查数据库
            return R.success(dishDtoList);
        }
        // 构造查询条件
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus, 1);
        // 添加排序条件
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(lqw);
        // 数据处理
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); // 分类id
            // 查找分类
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            // 查找口味
            Long dishId = item.getId(); // 菜品id
            LambdaQueryWrapper<DishFlavor> lqw1 = new LambdaQueryWrapper<DishFlavor>();
            lqw1.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> list1 = dishFlavorService.list(lqw1);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());
        // 如果不存在 查询数据库 缓存到redis
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}
