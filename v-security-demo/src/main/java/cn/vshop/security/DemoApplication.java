package cn.vshop.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/25 0:27
 */
@SpringBootApplication
@RestController
// 自动生成文档
@EnableSwagger2
public class DemoApplication {

    /**
     * 启动类
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

}
