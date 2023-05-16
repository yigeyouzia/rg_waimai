package com.cyt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyt.reggie.entity.Orders;

/**
 * @author cyt
 * @version 1.0
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
