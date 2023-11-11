package com.cyt.reggie;

import com.cyt.reggie.utils.SMSUtils;
import com.cyt.reggie.utils.ValidateCodeUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cyt
 * @version 1.0
 */

@SpringBootTest(classes = ReggieApplication.class)
@SuppressWarnings({"all"})
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("002", "name", "xry");
        hashOperations.put("002", "age", "20");
    }

    @Test
    void testSendMsg() {
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        System.out.println(code);
        SMSUtils.sendMessage("阿里五五集团", "SMS_460740617", "18181761872", code);
    }

}
