package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.entity.OrderDetail;
import com.cyt.reggie.mapper.OrderDetailMapper;
import com.cyt.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author cyt
 * @version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
