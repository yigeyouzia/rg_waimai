package com.cyt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyt.reggie.common.R;
import com.cyt.reggie.entity.User;
import com.cyt.reggie.service.UserService;
import com.cyt.reggie.utils.SMSUtils;
import com.cyt.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author cyt
 * @version 1.0
 */
@SuppressWarnings({"all"})
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // 生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码code = {}", code);
            // 调用阿里云api
            SMSUtils.sendMessage("瑞吉外卖", "SMS_460740617", phone, code);
            // 保存验证码Session
            // session.setAttribute(phone, code);
            // 将生成的验证码缓存到redis  设置有效期五分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");
    }

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 获取session验证码
        // Object codeInSession = session.getAttribute(phone);
        // 从redis获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        // 比对验证码
        if (codeInSession != null && codeInSession.equals(code)) { // 登录成功
            // 查询用户是否存在
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            if (user == null) {
                // 如果是新用户 自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            // 登录成功 删除验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
