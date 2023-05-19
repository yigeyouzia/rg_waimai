package com.cyt.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author cyt
 * @version 1.0
 */
@SpringBootApplication
@ServletComponentScan
@Slf4j
@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching // 开启缓存注解
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功...");
    }
}
