package com.cyt.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author cyt
 * @version 1.0
 */

// 全局异常处理
@ControllerAdvice(annotations = {RestController.class, Controller.class}) // 拦截加了这个注解的类
@ResponseBody // 返回json
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.info(ex.getMessage());
        // 是这种异常 违反唯一约束
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            return R.error(split[2] + "用户已存在");
        }
        return R.error("未知错误");
    }

    /**
     * 异常处理
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
