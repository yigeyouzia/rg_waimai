package com.cyt.reggie;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author cyt
 * @date 2023/11/9 18:39
 * @desc
 */
@SpringBootTest(classes = ReggieApplication.class)
@SuppressWarnings({"all"})
public class HttpRequestTest {
    /**
     * Spring Boot Post请求
     */
    @Test
    public void testPostRequest() {
        String url = "http://121.43.163.212:5244/api/auth/login";
        //        请求表
        JSONObject paramMap = new JSONObject();
        paramMap.put("username", "admin");
        paramMap.put("password", "@2023cyt");
        //        请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("user-agent", "Mozilla/5.0 WindSnowLi-Blog");
        //        整合请求头和请求参数
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(paramMap, headers);
        //         请求客户端
        RestTemplate client = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN));
        client.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        //      发起请求
        JSONObject body = client.postForEntity(url, httpEntity, JSONObject.class).getBody();
        System.out.println("******** POST请求 *********");
        assert body != null;
        System.out.println(body.toJSONString());
    }

    /**
     * Spring Boot Get请求
     */
    @Test
    public void testGetRequest() {
        String url = "https://www.blog.firstmeet.xyz/";
        //         请求客户端
        RestTemplate client = new RestTemplate();
        //      发起请求
        String body = client.getForEntity(url, String.class).getBody();
        System.out.println("******** Get请求 *********");
        assert body != null;
        System.out.println(body);
    }
}
