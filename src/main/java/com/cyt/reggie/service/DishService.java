package com.cyt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyt.reggie.dto.DishDto;
import com.cyt.reggie.entity.Dish;

/**
 * @author cyt
 * @version 1.0
 */
public interface DishService extends IService<Dish> {
    // 新增菜品 同时增加口味
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息及口味信息
    public DishDto getWithFlavor(Long id);

    // 更新菜品信息 同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
