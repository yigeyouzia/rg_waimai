package com.cyt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyt.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cyt
 * @version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
