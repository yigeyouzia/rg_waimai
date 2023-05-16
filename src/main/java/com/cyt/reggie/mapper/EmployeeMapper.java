package com.cyt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyt.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cyt
 * @version 1.0
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
