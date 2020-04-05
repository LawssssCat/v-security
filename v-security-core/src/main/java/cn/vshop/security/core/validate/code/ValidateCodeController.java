package cn.vshop.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 1:33
 */
@RestController
public class ValidateCodeController {

    /**
     * 校验码信息存入session中的key
     */
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    /**
     * 注入接口，接口的实现完成验证码的生成和封装
     */
    @Autowired
    private ValidateCodeGenerator validateCodeGenerator ;


    /**
     * Spring 的工具类，用以操作session
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成随机的验证码，并封装为 ImageCode
        ImageCode imageCode = validateCodeGenerator.createImageCode(request);
        // 获取session，并把键值对存入session
        sessionStrategy.setAttribute(
                // ServletWebRequest 是一个适配器（Adapter），把servlet封装成spring的WebRequest（继承了RequestAttributes）
                // 通过把请求传进来，sessionStrategy会从请求中获取session
                new ServletWebRequest(request),
                // 存入session中的key
                SESSION_KEY,
                // 存入session中的值
                imageCode);
        // javax的io工具包
        // 将BufferedImage以指定格式写入输出流中
        ImageIO.write(
                // 以BufferedImage类型的图片
                imageCode.getImage(),
                // 输出的图片格式
                "JPEG",
                // 输出流，输出到响应体中
                response.getOutputStream());

    }

}
