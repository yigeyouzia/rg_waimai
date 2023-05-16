package com.cyt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyt.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cyt
 * @version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
