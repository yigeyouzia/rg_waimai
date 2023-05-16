package com.cyt.reggie.controller;

import com.cyt.reggie.common.R;
import com.cyt.reggie.entity.Orders;
import com.cyt.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyt
 * @version 1.0
 */
@SuppressWarnings({"ALl"})
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 支付下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据={}", orders);
        ordersService.submit(orders);
        return R.success("下单成功✨🥰");
    }
}
