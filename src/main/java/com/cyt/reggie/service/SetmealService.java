package com.cyt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyt.reggie.dto.SetmealDto;
import com.cyt.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author cyt
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐 同时保存套餐的菜品
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐 同时删除菜品
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
