package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.entity.User;
import com.cyt.reggie.mapper.UserMapper;
import com.cyt.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author cyt
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
