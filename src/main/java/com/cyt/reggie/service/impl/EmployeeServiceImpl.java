package com.cyt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyt.reggie.entity.Employee;
import com.cyt.reggie.mapper.EmployeeMapper;
import com.cyt.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author cyt
 * @version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
