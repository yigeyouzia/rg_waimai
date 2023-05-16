package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.common.CustomException;
import com.cyt.reggie.entity.Category;
import com.cyt.reggie.entity.Dish;
import com.cyt.reggie.entity.Setmeal;
import com.cyt.reggie.mapper.CategoryMapper;
import com.cyt.reggie.service.CategoryService;
import com.cyt.reggie.service.DishService;
import com.cyt.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cyt
 * @version 1.0
 */
@Service
@SuppressWarnings({"all"})
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前判断是否有关联
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询是否关联菜品，如果关联 抛出异常
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 > 0) {
            // 已经关联菜品
            throw new CustomException("当前分类关联菜品，不能删除！");
        }
        // 查询是否关联套餐，如果关联 抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0) {
            // 已经关联了套餐
            throw new CustomException("当前分类关联套餐，不能删除！");
        }

        // 正常删除
        super.removeById(id);
    }
}
