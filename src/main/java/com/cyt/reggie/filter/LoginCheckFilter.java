package com.cyt.reggie.filter;

/**
 * @author cyt
 * @version 1.0
 */

import com.alibaba.fastjson.JSON;
import com.cyt.reggie.common.BaseContext;
import com.cyt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 具体逻辑
        // 1.获取uri
        String requestURI = request.getRequestURI();
        // 2.判断是否处理
        String[] urls = new String[]{  // 不需要处理的请求路径
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "swagger-resources",
                "/v2/api-docs"
        };
        boolean check = check(urls, requestURI);
        // 3.不需要处理 直接放行
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        // 4-1.判断登录 如果登录 则放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已，用户id={}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            // 线程放入id
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }
        // 4-2.判断登录 如果登录 则放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已，用户id={}", request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            // 线程放入id
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        // 5.没有登录 拦截  通过输出流的方式向客户端响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    // 路径匹配
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
