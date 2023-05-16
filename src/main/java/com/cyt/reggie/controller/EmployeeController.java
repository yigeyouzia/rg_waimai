package com.cyt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyt.reggie.common.R;
import com.cyt.reggie.entity.Employee;
import com.cyt.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author cyt
 * @version 1.0
 */
@SuppressWarnings({"ALl"})
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        System.out.println(employee);

        // 1. md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2.查数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);
        // 3,判断是否查到
        if (emp == null) {
            return R.error("没有该账户");
        }
        // 4.比对密码 不成功
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败，密码错误");
        }
        // 5.查询员工状态是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("此账户已被禁用");
        }

        // 6.登录成功放入session
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest request) {
        // 清除session中当前员工的id
        request.getSession().removeAttribute("emloyee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        // 1.设置初始密码 md5
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        // 2.获得当前用户id
        Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageisze = {}, name = {}", page, pageSize, name);
        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 构建条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        lqw.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(pageInfo, lqw);
        return R.success(pageInfo);
    }

    /**
     * 禁用员工
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee {}", employee.toString());

        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 修改员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息");
        return R.success(employeeService.getById(id));
    }
}
