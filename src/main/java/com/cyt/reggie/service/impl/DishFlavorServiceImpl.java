package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.entity.DishFlavor;
import com.cyt.reggie.mapper.DishFlavorMapper;
import com.cyt.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author cyt
 * @version 1.0
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
