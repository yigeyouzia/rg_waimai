package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.common.CustomException;
import com.cyt.reggie.dto.SetmealDto;
import com.cyt.reggie.entity.Setmeal;
import com.cyt.reggie.entity.SetmealDish;
import com.cyt.reggie.mapper.SetmealMapper;
import com.cyt.reggie.service.SetmealDishService;
import com.cyt.reggie.service.SetmealService;
import org.apache.ibatis.javassist.tools.reflect.CannotCreateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cyt
 * @version 1.0
 */
@Service
@SuppressWarnings({"all"})
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐 同时保存套餐的菜品
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 保存套餐和菜品信息  setmeal_dish
        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * 删除套餐 同时删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 判断是否可以删除 status = 1
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.in(Setmeal::getId, ids);
        lqw.eq(Setmeal::getStatus, 1);
        // 如果 不能删除 抛出业务异常
        int count = this.count(lqw);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        // 删除套餐 setmeal
        this.removeByIds(ids);
        // 删除菜品 setmeal_dish
        LambdaQueryWrapper<SetmealDish> lqw1 = new LambdaQueryWrapper<>();
        lqw1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lqw1);
    }
}
