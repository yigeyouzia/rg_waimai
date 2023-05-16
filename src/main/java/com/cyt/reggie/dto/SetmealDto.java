package com.cyt.reggie.dto;

import com.cyt.reggie.entity.Setmeal;
import com.cyt.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
