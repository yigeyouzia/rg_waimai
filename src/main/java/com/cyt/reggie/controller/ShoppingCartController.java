package com.cyt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyt.reggie.common.BaseContext;
import com.cyt.reggie.common.R;
import com.cyt.reggie.entity.ShoppingCart;
import com.cyt.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cyt
 * @version 1.0
 */
@Slf4j
@SuppressWarnings({"all"})
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingCart() {
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lqw.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }

    /**
     * 添加菜品或者套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加菜品{}", shoppingCart);
        // 设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        // 查询当前菜品或套餐是否在购物车 +1
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            // 添加菜品
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {
            // 添加套餐
            shoppingCart.setCreateTime(LocalDateTime.now());
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart catServiceOne = shoppingCartService.getOne(lqw);
        if (catServiceOne != null) {
            // 已经存在
            Integer number = catServiceOne.getNumber();
            catServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(catServiceOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            catServiceOne = shoppingCart;
        }
        // 不存在
        return R.success(catServiceOne);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lqw);
        return R.success("清空购物车成功");
    }
}
