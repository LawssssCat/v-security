package cn.vshop.security.web.controller;

// 注意：这里用了静态导入了三个方法

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;

/**
 * '@RunWith' 一个注解，能用来指定用什么运行器，来运行测试
 * <p>
 * SpringRunner 运行器，会在测试开始的时候自动创建Spring的应用上下文 ApplicationContext
 * (如果不用SpringRunner作为运行器，默认会走SpringBoot提供的运行方法，即使用生产环境的入口做为启动的入口。)
 * (本例中，生产环境的入口为DemoApplication的main方法)
 * <p>
 * 这样做，避免了很多不必要的启动，如避免了内置的tomcat的启动(在测试环境不可能用到tomcat)
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/29 1:04
 */
@RunWith(SpringRunner.class)
// @SpringBootTest会被SpringBoot在测试环境创建，并为其注入依赖(即会使@Autowired等注解生效)
@SpringBootTest
@Slf4j
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    // 伪造的一个mvc环境,发送和判断rest请求是否符合预期
    private MockMvc mockMvc;

    /**
     * 被@Before注解的方法会在每个用例执行前执行
     * 用于在每个测试案例执行前，伪造出mvc环境
     */
    @Before
    public void setup() {
        // 通过 MockMvcBuilders 的 webAppContextSetup(WebApplicationContext context) 方法
        // 获取 DefaultMockMvcBuilder，再调用 build() 方法，初始化 MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 用户查询成功的测试案例
     * 调用 GET /user 成功
     */
    @Test
    public void whenQuerySuccess() throws Exception {
        String result = mockMvc
                // 通过 perform(执行) 模拟执行发送请求，并接受返回值
                .perform(
                        // 模拟一个 get 请求
                        get("/user")
                                // 指定请求的类型为:application/json;charset=UTF-8
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                //请求中包含的参数
                                .param("username", "jojo")
                                .param("age", "18")
                )
                // 写期望服务器端返回的东西
                .andExpect(
                        // 期望响应码是200
                        status().isOk()
                )
                // 写期望服务器端返回的东西
                .andExpect(
                        //解析返回的json内容
                        //“$.length()”的语法参考：https://github.com/jayway/JsonPath
                        //意思是：获取的响应是个数组，数组长度为3
                        jsonPath("$.length()").value(3)
                )
                // 把接收到的响应返回，以字符串的形式
                .andReturn().getResponse().getContentAsString();
        // 打印响应返回值
        log.info(result);
    }

    /**
     * 查询用户详情
     * 调用 GET /user/1 成功
     */
    @Test
    public void whenGetInfoSuccess() throws Exception {
        String result = mockMvc.perform(get("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("boo"))
                .andReturn().getResponse().getContentAsString();
        log.info(result);
    }

    /**
     * 调用 GET /user/1 失败
     */
    @Test
    public void whenGetInfoFail() throws Exception {
        mockMvc.perform(
                // 接口要求"/user/数字"
                get("/user/a").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(
                        // 希望抛出4xx异常
                        status().is4xxClientError()
                );
    }

    /**
     * 添加用户
     * 调用 POST /user 成功
     */
    @Test
    public void whenCreateSuccess() throws Exception {
        // 前台以时间戳形式向后台传时间
        long timestamp = new Date().getTime();
        // post 的内容
        String content = " {\"username\":\"\",\"password\":\"222\",\"balance\":100, \"birthday\":" + timestamp + "}";
        log.info(content);

        String result = mockMvc.perform(
                // 发送post请求
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        // 设置post的内容
                        .content(content))
                // 响应200请求
                .andExpect(status().isOk())
                // 期望响应user的id=1
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();

        // 打印添加成功的返回值
        log.info(result);
    }

    /**
     * 修改用户
     * 调用 PUT /user 成功
     */
    @Test
    public void whenUpdateSuccess() throws Exception {
        //伪造一个未来的时间
        // long time = System.currentTimeMillis() + 365 * 24 * 60 * 1000;
        // 或者:
        // 指定时区为系统默认时区
        ZoneId zone = ZoneId.systemDefault();
        // 指定时间戳为未来一年后的时间
        long timestamp = LocalDateTime.now().plusYears(1)
                // 上面的时区设置进来
                .atZone(zone)
                // 获取这个时区这个时刻的时间戳
                .toInstant().toEpochMilli();
        String content = "{\"id\":\"1\",\"username\":\"foo\",\"password\":\"222\",\"balance\":100, \"birthday\":" + timestamp + "}";
        log.info(content);
        String result = mockMvc.perform(
                put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    /**
     * 删除用户
     * 调用 DELETE /user/1 成功
     */
    @Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // 后面重构，controller返回自定义json类时，再判断返回状态
    }

    /**
     * 上传文件
     * 调用 /file 成功
     */
    @Test
    public void whenUploadSuccess() throws Exception {
        // 模拟一个上传的文件
        MockMultipartFile file = new MockMultipartFile(
                // 文件上传的名字
                "file",
                // 文件在客户端的名字
                "test.txt",
                // 请求体类型
                "multipart/form-data",
                // 文件内容(字节码形式)
                "hello upload".getBytes("UTF-8")
        );
        mockMvc.perform(fileUpload("/file")
                // 设置要上传的文件
                .file(file))
                .andExpect(status().isOk());
    }

}


